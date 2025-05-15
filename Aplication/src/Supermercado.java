public class Supermercado {
    private int id_supermercado;
    private String nombre;

    public Supermercado() {}

    public Supermercado(String nombre) {
        this.nombre = nombre;
    }

    public int getId_supermercado() {
        return id_supermercado;
    }

    public void setId_supermercado(int id_supermercado) {
        this.id_supermercado = id_supermercado;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return "Supermercado{" +
                "id_supermercado=" + id_supermercado +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
