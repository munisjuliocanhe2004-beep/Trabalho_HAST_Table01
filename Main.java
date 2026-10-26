import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        TabelaHash tabela = new TabelaHash();
        LeitorCSV leitor = new LeitorCSV();
        Scanner entrada = new Scanner(System.in);
        boolean executando = true;

        while (executando) {
            mostrarMenu();
            System.out.print("Opção: ");
            String opcao = entrada.nextLine().trim();

            try {
                switch (opcao) {
                    case "1":
                        System.out.print("Caminho do ficheiro CSV [estudantes.csv]: ");
                        String caminho = entrada.nextLine().trim();
                        if (caminho.isEmpty()) caminho = "estudantes.csv";
                        leitor.carregar(caminho, tabela);
                        break;

                    case "2":
                        System.out.print("Código a pesquisar: ");
                        String codigoPesquisa = entrada.nextLine().trim();
                        Estudante encontrado = tabela.pesquisar(codigoPesquisa);
                        if (encontrado == null) {
                            System.out.println("Estudante não encontrado.");
                        } else {
                            System.out.println(encontrado);
                        }
                        break;

                    case "3":
                        Estudante novo = lerEstudante(entrada);
                        tabela.inserir(novo);
                        System.out.println("Estudante inserido com sucesso.");
                        break;

                    case "4":
                        System.out.print("Código a remover: ");
                        String codigoRemover = entrada.nextLine().trim();
                        if (tabela.remover(codigoRemover)) {
                            System.out.println("Estudante removido.");
                        } else {
                            System.out.println("Código não encontrado.");
                        }
                        break;

                    case "5":
                        List<Estudante> estudantes = tabela.listarEstudantes();
                        Estatisticas.imprimirEstatisticasEstudantes(estudantes);
                        break;

                    case "6":
                        tabela.imprimirEstatisticas();
                        break;

                    case "7":
                        Desempenho.executar(tabela.listarEstudantes());
                        break;

                    case "8":
                        Desempenho.compararSequencialHash(
                                tabela.listarEstudantes(), tabela);
                        break;

                    case "0":
                        executando = false;
                        System.out.println("Programa terminado.");
                        break;

                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (IOException e) {
                System.out.println("Erro de leitura do ficheiro: " + e.getMessage());
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Operação não concluída: " + e.getMessage());
            } catch (RuntimeException e) {
                System.out.println("Dados inválidos: " + e.getMessage());
            }
        }

        entrada.close();
    }

    private static void mostrarMenu() {
        System.out.println("\\n==============================================");
        System.out.println("       SISTEMA DE GESTÃO DE ESTUDANTES");
        System.out.println("==============================================");
        System.out.println("1 - Carregar ficheiro CSV");
        System.out.println("2 - Pesquisar estudante");
        System.out.println("3 - Inserir estudante");
        System.out.println("4 - Remover estudante");
        System.out.println("5 - Estatísticas dos estudantes");
        System.out.println("6 - Estatísticas da Tabela Hash");
        System.out.println("7 - Executar teste de desempenho");
        System.out.println("8 - Comparar pesquisa sequencial e Hash");
        System.out.println("0 - Sair");
    }

    private static Estudante lerEstudante(Scanner entrada) {
        System.out.print("Código: ");
        String codigo = entrada.nextLine().trim();
        System.out.print("Nome: ");
        String nome = entrada.nextLine().trim();
        System.out.print("Curso: ");
        String curso = entrada.nextLine().trim();
        int ano = lerInteiro(entrada, "Ano (1-4): ");
        int idade = lerInteiro(entrada, "Idade: ");
        System.out.print("Sexo (M/F): ");
        String sexo = entrada.nextLine().trim().toUpperCase();
        double media = lerDouble(entrada, "Média (0-20): ");

        if (codigo.isEmpty() || nome.isEmpty() || curso.isEmpty()
                || ano < 1 || ano > 4 || idade <= 0
                || !(sexo.equals("M") || sexo.equals("F"))
                || !Double.isFinite(media) || media < 0 || media > 20) {
            throw new IllegalArgumentException("Os dados introduzidos não são válidos.");
        }

        return new Estudante(codigo, nome, curso, ano, idade, sexo, media);
    }

    private static int lerInteiro(Scanner entrada, String mensagem) {
        System.out.print(mensagem);
        return Integer.parseInt(entrada.nextLine().trim());
    }

    private static double lerDouble(Scanner entrada, String mensagem) {
        System.out.print(mensagem);
        return Double.parseDouble(entrada.nextLine().trim().replace(',', '.'));
    }
}
