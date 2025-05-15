import java.util.List;

public class Grupo {
    private int idGrupo;
    private String nombre;
    List<Supermercado> supermercados;

    // Constructor con parámetros
    public Grupo(String nombre) {
        this.nombre = nombre;
    }


    // Getters y Setters
    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getNombre() {
        return nombre;
    }

    // toString para mostrar la información del objeto
    @Override
    public String toString() {
        return "Grupo{" +
                "idGrupo=" + idGrupo +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
