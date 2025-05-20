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
 * Panel estilizado para registro de usuarios
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
        // Configuración del panel
        setLayout(new BorderLayout());
        setBackground(UITheme.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Panel de título
        JPanel titlePanel = UITheme.createTitlePanel("Registro de Usuario");

        // Panel de desplazamiento para formulario (por si la resolución es pequeña)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(UITheme.WHITE);

        // Crear el panel de formulario con GridBagLayout para mejor control
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UITheme.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Configuración de los campos
        setupFormField(formPanel, gbc, "Nickname:", nicknameField = new JTextField(), 0);
        setupFormField(formPanel, gbc, "Contraseña:", passwordField = new JPasswordField(), 1);
        setupFormField(formPanel, gbc, "Nombre:", nombreField = new JTextField(), 2);
        setupFormField(formPanel, gbc, "Apellidos:", apellidosField = new JTextField(), 3);
        setupFormField(formPanel, gbc, "Teléfono:", telefonoField = new JTextField(), 4);
        setupFormField(formPanel, gbc, "Correo:", correoField = new JTextField(), 5);

        // JScrollPane en caso de que la pantalla sea pequeña
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(UITheme.WHITE);
        scrollPane.getViewport().setBackground(UITheme.WHITE);

        // Añadir el scrollPane al panel de contenido
        contentPanel.add(scrollPane);
        contentPanel.add(Box.createVerticalStrut(20));

        // Panel de botones
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        buttonPanel.setBackground(UITheme.WHITE);
        buttonPanel.setMaximumSize(new Dimension(400, 50));

        // Botón volver
        JButton backButton = new JButton("Volver");
        UITheme.applySecondaryButtonStyle(backButton);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Volver al panel de bienvenida
                IPanelSwitcher.openPanel(new WelcomeIPanel(IPanelSwitcher));
            }
        });

        // Botón registrar
        JButton registerButton = new JButton("Registrar");
        UITheme.applyPrimaryButtonStyle(registerButton);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(checker.checkNickname(nicknameField.getText())) {
                    attemptRegister();
                } else {
                    showErrorDialog("El nickname ya está en uso");
                }
            }
        });

        // Añadir botones al panel de botones
        buttonPanel.add(backButton);
        buttonPanel.add(registerButton);

        // Panel contenedor de botones para centrado
        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonContainer.setBackground(UITheme.WHITE);
        buttonContainer.add(buttonPanel);
        contentPanel.add(buttonContainer);

        // Añadir paneles al contenedor principal
        add(titlePanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    /**
     * Configura un campo del formulario
     */
    private void setupFormField(JPanel panel, GridBagConstraints gbc, String labelText, JTextField field, int row) {
        // Etiqueta
        JLabel label = new JLabel(labelText);
        UITheme.applyFormLabelStyle(label);
        gbc.gridx = 0;
        gbc.gridy = row * 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0.2;
        panel.add(label, gbc);

        // Campo
        if (field instanceof JPasswordField) {
            UITheme.applyPasswordFieldStyle((JPasswordField) field);
        } else {
            UITheme.applyTextFieldStyle(field);
        }
        gbc.gridx = 0;
        gbc.gridy = row * 2 + 1;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
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
            showErrorDialog("Por favor, complete todos los campos");
            return;
        }

        // Crear objeto usuario
        Usuario nuevoUsuario = new Usuario(nickname, password, nombre, apellidos, telefono, correo);

        // Verificar si ya existe el usuario
        if (!checker.check(nuevoUsuario)) {
            showErrorDialog("El nickname o correo ya están en uso");
            return;
        }

        // Intentar añadir el usuario a la base de datos
        boolean registroExitoso = adder.add(nuevoUsuario);

        if (registroExitoso) {
            // Usar mensaje personalizado estilizado
            JOptionPane optionPane = new JOptionPane(
                    "¡Registro exitoso!\nAhora puede iniciar sesión",
                    JOptionPane.INFORMATION_MESSAGE);
            JDialog dialog = optionPane.createDialog("Registro completado");
            dialog.setIconImage(new ImageIcon(getClass().getResource("/img/LogoBMP.png")).getImage());
            dialog.setVisible(true);

            // Navegar al panel de login
            IPanelSwitcher.openPanel(new LoginIPanel(IPanelSwitcher));
        } else {
            showErrorDialog("Ocurrió un error durante el registro");
        }
    }

    /**
     * Muestra un diálogo de error estilizado
     */
    private void showErrorDialog(String message) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE);
        JDialog dialog = optionPane.createDialog("Error de registro");
        dialog.setIconImage(new ImageIcon(getClass().getResource("/img/LogoBMP.png")).getImage());
        dialog.setVisible(true);
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