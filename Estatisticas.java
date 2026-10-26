import java.util.ArrayList;
import java.util.List;

public class Estatisticas {

    public static void imprimirEstatisticasEstudantes(List<Estudante> estudantes) {
        System.out.println("\\n===== ESTATÍSTICAS DOS ESTUDANTES =====");
        int total = estudantes.size();

        if (total == 0) {
            System.out.println("Não existem estudantes para analisar.");
            return;
        }

        int masculinos = 0, femininos = 0;
        int[] porAno = new int[5];
        int abaixo10 = 0, de10a139 = 0, de14a169 = 0, aPartir17 = 0;
        int menor18 = 0, idade18a20 = 0, idade21a23 = 0, idade24a26 = 0, maior26 = 0;
        int somaIdades = 0;
        double somaMedias = 0;
        Estudante melhor = estudantes.get(0);
        Estudante pior = estudantes.get(0);
        int menorIdade = Integer.MAX_VALUE, maiorIdade = Integer.MIN_VALUE;

        // Lista simples de cursos para não usar Map como estrutura principal.
        List<String> cursos = new ArrayList<>();
        List<Integer> quantidadesCurso = new ArrayList<>();
        List<Double> somasCurso = new ArrayList<>();

        for (Estudante e : estudantes) {
            String sexo = e.getSexo() == null ? "" : e.getSexo().trim();
            if (sexo.equalsIgnoreCase("M")) masculinos++;
            if (sexo.equalsIgnoreCase("F")) femininos++;

            if (e.getAno() >= 1 && e.getAno() <= 4) porAno[e.getAno()]++;

            double media = e.getMedia();
            somaMedias += media;
            if (media < melhor.getMedia()) { /* só para manter a comparação abaixo clara */ }
            if (media > melhor.getMedia()) melhor = e;
            if (media < pior.getMedia()) pior = e;

            if (media < 10) abaixo10++;
            else if (media < 14) de10a139++;
            else if (media < 17) de14a169++;
            else aPartir17++;

            int idade = e.getIdade();
            somaIdades += idade;
            menorIdade = Math.min(menorIdade, idade);
            maiorIdade = Math.max(maiorIdade, idade);
            if (idade < 18) menor18++;
            else if (idade <= 20) idade18a20++;
            else if (idade <= 23) idade21a23++;
            else if (idade <= 26) idade24a26++;
            else maior26++;

            String curso = e.getCurso() == null ? "(sem curso)" : e.getCurso();
            int idx = cursos.indexOf(curso);
            if (idx == -1) {
                cursos.add(curso);
                quantidadesCurso.add(1);
                somasCurso.add(media);
            } else {
                quantidadesCurso.set(idx, quantidadesCurso.get(idx) + 1);
                somasCurso.set(idx, somasCurso.get(idx) + media);
            }
        }

        System.out.println("Estudantes analisados: " + total);
        System.out.printf("Masculinos: %d (%.2f%%)%n", masculinos, 100.0 * masculinos / total);
        System.out.printf("Femininos: %d (%.2f%%)%n", femininos, 100.0 * femininos / total);
        System.out.println("\\nEstudantes por ano:");
        for (int ano = 1; ano <= 4; ano++) {
            System.out.println(ano + ".º ano: " + porAno[ano]);
        }

        System.out.println("\\nEstatísticas das médias:");
        System.out.printf("Média geral: %.2f%n", somaMedias / total);
        System.out.printf("Maior média: %.2f (%s)%n", melhor.getMedia(), melhor.getNome());
        System.out.printf("Menor média: %.2f (%s)%n", pior.getMedia(), pior.getNome());
        System.out.println("Média inferior a 10: " + abaixo10);
        System.out.println("Média entre 10 e 13,9: " + de10a139);
        System.out.println("Média entre 14 e 16,9: " + de14a169);
        System.out.println("Média igual ou superior a 17: " + aPartir17);

        System.out.println("\\nEstatísticas das idades:");
        System.out.printf("Idade média: %.2f%n", (double) somaIdades / total);
        System.out.println("Menor idade: " + menorIdade);
        System.out.println("Maior idade: " + maiorIdade);
        System.out.println("< 18: " + menor18);
        System.out.println("18–20: " + idade18a20);
        System.out.println("21–23: " + idade21a23);
        System.out.println("24–26: " + idade24a26);
        System.out.println("> 26: " + maior26);

        System.out.println("\\nEstatísticas por curso:");
        for (int i = 0; i < cursos.size(); i++) {
            int quantidade = quantidadesCurso.get(i);
            System.out.println(cursos.get(i));
            System.out.println("  Estudantes: " + quantidade);
            System.out.printf("  Percentagem: %.2f%%%n", 100.0 * quantidade / total);
            System.out.printf("  Média académica: %.2f%n", somasCurso.get(i) / quantidade);
        }
    }

    public static Estudante pesquisaSequencial(List<Estudante> estudantes, String codigo) {
        for (Estudante e : estudantes) {
            if (e.getCodigo().equals(codigo)) {
                return e;
            }
        }
        return null;
    }
}
