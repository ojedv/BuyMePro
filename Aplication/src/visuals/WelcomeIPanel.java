package visuals;

import interfaces.IPanelSwitcher;
import javax.swing.ImageIcon;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel de bienvenida simplificado
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
        // Usar BorderLayout para organizar los elementos
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Panel central con título y descripción
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

        // Añadir componentes al panel central
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(descLabel);
        centerPanel.add(Box.createVerticalGlue());

        // Panel para botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));

        // Botón de inicio de sesión
        JButton loginButton = new JButton("Iniciar Sesión");
        loginButton.setPreferredSize(new Dimension(150, 40));
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cambiar al panel de login
                IPanelSwitcher.openPanel(new LoginIPanel(IPanelSwitcher));
            }
        });

        // Botón de registro
        JButton registerButton = new JButton("Registrarse");
        registerButton.setPreferredSize(new Dimension(150, 40));
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
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

        // Añadir paneles al contenedor principal
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }



    @Override
    public void closePanel(JPanel panel) {

    }

    @Override
    public void openPanel(JPanel panel) {

    }
}