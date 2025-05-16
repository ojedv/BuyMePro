import java.sql.Connection;//Establecer conexión
import java.sql.DriverManager;//Establecer conexión
import java.sql.PreparedStatement;
import java.sql.ResultSet;//Procesar resultados
import java.sql.SQLException;//Controlar errores
import java.sql.Statement;//Lanzar consultas sql


public class ConexionBD {

    private static final String URL = "jdbc:mysql://localhost:3306/instituto";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "";

    public Connection conectar() {
        try {
            return DriverManager.getConnection(URL, USUARIO, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos");
            e.printStackTrace();
            return null;
        }
    }
}
