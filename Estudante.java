public class Estudante {
    private final String codigo;
    private String nome;
    private String curso;
    private int ano;
    private int idade;
    private String sexo;
    private double media;

    public Estudante(String codigo, String nome, String curso,
                     int ano, int idade, String sexo, double media) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("O código não pode estar vazio.");
        }
        this.codigo = codigo.trim();
        this.nome = nome;
        this.curso = curso;
        this.ano = ano;
        this.idade = idade;
        this.sexo = sexo;
        this.media = media;
    }

    public String getCodigo() { return codigo; }
    public String getNome() { return nome; }
    public String getCurso() { return curso; }
    public int getAno() { return ano; }
    public int getIdade() { return idade; }
    public String getSexo() { return sexo; }
    public double getMedia() { return media; }

    public void setNome(String nome) { this.nome = nome; }
    public void setCurso(String curso) { this.curso = curso; }
    public void setAno(int ano) { this.ano = ano; }
    public void setIdade(int idade) { this.idade = idade; }
    public void setSexo(String sexo) { this.sexo = sexo; }
    public void setMedia(double media) { this.media = media; }

    @Override
    public String toString() {
        return "Código: " + codigo
                + "\nNome: " + nome
                + "\nCurso: " + curso
                + "\nAno: " + ano
                + "\nIdade: " + idade
                + "\nSexo: " + sexo
                + "\nMédia: " + String.format("%.2f", media);
    }
}
