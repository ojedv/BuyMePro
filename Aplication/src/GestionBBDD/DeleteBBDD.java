package GestionBBDD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteBBDD {

    private ConexionBD conexionBD;

    public DeleteBBDD(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /*
    Esta clase sirve para eliminar registros de la base de datos.
    En cada metodo se especifica con el parámetro qué registro se eliminará.
    */

    /**
     * Elimina un Usuario de la base de datos basado en su nickname o correo.
     * @param usuario El usuario a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     */
    public boolean delete(Usuario usuario) {
        String sql = "DELETE FROM usuario WHERE nickname = ? OR correo = ?";
        
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            
            ps.setString(1, usuario.getNickname());
            ps.setString(2, usuario.getCorreo());
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Elimina un Producto de la base de datos basado en su nombre.
     * @param producto El producto a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     */
    public boolean delete(Producto producto) {
        String sql = "DELETE FROM producto WHERE nombre = ?";
        
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            
            ps.setString(1, producto.getNombre());
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Elimina un Supermercado de la base de datos basado en su nombre.
     * @param supermercado El supermercado a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     */
    public boolean delete(Supermercado supermercado) {
        String sql = "DELETE FROM supermercado WHERE nombre = ?";
        
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            
            ps.setString(1, supermercado.getNombre());
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar supermercado: " + e.getMessage());
            return false;
        }
    }
}