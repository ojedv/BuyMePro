import java.sql.*;

public class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/BuyMeProyect";
    private static final String USUARIO = "root";
    private static final String CONTRASEÑA = "";

    private static Connection conexion = null;

    // Método para obtener la conexión
    public static Connection obtenerConexion() {
        if (conexion == null) {
            try {
                // Cargar el driver
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Establecer la conexión
                conexion = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
                System.out.println("Conexión a la base de datos establecida con éxito");
            } catch (ClassNotFoundException e) {
                System.err.println("Error al cargar el driver: " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            }
        }
        return conexion;
    }

    // Método para cerrar la conexión
    public static void cerrarConexion(Connection conexion) {
        if (conexion != null) {
            try {
                conexion.close();
                conexion = null;
                System.out.println("Conexión a la base de datos cerrada");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}