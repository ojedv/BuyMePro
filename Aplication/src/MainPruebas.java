import GestionBBDD.*;
import Objetos.*;

public class MainPruebas {
    public static void main(String[] args) {

        ConexionBD conexionBD = new ConexionBD();

        Usuario usuarioPrueba = new Usuario(
                "prueba123",          // nickname
                "1234",               // contraseña (password)
                "Pedro",              // nombre
                "Pérez",              // apellidos
                "666777888",          // teléfono
                "prueba@email.com"    // correo
        );

        CheckBBDD checker = new CheckBBDD(conexionBD);
        AddBBDD adder = new AddBBDD(conexionBD);
        adder.add(usuarioPrueba);
    }
}
