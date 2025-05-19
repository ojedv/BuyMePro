package visuals;

import GestionBBDD.CheckBBDD;
import GestionBBDD.AddBBDD;
import Objetos.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel de registro para nuevos usuarios
 */
public class RegisterPanel extends JPanel {

    private PanelManager panelManager;
    private CheckBBDD checker;
    private AddBBDD adder;

    // Componentes de la interfaz
    private JTextField nicknameField;
    private JPasswordField passwordField;
    private JTextField nombreField;
    private JTextField apellidosField;
    private JTextField telefonoField;
    private JTextField correoField;

    /**
     * Constructor del panel de registro
     * @param panelManager Gestor de paneles para la navegación
     * @param checker Objeto para verificar usuario en la BD
     * @param adder Objeto para añadir usuario a la BD
     */
    public RegisterPanel(PanelManager panelManager, CheckBBDD checker, AddBBDD adder) {
        this.panelManager = panelManager;
        this.checker = checker;
        this.adder = adder;
        initComponents();
    }

    /**
     * Inicializa los componentes de la interfaz
     */
    private void initComponents() {
        // Configurar layout
        setLayout(new BorderLayout());

        // Panel de título
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Registro de Usuario");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);

        // Panel de formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);

        // Nickname
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Nickname:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        nicknameField = new JTextField(20);
        formPanel.add(nicknameField, gbc);

        // Contraseña
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Contraseña:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);

        // Nombre
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Nombre:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        nombreField = new JTextField(20);
        formPanel.add(nombreField, gbc);

        // Apellidos
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Apellidos:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        apellidosField = new JTextField(20);
        formPanel.add(apellidosField, gbc);

        // Teléfono
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Teléfono:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        telefonoField = new JTextField(20);
        formPanel.add(telefonoField, gbc);

        // Correo
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Correo:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        correoField = new JTextField(20);
        formPanel.add(correoField, gbc);

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Botón volver
        JButton backButton = new JButton("Volver");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
                panelManager.showPanel("welcome");
            }
        });

        // Botón registrar
        JButton registerButton = new JButton("Registrar");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attemptRegister();
            }
        });

        // Añadir botones al panel
        buttonPanel.add(backButton);
        buttonPanel.add(registerButton);

        // Añadir todos los paneles al contenedor principal
        add(titlePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Añadir padding
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    /**
     * Intenta registrar un nuevo usuario con los datos proporcionados
     */
    private void attemptRegister() {
        // Obtener valores de los campos
        String nickname = nicknameField.getText();
        String password = new String(passwordField.getPassword());
        String nombre = nombreField.getText();
        String apellidos = apellidosField.getText();
        String telefono = telefonoField.getText();
        String correo = correoField.getText();

        // Validar que todos los campos estén completos
        if (nickname.isEmpty() || password.isEmpty() || nombre.isEmpty() ||
                apellidos.isEmpty() || telefono.isEmpty() || correo.isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Por favor, complete todos los campos",
                    "Error de registro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear objeto usuario
        Usuario nuevoUsuario = new Usuario(nickname, password, nombre, apellidos, telefono, correo);

        // Verificar si ya existe el usuario
        if (!checker.check(nuevoUsuario)) {
            JOptionPane.showMessageDialog(this,
                    "El nickname o correo ya están en uso",
                    "Error de registro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Intentar añadir el usuario a la base de datos
        boolean registroExitoso = adder.add(nuevoUsuario);

        if (registroExitoso) {
            JOptionPane.showMessageDialog(this,
                    "¡Registro exitoso!\nAhora puede iniciar sesión",
                    "Registro completado",
                    JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            panelManager.showPanel("login");
        } else {
            JOptionPane.showMessageDialog(this,
                    "Ocurrió un error durante el registro",
                    "Error de registro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Limpia los campos del formulario
     */
    private void clearFields() {
        nicknameField.setText("");
        passwordField.setText("");
        nombreField.setText("");
        apellidosField.setText("");
        telefonoField.setText("");
        correoField.setText("");
    }
}