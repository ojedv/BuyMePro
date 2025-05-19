import GestionBBDD.*;
import Objetos.*;

import javax.script.ScriptEngine;
import java.util.ArrayList;
import java.util.List;

public class MainPruebas {


    public static void main(String[] args) {

        ConexionBD conexionBD = new ConexionBD();

        CheckBBDD checker = new CheckBBDD(conexionBD);
        AddBBDD adder = new AddBBDD(conexionBD);
        DeleteBBDD deleter = new DeleteBBDD(conexionBD);


    }
}
