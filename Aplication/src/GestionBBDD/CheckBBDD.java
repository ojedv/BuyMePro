package GestionBBDD;
import Objetos.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckBBDD {

    private ConexionBD conexionBD;

    public CheckBBDD(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /*
    Esta clase sirve para revisar si los datos que después introduciremos con el AddBBDD ya están creados
    en la BBDD. En cada método se especifica con el parámetro la revisión que se hace.
    */

    /**
     * Verifica si un Usuario ya existe en la base de datos basado en su nickname o correo.
     * @param usuario El usuario a verificar
     * @return true si no existe (puede añadirse), false si ya existe
     */
    public boolean check(Usuario usuario) {
        String sql = "SELECT COUNT(*) AS Coincidencias FROM usuario WHERE nickname = ? OR correo = ?";
        int count = 0;

        try (Connection conexion = conexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, usuario.getNickname());
            ps.setString(2, usuario.getCorreo());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("Coincidencias");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al comprobar usuario: " + e.getMessage());
            return false;
        }

        return count == 0;
    }

    /**
     * Verifica si un Producto ya existe en la base de datos por su nombre.
     */
    public boolean check(Producto producto) {
        String sql = "SELECT COUNT(*) AS Coincidencias FROM producto WHERE nombre = ?";
        int count = 0;

        try (Connection conexion = conexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, producto.getNombre());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("Coincidencias");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al comprobar producto: " + e.getMessage());
            return false;
        }

        return count == 0;
    }

    /**
     * Verifica si un Supermercado ya existe en la base de datos por su nombre.
     */
    public boolean check(Supermercado supermercado) {
        String sql = "SELECT COUNT(*) AS Coincidencias FROM supermercado WHERE nombre = ?";
        int count = 0;

        try (Connection conexion = conexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, supermercado.getNombre());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("Coincidencias");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al comprobar supermercado: " + e.getMessage());
            return false;
        }

        return count == 0;
    }
}
