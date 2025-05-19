package visuals;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import GestionBBDD.CheckBBDD;
import GestionBBDD.ConexionBD;
import Objetos.Usuario;
import interfaces.IPanelSwitcher;

/**
 * Panel simplificado para inicio de sesión
 */
public class LoginIPanel extends JPanel implements IPanelSwitcher {
    // Referencia al gestor de paneles
    private IPanelSwitcher IPanelSwitcher;

    // Componentes de la interfaz
    private JTextField nicknameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton backButton;

    // Para la base de datos
    private CheckBBDD checker;

    /**
     * Constructor del panel de inicio de sesión
     * @param IPanelSwitcher Gestor para cambiar entre paneles
     */
    public LoginIPanel(IPanelSwitcher IPanelSwitcher) {
        this.IPanelSwitcher = IPanelSwitcher;

        // Inicializar el checker para la base de datos
        ConexionBD conexionBD = new ConexionBD();
        this.checker = new CheckBBDD(conexionBD);

        setupPanel();
    }

    /**
     * Configura los componentes del panel
     */
    private void setupPanel() {
        // Usar BorderLayout como layout principal
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Panel de título
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Iniciar Sesión");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);

        // Panel de formulario
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 20));

        // Nickname
        formPanel.add(new JLabel("Nickname:"));
        nicknameField = new JTextField(20);
        formPanel.add(nicknameField);

        // Contraseña
        formPanel.add(new JLabel("Contraseña:"));
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));

        // Botón volver
        backButton = new JButton("Volver");
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Volver al panel de bienvenida
                IPanelSwitcher.openPanel(new WelcomeIPanel(IPanelSwitcher));
            }
        });

        // Botón iniciar sesión
        loginButton = new JButton("Iniciar Sesión");
        loginButton.setPreferredSize(new Dimension(120, 40));
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                IPanelSwitcher.openPanel(new RoleSelectionPanel(IPanelSwitcher, nicknameField.getText()));

            }
        });

        // Añadir botones al panel
        buttonPanel.add(backButton);
        buttonPanel.add(loginButton);

        // Añadir paneles al contenedor principal
        add(titlePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }



    @Override
    public void closePanel(JPanel panel) {

    }

    @Override
    public void openPanel(JPanel panel) {

    }
}