package GestionBBDD;

import Objetos.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteBBDD {

    private ConexionBD conexionBD;

    public DeleteBBDD(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    // Método para borrar un usuario por su nickname
    public boolean delete(Usuario usuario) {
        String sql = "DELETE FROM usuario WHERE nickname = ?";
        try (Connection conexion = conexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, usuario.getNickname());
            int filasEliminadas = ps.executeUpdate();
            return filasEliminadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }
}
