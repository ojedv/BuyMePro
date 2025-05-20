package visuals;

import interfaces.IPanelSwitcher;
import javax.swing.ImageIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel de bienvenida estilizado
 */
public class WelcomeIPanel extends JPanel implements IPanelSwitcher {
    // Referencia al gestor de paneles
    private IPanelSwitcher IPanelSwitcher;

    /**
     * Constructor del panel de bienvenida
     * @param IPanelSwitcher Gestor para cambiar entre paneles
     */
    public WelcomeIPanel(IPanelSwitcher IPanelSwitcher) {
        this.IPanelSwitcher = IPanelSwitcher;
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

        // Panel central con disposición de caja vertical
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(UITheme.WHITE);

        // Añadir logo
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/img/LogoBMP.png"));
            // Redimensionar logo para que se vea bien
            Image img = originalIcon.getImage();
            Image resizedImg = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            ImageIcon logoIcon = new ImageIcon(resizedImg);

            JLabel logoLabel = new JLabel(logoIcon);
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            centerPanel.add(logoLabel);
            centerPanel.add(Box.createVerticalStrut(30));
        } catch (Exception e) {
            System.err.println("Error: No se pudo cargar el logo. " + e.getMessage());
        }

        // Título principal
        JLabel titleLabel = new JLabel("¡Bienvenido a BuyMe!");
        UITheme.applyTitleLabelStyle(titleLabel);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createVerticalStrut(15));

        // Subtítulo
        JLabel descLabel = new JLabel("Tu aplicación para gestionar compras");
        UITheme.applySubtitleLabelStyle(descLabel);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(descLabel);
        centerPanel.add(Box.createVerticalStrut(40));

        // Panel para botones con efecto de sombra
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 20, 0));
        buttonPanel.setBackground(UITheme.WHITE);
        buttonPanel.setMaximumSize(new Dimension(400, 50));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Botón de inicio de sesión
        JButton loginButton = new JButton("Iniciar Sesión");
        loginButton.setPreferredSize(new Dimension(180, 50));
        UITheme.applyPrimaryButtonStyle(loginButton);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cambiar al panel de login
                IPanelSwitcher.openPanel(new LoginIPanel(IPanelSwitcher));
            }
        });

        // Botón de registro
        JButton registerButton = new JButton("Registrarse");
        registerButton.setPreferredSize(new Dimension(180, 50));
        UITheme.applySecondaryButtonStyle(registerButton);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cambiar al panel de registro
                IPanelSwitcher.openPanel(new RegisterIPanel(IPanelSwitcher));
            }
        });

        // Añadir botones al panel
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        centerPanel.add(buttonPanel);

        // Versión de la aplicación
        JLabel versionLabel = new JLabel("v1.0");
        versionLabel.setFont(UITheme.SMALL_FONT);
        versionLabel.setForeground(Color.GRAY);
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(versionLabel);

        // Añadir panel central al contenedor principal
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