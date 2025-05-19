package visuals;

import GestionBBDD.ConexionBD;
import GestionBBDD.CheckBBDD;
import GestionBBDD.AddBBDD;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Clase principal que gestiona la interfaz gráfica de usuario
 * Implementa un JFrame principal que cambia entre diferentes JPanels
 */
public class MainInterface extends JFrame {

    // Gestor de paneles
    private PanelManager panelManager;

    // Conexión a la base de datos
    private ConexionBD conexionBD;
    private CheckBBDD checker;
    private AddBBDD adder;

    // Constructor
    public MainInterface() {
        // Configurar la conexión a la BD
        conexionBD = new ConexionBD();
        checker = new CheckBBDD(conexionBD);
        adder = new AddBBDD(conexionBD);

        // Configurar ventana principal
        setTitle("BuyMe Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        // Inicializar gestor de paneles
        panelManager = new PanelManager(this);

        // Crear paneles iniciales
        LoginPanel loginPanel = new LoginPanel(panelManager, checker);
        RegisterPanel registerPanel = new RegisterPanel(panelManager, checker, adder);
        WelcomePanel welcomePanel = new WelcomePanel(panelManager);

        // Registrar los paneles en el gestor
        panelManager.registerPanel("welcome", welcomePanel);
        panelManager.registerPanel("login", loginPanel);
        panelManager.registerPanel("register", registerPanel);

        // Mostrar panel de bienvenida al iniciar
        panelManager.showPanel("welcome");

        // Hacer visible la ventana
        setVisible(true);
    }

    // Método principal para iniciar la aplicación
    public static void main(String[] args) {
        // Usar SwingUtilities para crear y mostrar la GUI en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            try {
                // Usar look and feel del sistema
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MainInterface();
        });
    }
}