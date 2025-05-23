package visuals;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import interfaces.IPanelSwitcher;

/**
 * Panel para la selección de rol del usuario - Versión mejorada
 */
public class RoleSelectionPanel extends JPanel implements IPanelSwitcher {
    private IPanelSwitcher IPanelSwitcher;
    private String currentUserNickname;

    public RoleSelectionPanel(IPanelSwitcher IPanelSwitcher, String userNickname) {
        this.IPanelSwitcher = IPanelSwitcher;
        this.currentUserNickname = userNickname;
        setupPanel();
    }

    private void setupPanel() {
        // Configuración del panel principal
        setLayout(new BorderLayout());
        setBackground(UITheme.WHITE);
        setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        // Panel central con disposición vertical
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(UITheme.WHITE);

        // Título principal
        JLabel titleLabel = new JLabel("Selección de Rol");
        UITheme.applyTitleLabelStyle(titleLabel);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createVerticalStrut(15));

        // Subtítulo
        JLabel subtitleLabel = new JLabel("¡Hola " + currentUserNickname + "! ¿Qué deseas hacer hoy?");
        UITheme.applySubtitleLabelStyle(subtitleLabel);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(subtitleLabel);
        centerPanel.add(Box.createVerticalStrut(40));

        // Panel para los botones de rol
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 0, 20));
        buttonPanel.setBackground(UITheme.WHITE);
        buttonPanel.setMaximumSize(new Dimension(350, 120));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Botón de ir a comprar
        JButton goShoppingButton = new JButton("Ir a Comprar");
        goShoppingButton.setPreferredSize(new Dimension(350, 50));
        UITheme.applyPrimaryButtonStyle(goShoppingButton);
        goShoppingButton.addActionListener(e ->
                IPanelSwitcher.openPanel(new SupermarketSelectionPanel(IPanelSwitcher, currentUserNickname, "comprador"))
        );

        // Botón de solicitar compras
        JButton getNeedShoppingButton = new JButton("Necesito que me Compren");
        getNeedShoppingButton.setPreferredSize(new Dimension(350, 50));
        UITheme.applySecondaryButtonStyle(getNeedShoppingButton);
        getNeedShoppingButton.addActionListener(e ->
                IPanelSwitcher.openPanel(new SupermarketSelectionPanel(IPanelSwitcher, currentUserNickname, "solicitante"))
        );

        buttonPanel.add(goShoppingButton);
        buttonPanel.add(getNeedShoppingButton);
        centerPanel.add(buttonPanel);
        centerPanel.add(Box.createVerticalStrut(40));

        // Botón volver
        JButton backButton = new JButton("← Volver");
        backButton.setPreferredSize(new Dimension(120, 35));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        UITheme.applySecondaryButtonStyle(backButton);
        backButton.addActionListener(e ->
                IPanelSwitcher.openPanel(new WelcomeIPanel(IPanelSwitcher))
        );
        centerPanel.add(backButton);

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