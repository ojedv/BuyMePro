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
 * Panel estilizado para inicio de sesión
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
        // Configuración del panel
        setLayout(new BorderLayout());
        setBackground(UITheme.WHITE);
        setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        // Panel de título
        JPanel titlePanel = UITheme.createTitlePanel("Iniciar Sesión");

        // Panel central con formulario
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(UITheme.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Panel del formulario con espaciado
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UITheme.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);

        // Etiqueta de nickname
        JLabel nicknameLabel = new JLabel("Nickname");
        UITheme.applyFormLabelStyle(nicknameLabel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        formPanel.add(nicknameLabel, gbc);

        // Campo de texto nickname
        nicknameField = new JTextField(20);
        UITheme.applyTextFieldStyle(nicknameField);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(nicknameField, gbc);

        // Espaciado entre campos
        gbc.gridy = 2;
        formPanel.add(Box.createVerticalStrut(10), gbc);

        // Etiqueta de contraseña
        JLabel passwordLabel = new JLabel("Contraseña");
        UITheme.applyFormLabelStyle(passwordLabel);
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(passwordLabel, gbc);

        // Campo de texto contraseña
        passwordField = new JPasswordField(20);
        UITheme.applyPasswordFieldStyle(passwordField);
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(passwordField, gbc);

        // Panel de formulario con alineación centrada
        JPanel formContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        formContainer.setBackground(UITheme.WHITE);
        formContainer.add(formPanel);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        buttonPanel.setBackground(UITheme.WHITE);
        buttonPanel.setMaximumSize(new Dimension(400, 50));

        // Botón volver
        backButton = new JButton("Volver");
        UITheme.applySecondaryButtonStyle(backButton);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Volver al panel de bienvenida
                IPanelSwitcher.openPanel(new WelcomeIPanel(IPanelSwitcher));
            }
        });

        // Botón iniciar sesión
        loginButton = new JButton("Iniciar Sesión");
        UITheme.applyPrimaryButtonStyle(loginButton);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validación del formulario
                if (nicknameField.getText().isEmpty() || passwordField.getPassword().length == 0) {
                    JOptionPane.showMessageDialog(LoginIPanel.this,
                            "Por favor, complete todos los campos",
                            "Error de inicio de sesión",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Lógica de inicio de sesión
                IPanelSwitcher.openPanel(new RoleSelectionPanel(IPanelSwitcher, nicknameField.getText()));
            }
        });

        // Añadir botones al panel de botones
        buttonPanel.add(backButton);
        buttonPanel.add(loginButton);

        // Panel contenedor de botones para centrado
        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonContainer.setBackground(UITheme.WHITE);
        buttonContainer.add(buttonPanel);

        // Añadir componentes al panel central
        centerPanel.add(formContainer);
        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(buttonContainer);

        // Añadir paneles al contenedor principal
        add(titlePanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    @Override
    public void closePanel(JPanel panel) {
        // Implementación requerida por la interfaz
    }

    @Override
    public void openPanel(JPanel panel) {
        // Implementación requerida por la interfaz
    }
}