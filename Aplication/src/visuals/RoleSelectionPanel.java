package visuals;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import interfaces.IPanelSwitcher;

/**
 * Panel para la selección de rol del usuario
 */
public class RoleSelectionPanel extends JPanel implements IPanelSwitcher {
    // Referencia al gestor de paneles
    private IPanelSwitcher IPanelSwitcher;

    // Almacenar el nickname del usuario actual
    private String currentUserNickname;

    /**
     * Constructor del panel de selección de rol
     * @param IPanelSwitcher Gestor para cambiar entre paneles
     * @param userNickname Nickname del usuario actual
     */
    public RoleSelectionPanel(IPanelSwitcher IPanelSwitcher, String userNickname) {
        this.IPanelSwitcher = IPanelSwitcher;
        this.currentUserNickname = userNickname;
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
        JLabel titleLabel = new JLabel("Selección de Rol");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);

        // Panel de información
        JPanel infoPanel = new JPanel();
        JLabel infoLabel = new JLabel("¿Qué deseas hacer hoy?");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        infoPanel.add(infoLabel);

        // Panel para los botones de rol
        JPanel roleButtonsPanel = new JPanel(new GridLayout(2, 1, 0, 20));
        roleButtonsPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        // Botón de ir a comprar
        JButton goShoppingButton = new JButton("Ir a comprar");
        goShoppingButton.setFont(new Font("Arial", Font.BOLD, 16));
        goShoppingButton.setPreferredSize(new Dimension(200, 60));
        goShoppingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Navegar al panel de selección de supermercados
                IPanelSwitcher.openPanel(new SupermarketSelectionPanel(IPanelSwitcher, currentUserNickname, "comprador"));
            }
        });

        // Botón de que te vayan a comprar
        JButton getNeedShoppingButton = new JButton("Necesito que me compren");
        getNeedShoppingButton.setFont(new Font("Arial", Font.BOLD, 16));
        getNeedShoppingButton.setPreferredSize(new Dimension(200, 60));
        getNeedShoppingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Aquí iría la lógica para el rol de solicitar compras
                // Por ahora, mostraremos un mensaje informativo
                JOptionPane.showMessageDialog(RoleSelectionPanel.this,
                        "Has seleccionado el rol de solicitar compras.\n" +
                                "Pronto un comprador verá tu solicitud.",
                        "Rol seleccionado",
                        JOptionPane.INFORMATION_MESSAGE);

                // También podríamos ir a un panel específico para crear la lista de compra
                // IPanelSwitcher.openPanel(new CreateShoppingListPanel(IPanelSwitcher, currentUserNickname));
            }
        });

        // Añadir botones al panel de roles
        roleButtonsPanel.add(goShoppingButton);
        roleButtonsPanel.add(getNeedShoppingButton);

        // Panel para el botón de volver
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Volver");
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Aquí deberíamos volver al panel anterior (probablemente un panel principal)
                // Por ahora, volveremos al panel de bienvenida
                IPanelSwitcher.openPanel(new WelcomeIPanel(IPanelSwitcher));
            }
        });
        backButtonPanel.add(backButton);

        // Añadir paneles al contenedor principal
        add(titlePanel, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.CENTER);
        add(roleButtonsPanel, BorderLayout.CENTER);
        add(backButtonPanel, BorderLayout.SOUTH);
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