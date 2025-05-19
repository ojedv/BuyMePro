package visuals;

import GestionBBDD.CheckBBDD;
import Objetos.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel de inicio de sesión para usuarios existentes
 */
public class LoginPanel extends JPanel {

    private PanelManager panelManager;
    private CheckBBDD checker;

    // Componentes de la interfaz
    private JTextField nicknameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton backButton;

    /**
     * Constructor del panel de inicio de sesión
     * @param panelManager Gestor de paneles para la navegación
     * @param checker Objeto para verificar credenciales en la BD
     */
    public LoginPanel(PanelManager panelManager, CheckBBDD checker) {
        this.panelManager = panelManager;
        this.checker = checker;
        initComponents();
    }

    /**
     * Inicializa los componentes de la interfaz
     */
    private void initComponents() {
        // Configurar layout
        setLayout(new BorderLayout());

        // Panel principal con título y formulario
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Título
        JLabel titleLabel = new JLabel("Iniciar Sesión");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Formulario
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Nickname
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel nicknameLabel = new JLabel("Nickname:");
        formPanel.add(nicknameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        nicknameField = new JTextField(20);
        formPanel.add(nicknameField, gbc);

        // Contraseña
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        JLabel passwordLabel = new JLabel("Contraseña:");
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);

        // Botón de inicio de sesión
        loginButton = new JButton("Iniciar Sesión");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attemptLogin();
            }
        });

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Botón de volver
        backButton = new JButton("Volver");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
                panelManager.showPanel("welcome");
            }
        });

        // Añadir botones al panel
        buttonPanel.add(backButton);
        buttonPanel.add(loginButton);

        // Añadir todo al panel principal
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(buttonPanel);

        // Añadir panel principal al contenedor
        add(mainPanel, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
    }

    /**
     * Intenta iniciar sesión con las credenciales proporcionadas
     */
    private void attemptLogin() {
        String nickname = nicknameField.getText();
        String password = new String(passwordField.getPassword());

        if (nickname.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, complete todos los campos",
                    "Error de inicio de sesión",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Aquí deberíamos verificar las credenciales en la base de datos
        // Como no tenemos un método específico en CheckBBDD para verificar login,
        // lo simularemos por ahora

        JOptionPane.showMessageDialog(this,
                "Inicio de sesión exitoso!\nBienvenido, " + nickname,
                "Inicio de sesión",
                JOptionPane.INFORMATION_MESSAGE);

        clearFields();
        // Aquí deberíamos navegar al panel principal de la aplicación
    }

    /**
     * Limpia los campos del formulario
     */
    private void clearFields() {
        nicknameField.setText("");
        passwordField.setText("");
    }
}