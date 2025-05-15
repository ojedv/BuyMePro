public class Pedido {
    private int idPedido;
    private int idUsuario;

    // Constructor con todos los campos
    public Pedido(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    // Getters y Setters
    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdUsuario() {
        return idUsuario;
    }


    @Override
    public String toString() {
        return "Pedido{" +
                "idPedido=" + idPedido +
                ", idUsuario=" + idUsuario +
                '}';
    }
}
