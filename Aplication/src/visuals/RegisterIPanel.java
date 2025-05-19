package visuals;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import GestionBBDD.AddBBDD;
import GestionBBDD.CheckBBDD;
import GestionBBDD.ConexionBD;
import Objetos.Usuario;
import interfaces.IPanelSwitcher;

/**
 * Panel simplificado para registro de usuarios
 */
public class RegisterIPanel extends JPanel implements IPanelSwitcher {
    // Referencia al gestor de paneles
    private IPanelSwitcher IPanelSwitcher;

    // Componentes de la interfaz
    private JTextField nicknameField;
    private JPasswordField passwordField;
    private JTextField nombreField;
    private JTextField apellidosField;
    private JTextField telefonoField;
    private JTextField correoField;

    // Para la base de datos
    private CheckBBDD checker;
    private AddBBDD adder;

    /**
     * Constructor del panel de registro
     * @param IPanelSwitcher Gestor para cambiar entre paneles
     */
    public RegisterIPanel(IPanelSwitcher IPanelSwitcher) {
        this.IPanelSwitcher = IPanelSwitcher;

        // Inicializar objetos de base de datos
        ConexionBD conexionBD = new ConexionBD();
        this.checker = new CheckBBDD(conexionBD);
        this.adder = new AddBBDD(conexionBD);

        setupPanel();
    }

    /**
     * Configura los componentes del panel
     */
    private void setupPanel() {
        // Usar BorderLayout como layout principal
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Panel de título
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Registro de Usuario");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);

        // Panel de formulario
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 15));

        // Nickname
        formPanel.add(new JLabel("Nickname:"));
        nicknameField = new JTextField(20);
        formPanel.add(nicknameField);

        // Contraseña
        formPanel.add(new JLabel("Contraseña:"));
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField);

        // Nombre
        formPanel.add(new JLabel("Nombre:"));
        nombreField = new JTextField(20);
        formPanel.add(nombreField);

        // Apellidos
        formPanel.add(new JLabel("Apellidos:"));
        apellidosField = new JTextField(20);
        formPanel.add(apellidosField);

        // Teléfono
        formPanel.add(new JLabel("Teléfono:"));
        telefonoField = new JTextField(20);
        formPanel.add(telefonoField);

        // Correo
        formPanel.add(new JLabel("Correo:"));
        correoField = new JTextField(20);
        formPanel.add(correoField);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));

        // Botón volver
        JButton backButton = new JButton("Volver");
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Volver al panel de bienvenida
                IPanelSwitcher.openPanel(new WelcomeIPanel(IPanelSwitcher));
            }
        });

        // Botón registrar
        JButton registerButton = new JButton("Registrar");
        registerButton.setPreferredSize(new Dimension(120, 40));
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attemptRegister();
            }
        });

        // Añadir botones al panel
        buttonPanel.add(backButton);
        buttonPanel.add(registerButton);

        // Añadir paneles al contenedor principal
        add(titlePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Método para intentar registrar un usuario
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

            // Navegar al panel de login
            IPanelSwitcher.openPanel(new LoginIPanel(IPanelSwitcher));
        } else {
            JOptionPane.showMessageDialog(this,
                    "Ocurrió un error durante el registro",
                    "Error de registro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void closePanel(JPanel panel) {

    }

    @Override
    public void openPanel(JPanel panel) {

    }
}