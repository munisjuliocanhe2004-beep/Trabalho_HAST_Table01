public class Entrada {
    private Estudante estudante;
    private Estado estado;

    public Entrada() {
        estudante = null;
        estado = Estado.VAZIO;
    }

    public Estudante getEstudante() {
        return estudante;
    }

    public Estado getEstado() {
        return estado;
    }

    public void ocupar(Estudante estudante) {
        this.estudante = estudante;
        this.estado = Estado.OCUPADO;
    }

    public void marcarRemovido() {
        estudante = null;
        estado = Estado.REMOVIDO;
    }

    public void marcarVazio() {
        estudante = null;
        estado = Estado.VAZIO;
    }
}
