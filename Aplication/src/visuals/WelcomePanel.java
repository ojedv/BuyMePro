package visuals;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel de bienvenida que ofrece opciones para iniciar sesión o registrarse
 */
public class WelcomePanel extends JPanel {

    private PanelManager panelManager;

    /**
     * Constructor del panel de bienvenida
     * @param panelManager Gestor de paneles para la navegación
     */
    public WelcomePanel(PanelManager panelManager) {
        this.panelManager = panelManager;
        initComponents();
    }

    /**
     * Inicializa los componentes de la interfaz
     */
    private void initComponents() {
        // Configurar el layout
        setLayout(new BorderLayout());

        // Panel central con título y logo
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Título
        JLabel titleLabel = new JLabel("¡Bienvenido a BuyMe!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Descripción
        JLabel descLabel = new JLabel("Tu aplicación para gestionar compras");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Espacio para un logo (simulado con un área vacía)
        JPanel logoPanel = new JPanel();
        logoPanel.setPreferredSize(new Dimension(200, 100));
        logoPanel.setBackground(new Color(220, 240, 255));
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Añadir componentes al panel central
        centerPanel.add(Box.createVerticalStrut(50));
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(descLabel);
        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(logoPanel);

        // Panel para botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        // Botón de inicio de sesión
        JButton loginButton = new JButton("Iniciar Sesión");
        loginButton.setPreferredSize(new Dimension(150, 40));
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.showPanel("login");
            }
        });

        // Botón de registro
        JButton registerButton = new JButton("Registrarse");
        registerButton.setPreferredSize(new Dimension(150, 40));
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.showPanel("register");
            }
        });

        // Añadir botones al panel
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        // Añadir paneles al contenedor principal
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Añadir padding
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }
}