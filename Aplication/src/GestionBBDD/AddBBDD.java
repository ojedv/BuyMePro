package GestionBBDD;
import Objetos.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddBBDD {

    private ConexionBD conexionBD;

    public AddBBDD(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    public boolean add(Usuario usuario) {
        // Aquí NO toca cambiar nada, porque las columnas ya están en snake_case:
        String sql = "INSERT INTO usuario (nickname, contraseña, nombre, apellidos, telefono, correo) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conexion = conexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, usuario.getNickname());
            ps.setString(2, usuario.getPassword());
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
        // Aquí: cambiamos “id_Supermercado” → “id_supermercado”
        String sql = "INSERT INTO producto (nombre, precio, peso, id_supermercado) VALUES (?, ?, ?, ?)";

        try (Connection conexion = conexionBD.getConnection();
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
        // No se incluye columna de clave foránea aquí, quedaría igual:
        String sql = "INSERT INTO supermercado (nombre) VALUES (?)";

        try (Connection conexion = conexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, supermercado.getNombre());

            int filasInsertadas = ps.executeUpdate();
            return filasInsertadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al añadir el supermercado: " + e.getMessage());
            return false;
        }
    }

    public boolean add(Pedido pedido) {
        // Aquí: cambiamos “id_Usuario” → “id_usuario”
        String sql = "INSERT INTO pedido (id_usuario) VALUES (?)";

        try (Connection conexion = conexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, pedido.getIdUsuarioComprador());

            int filasInsertadas = ps.executeUpdate();
            return filasInsertadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al añadir el pedido: " + e.getMessage());
            return false;
        }
    }

    public boolean add(Grupo grupo) {
        // Aquí no cambia nada, “nombre” coincide
        String sql = "INSERT INTO grupo (nombre) VALUES (?)";

        try (Connection conexion = conexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, grupo.getNombre());

            int filasInsertadas = ps.executeUpdate();
            return filasInsertadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al añadir el grupo: " + e.getMessage());
            return false;
        }
    }
}
