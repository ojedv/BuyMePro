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
    Esta clase sirve para revisar si los datos que despues introduciremos con el AddBBDD, ya estan creados
    en la BBDD en cada metodo se especifica con el parametro la revision que se hace.
    Ej:
    check(Usuario): Revisamos si ya existe un usuario con ese nombre o correo (Devuelve booleano)
    */


    /**
     * Verifica si un Usuario ya existe en la base de datos basado en su nickname o correo.
     * @param usuario El usuario a verificar
     * @return true si no existe (puede añadirse), false si ya existe
     */
    public boolean check(Usuario usuario) { // Este metodo lo que hace es hacer un count de las coincidencias con el nombre o correo de los datos que hay en la BBDD y si encuentra alguna da error.
        String sql = "SELECT COUNT(*) AS Coincidencias FROM usuario WHERE nickname = ? OR correo = ?";
        int count = 0;

        try (Connection conexion = conexionBD.conectar();
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
            return false; // En caso de error, mejor retornar false por seguridad
        }

        return count == 0; // Si no hay registros con el mismo nickname o correo, puede añadirse
    }
    public boolean check(Producto producto) {
        String sql = "SELECT COUNT(*) AS Coincidencias FROM producto WHERE nombre = ?";
        int count = 0;

        try (Connection conexion = conexionBD.conectar();
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
    public boolean check(Supermercado supermercado) {
        String sql = "SELECT COUNT(*) AS Coincidencias FROM supermercado WHERE nombre = ?";
        int count = 0;

        try (Connection conexion = conexionBD.conectar();
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
