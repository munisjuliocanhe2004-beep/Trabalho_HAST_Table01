import java.util.List;

public class Desempenho {

    public static void executar(List<Estudante> estudantes) {
        if (estudantes.isEmpty()) {
            System.out.println("Carregue primeiro o ficheiro CSV.");
            return;
        }

        int[] tamanhos = {1000, 5000, 10000};
        System.out.println("\\n===== TESTES DE DESEMPENHO =====");

        for (int pedido : tamanhos) {
            int n = Math.min(pedido, estudantes.size());
            if (n == 0) continue;

            TabelaHash teste = new TabelaHash();
            long inicio = System.nanoTime();
            for (int i = 0; i < n; i++) {
                teste.inserir(estudantes.get(i));
            }
            long tempoInsercao = System.nanoTime() - inicio;

            int repeticoes = Math.min(1000, n);
            inicio = System.nanoTime();
            for (int i = 0; i < repeticoes; i++) {
                teste.pesquisar(estudantes.get(i).getCodigo());
            }
            long tempoPesquisa = System.nanoTime() - inicio;

            System.out.println("\\nRegistos: " + n);
            System.out.println("Colisões: " + teste.getTotalColisoes());
            System.out.printf("Média de sondagens/inserção: %.2f%n", teste.getMediaSondagensInsercao());
            System.out.println("Máximo de sondagens: " + teste.getMaiorSondagem());
            System.out.printf("Factor de carga: %.4f%n", teste.getFactorCarga());
            System.out.printf("Tempo de inserção: %.3f ms%n", tempoInsercao / 1_000_000.0);
            System.out.printf("Tempo de pesquisa (%d pesquisas): %.3f ms%n",
                    repeticoes, tempoPesquisa / 1_000_000.0);
        }

        System.out.println("\\nNota: os tempos variam conforme o computador e a execução.");
    }

    public static void compararSequencialHash(List<Estudante> estudantes, TabelaHash tabela) {
        if (estudantes.isEmpty()) {
            System.out.println("Carregue primeiro o ficheiro CSV.");
            return;
        }

        int repeticoes = Math.min(1000, estudantes.size());
        String[] existentes = new String[repeticoes];
        String[] inexistentes = new String[repeticoes];

        for (int i = 0; i < repeticoes; i++) {
            existentes[i] = estudantes.get(i).getCodigo();
            inexistentes[i] = "NAO_EXISTE_" + i;
        }

        compararGrupo("Códigos existentes", estudantes, tabela, existentes);
        compararGrupo("Códigos inexistentes", estudantes, tabela, inexistentes);
    }

    private static void compararGrupo(String titulo, List<Estudante> estudantes,
                                      TabelaHash tabela, String[] codigos) {
        long inicio = System.nanoTime();
        for (String codigo : codigos) {
            Estatisticas.pesquisaSequencial(estudantes, codigo);
        }
        long tempoSequencial = System.nanoTime() - inicio;

        inicio = System.nanoTime();
        for (String codigo : codigos) {
            tabela.pesquisar(codigo);
        }
        long tempoHash = System.nanoTime() - inicio;

        System.out.println("\\n" + titulo + " (" + codigos.length + " pesquisas)");
        System.out.printf("Pesquisa sequencial: %.3f ms (total)%n", tempoSequencial / 1_000_000.0);
        System.out.printf("Pesquisa Hash: %.3f ms (total)%n", tempoHash / 1_000_000.0);
    }
}
