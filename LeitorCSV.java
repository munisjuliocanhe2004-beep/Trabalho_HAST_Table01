import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LeitorCSV {
    private int totalLinhas;
    private int registosValidos;
    private int registosInvalidos;
    private int estudantesInseridos;

    public void carregar(String caminho, TabelaHash tabela) throws IOException {
        totalLinhas = 0;
        registosValidos = 0;
        registosInvalidos = 0;
        estudantesInseridos = 0;

        try (BufferedReader br = new BufferedReader(
                new FileReader(caminho, StandardCharsets.UTF_8))) {
            String linha = br.readLine(); // Ignorar cabeçalho.

            if (linha == null) {
                System.out.println("O ficheiro está vazio.");
                return;
            }

            while ((linha = br.readLine()) != null) {
                totalLinhas++;

                try {
                    Estudante estudante = converterLinha(linha);
                    if (estudante == null) {
                        registosInvalidos++;
                        continue;
                    }

                    registosValidos++;

                    try {
                        tabela.inserir(estudante);
                        estudantesInseridos++;
                    } catch (IllegalArgumentException e) {
                        // Um código duplicado é válido como linha, mas não é inserido.
                        System.out.println("Registo não inserido: " + e.getMessage());
                    }
                } catch (RuntimeException e) {
                    registosInvalidos++;
                }
            }
        }

        imprimirResumo();
    }

    private Estudante converterLinha(String linha) {
        String[] campos = linha.split(",", -1);
        if (campos.length != 7) {
            return null;
        }

        String codigo = campos[0].trim();
        String nome = campos[1].trim();
        String curso = campos[2].trim();
        int ano = Integer.parseInt(campos[3].trim());
        int idade = Integer.parseInt(campos[4].trim());
        String sexo = campos[5].trim().toUpperCase();
        double media = Double.parseDouble(campos[6].trim());

        boolean valido = !codigo.isEmpty()
                && !nome.isEmpty()
                && !curso.isEmpty()
                && ano >= 1 && ano <= 4
                && idade > 0
                && (sexo.equals("M") || sexo.equals("F"))
                && Double.isFinite(media)
                && media >= 0 && media <= 20;

        if (!valido) {
            return null;
        }

        return new Estudante(codigo, nome, curso, ano, idade, sexo, media);
    }

    private void imprimirResumo() {
        System.out.println("\\n===== RESUMO DA LEITURA CSV =====");
        System.out.println("Linhas de dados lidas (sem cabeçalho): " + totalLinhas);
        System.out.println("Registos válidos: " + registosValidos);
        System.out.println("Registos inválidos: " + registosInvalidos);
        System.out.println("Estudantes inseridos: " + estudantesInseridos);
    }

    public int getTotalLinhas() { return totalLinhas; }
    public int getRegistosValidos() { return registosValidos; }
    public int getRegistosInvalidos() { return registosInvalidos; }
    public int getEstudantesInseridos() { return estudantesInseridos; }
}
