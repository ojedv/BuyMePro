import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddBBDD {

    private ConexionBD conexionBD;

    // Constructor
    public AddBBDD(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /*
    Esta clase nos añade los atributos de cada clase previamente creada en Java a la BBDD usamos el add
    y pasamos como parametro lo que vayamos a insertar
    Ej:
    add(Usuario): Hacemos uso de los Getters para rellenar la insercion SQL que ejecutamos en este mismo
    metodo.
    */

    // Este metodo tambien serviria para añadir usuarios a la base de datos pero es mas inseguro.
    /*public void añadir(Usuario usuario) {
        String sql = "INSERT INTO usuario (nickname, contraseña, nombre, apellidos, telefono, correo) " +
                "VALUES ("+usuario.getNickname()+", "+usuario.getContraseña()+")";
        try (Connection conexion = conexionBD.conectar();
             Statement stmt = conexion.createStatement()) {

            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            System.err.println("Error al añadir el usuario: " + e.getMessage());
        }
    }*/

    // Método para añadir un Usuario a la base de datos
    public boolean add(Usuario usuario) {
        String sql = "INSERT INTO usuario (nickname, contraseña, nombre, apellidos, telefono, correo) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, usuario.getNickname());
            ps.setString(2, usuario.getPassword()); // Recuerda usar hashing para contraseñas en producción
            ps.setString(3, usuario.getNombre());
            ps.setString(4, usuario.getApellidos());
            ps.setString(5, usuario.getTelefono());
            ps.setString(6, usuario.getCorreo());

            int filasInsertadas = ps.executeUpdate();
            return filasInsertadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al añadir el usuario: " + e.getMessage());
            return false;
        }
    }
    public boolean add(Producto producto) {
        String sql = "INSERT INTO producto (nombre, precio, peso, idSupermercado) VALUES (?, ?, ?, ?)";

        try (Connection conexion = conexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, producto.getNombre());
            ps.setFloat(2, producto.getPrecio());
            ps.setFloat(3, producto.getPeso());
            ps.setInt(4, producto.getIdSupermercado());

            int filasInsertadas = ps.executeUpdate();
            return filasInsertadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al añadir el producto: " + e.getMessage());
            return false;
        }
    }
    public boolean add(Supermercado supermercado) {
        String sql = "INSERT INTO supermercado (nombre) VALUES (?)";

        try (Connection conexion = conexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, supermercado.getNombre());

            int filasInsertadas = ps.executeUpdate();
            return filasInsertadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al añadir el supermercado: " + e.getMessage());
            return false;
        }
    }
    public void add(Pedido pedido) {
        String sql = "INSERT INTO pedido (idUsuario) VALUES (?)";

        try (Connection conexion = conexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, pedido.getIdUsuario());

            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al añadir el pedido: " + e.getMessage());
        }
    }
    public void add(Grupo grupo) {
        String sql = "INSERT INTO grupo (nombre) VALUES (?)";

        try (Connection conexion = conexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, grupo.getNombre());

            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al añadir el grupo: " + e.getMessage());
        }
    }


}