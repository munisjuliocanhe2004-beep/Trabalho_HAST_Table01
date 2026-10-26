import java.util.ArrayList;
import java.util.List;

public class TabelaHash {
    private static final int CAPACIDADE_INICIAL = 1009;
    private static final double LIMITE_CARGA = 0.70;

    private Entrada[] tabela;
    private int tamanho;
    private final int capacidadeInicial;
    private long totalColisoes;
    private long totalSondagensInsercao;
    private int maiorSondagem;
    private int redimensionamentos;

    public TabelaHash() {
        this(CAPACIDADE_INICIAL);
    }

    public TabelaHash(int capacidadeInicial) {
        if (capacidadeInicial < 3) {
            capacidadeInicial = 3;
        }
        this.capacidadeInicial = proximoPrimo(capacidadeInicial);
        this.tabela = criarTabela(this.capacidadeInicial);
        this.tamanho = 0;
    }

    private Entrada[] criarTabela(int capacidade) {
        Entrada[] nova = new Entrada[capacidade];
        for (int i = 0; i < capacidade; i++) {
            nova[i] = new Entrada();
        }
        return nova;
    }

    // h1(k): posição inicial calculada a partir do código.
    private int h1(String codigo, int m) {
        return Math.floorMod(codigo.hashCode(), m);
    }

    // h2(k): passo entre tentativas. Como m é primo e o passo está entre
    // 1 e m-1, o passo é coprimo com m e pode percorrer toda a tabela.
    private int h2(String codigo, int m) {
        int hash = codigo.hashCode();
        int passo = 1 + Math.floorMod(hash / Math.max(1, m), m - 1);
        return passo;
    }

    private int indice(String codigo, int tentativa, int m) {
        long posicao = (long) h1(codigo, m) + (long) tentativa * h2(codigo, m);
        return (int) Math.floorMod(posicao, m);
    }

    public void inserir(Estudante estudante) {
        if (estudante == null) {
            throw new IllegalArgumentException("O estudante não pode ser null.");
        }

        if ((double) (tamanho + 1) / tabela.length > LIMITE_CARGA) {
            redimensionar(proximoPrimo(tabela.length * 2 + 1));
        }

        inserirInterno(estudante, true);
    }

    private void inserirInterno(Estudante estudante, boolean contarEstatisticas) {
        String codigo = estudante.getCodigo();
        int primeiraRemovida = -1;
        int sondagens = 0;

        for (int i = 0; i < tabela.length; i++) {
            int pos = indice(codigo, i, tabela.length);
            Entrada entrada = tabela[pos];
            sondagens++;

            if (entrada.getEstado() == Estado.OCUPADO) {
                if (entrada.getEstudante().getCodigo().equals(codigo)) {
                    throw new IllegalArgumentException("Já existe um estudante com o código " + codigo + ".");
                }
                if (contarEstatisticas) {
                    totalColisoes++;
                }
            } else if (entrada.getEstado() == Estado.REMOVIDO) {
                if (primeiraRemovida == -1) {
                    primeiraRemovida = pos;
                }
            } else { // VAZIO: podemos inserir aqui ou na primeira posição REMOVIDO.
                int destino = (primeiraRemovida != -1) ? primeiraRemovida : pos;
                tabela[destino].ocupar(estudante);
                tamanho++;
                registarSondagens(sondagens, contarEstatisticas);
                return;
            }
        }

        // Caso raro em que não existe VAZIO, mas existe uma posição REMOVIDO.
        if (primeiraRemovida != -1) {
            tabela[primeiraRemovida].ocupar(estudante);
            tamanho++;
            registarSondagens(sondagens, contarEstatisticas);
            return;
        }

        throw new IllegalStateException("A tabela não tem posições disponíveis.");
    }

    private void registarSondagens(int sondagens, boolean contar) {
        if (contar) {
            totalSondagensInsercao += sondagens;
            if (sondagens > maiorSondagem) {
                maiorSondagem = sondagens;
            }
        }
    }

    public Estudante pesquisar(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            return null;
        }

        for (int i = 0; i < tabela.length; i++) {
            Entrada entrada = tabela[indice(codigo, i, tabela.length)];

            if (entrada.getEstado() == Estado.VAZIO) {
                return null;
            }

            if (entrada.getEstado() == Estado.OCUPADO
                    && entrada.getEstudante().getCodigo().equals(codigo)) {
                return entrada.getEstudante();
            }
            // REMOVIDO não termina a pesquisa: a sondagem deve continuar.
        }
        return null;
    }

    public boolean remover(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            return false;
        }

        for (int i = 0; i < tabela.length; i++) {
            Entrada entrada = tabela[indice(codigo, i, tabela.length)];

            if (entrada.getEstado() == Estado.VAZIO) {
                return false;
            }

            if (entrada.getEstado() == Estado.OCUPADO
                    && entrada.getEstudante().getCodigo().equals(codigo)) {
                entrada.marcarRemovido();
                tamanho--;
                return true;
            }
        }
        return false;
    }

    private void redimensionar(int novaCapacidade) {
        Entrada[] antiga = tabela;
        tabela = criarTabela(novaCapacidade);
        int tamanhoAntigo = tamanho;
        tamanho = 0;

        for (Entrada entrada : antiga) {
            if (entrada.getEstado() == Estado.OCUPADO) {
                inserirInterno(entrada.getEstudante(), false);
            }
        }

        // Proteção contra alterações acidentais na contagem durante a rehash.
        if (tamanho != tamanhoAntigo) {
            throw new IllegalStateException("Erro ao redimensionar a Tabela Hash.");
        }
        redimensionamentos++;
    }

    public List<Estudante> listarEstudantes() {
        List<Estudante> lista = new ArrayList<>();
        for (Entrada entrada : tabela) {
            if (entrada.getEstado() == Estado.OCUPADO) {
                lista.add(entrada.getEstudante());
            }
        }
        return lista;
    }

    public int getTamanho() { return tamanho; }
    public int getCapacidadeInicial() { return capacidadeInicial; }
    public int getCapacidadeAtual() { return tabela.length; }
    public double getFactorCarga() { return (double) tamanho / tabela.length; }
    public long getTotalColisoes() { return totalColisoes; }
    public long getTotalSondagensInsercao() { return totalSondagensInsercao; }
    public double getMediaSondagensInsercao() {
        return tamanho == 0 ? 0.0 : (double) totalSondagensInsercao / tamanho;
    }
    public int getMaiorSondagem() { return maiorSondagem; }
    public int getRedimensionamentos() { return redimensionamentos; }

    public void imprimirEstatisticas() {
        System.out.println("\\n===== ESTATÍSTICAS DA TABELA HASH =====");
        System.out.println("Capacidade inicial: " + capacidadeInicial);
        System.out.println("Capacidade final: " + tabela.length);
        System.out.println("Elementos armazenados: " + tamanho);
        System.out.printf("Factor de carga: %.4f%n", getFactorCarga());
        System.out.println("Colisões contabilizadas nas inserções: " + totalColisoes);
        System.out.println("Sondagens nas inserções: " + totalSondagensInsercao);
        System.out.printf("Média de sondagens por inserção: %.2f%n", getMediaSondagensInsercao());
        System.out.println("Maior número de sondagens numa inserção: " + maiorSondagem);
        System.out.println("Redimensionamentos: " + redimensionamentos);
    }

    private static int proximoPrimo(int n) {
        while (!ehPrimo(n)) {
            n++;
        }
        return n;
    }

    private static boolean ehPrimo(int n) {
        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        for (int i = 3; (long) i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }
}
