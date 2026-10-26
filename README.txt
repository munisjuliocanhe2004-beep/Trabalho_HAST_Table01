PROJETO EDA — TABELA HASH COM DOUBLE HASHING

Ficheiros:
- Estudante.java: modelo do estudante (atributos e métodos).
- Estado.java: estados VAZIO, OCUPADO e REMOVIDO.
- Entrada.java: representa cada posição da tabela.
- TabelaHash.java: array próprio, double hashing, inserção, pesquisa, remoção e redimensionamento.
- LeitorCSV.java: leitura, conversão e validação do estudantes.csv.
- Estatisticas.java: estatísticas dos estudantes e pesquisa sequencial.
- Desempenho.java: testes para até 1.000, 5.000 e 10.000 registos e comparação de pesquisa.
- Main.java: menu textual.

Como executar:
1. Coloque estudantes.csv na mesma pasta dos ficheiros Java.
2. Abra o terminal nessa pasta.
3. Compile: javac *.java
4. Execute: java Main

Notas importantes:
- Requer Java 11 ou superior (FileReader com Charset).
- O leitor assume CSV simples separado por vírgulas, sem vírgulas dentro de campos entre aspas.
- As validações (ano 1–4, sexo M/F, média 0–20) são hipóteses razoáveis; confirme-as com o docente/dados reais.
- As estatísticas de colisões/sondagens contam as inserções normais, não as inserções internas durante o redimensionamento.
- Os testes de desempenho são experimentais; os tempos dependem do computador e devem ser executados várias vezes para um relatório rigoroso.
- O trabalho é uma base completa de estudo, mas deves compilar, testar com o CSV real e compreender cada classe antes da defesa.
