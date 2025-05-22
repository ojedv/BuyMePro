package visuals;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import interfaces.IPanelSwitcher;

/**
 * Panel para la selección de supermercado
 */
public class SupermarketSelectionPanel extends JPanel implements IPanelSwitcher {
    // Referencia al gestor de paneles
    private IPanelSwitcher IPanelSwitcher;

    // Datos del usuario
    private String currentUserNickname;
    private String userRole;

    /**
     * Constructor del panel de selección de supermercado
     * @param IPanelSwitcher Gestor para cambiar entre paneles
     * @param userNickname Nickname del usuario actual
     * @param role Rol seleccionado (comprador o solicitante)
     */
    public SupermarketSelectionPanel(IPanelSwitcher IPanelSwitcher, String userNickname, String role) {
        this.IPanelSwitcher = IPanelSwitcher;
        this.currentUserNickname = userNickname;
        this.userRole = role;
        setupPanel();
    }

    /**
     * Configura los componentes del panel
     */
    private void setupPanel() {
        // Usar BorderLayout como layout principal
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        // Panel de título
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Selección de Supermercado");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);

        // Panel de información con texto dinámico según el rol
        JPanel infoPanel = new JPanel();
        String infoText = userRole.equals("comprador") ?
                "Selecciona el supermercado donde quieres realizar la compra:" :
                "Selecciona el supermercado para crear tu lista de compra:";
        JLabel infoLabel = new JLabel(infoText);
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        infoPanel.add(infoLabel);

        // Panel para los botones de supermercado
        JPanel supermarketButtonsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        supermarketButtonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Crear los botones para cada supermercado con sus IDs correspondientes
        String[][] supermarkets = {
                {"Mercadona", "1"},
                {"Lidl", "2"},
                {"Dia", "3"},
                {"Mas", "4"}
        };

        for (String[] supermarket : supermarkets) {
            String name = supermarket[0];
            String id = supermarket[1];

            JButton marketButton = new JButton(name);
            marketButton.setFont(new Font("Arial", Font.BOLD, 16));
            marketButton.setPreferredSize(new Dimension(150, 100));

            // Configurar el ActionListener para cada botón
            marketButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (userRole.equals("comprador")) {
                        // Para compradores, ir al panel de actualización de productos
                        IPanelSwitcher.openPanel(new UpdateProductsPanel(IPanelSwitcher, currentUserNickname, name));
                    } else {
                        // Para solicitantes, ir al panel de productos del supermercado
                        IPanelSwitcher.openPanel(new SupermarketProductsPanel(IPanelSwitcher, currentUserNickname, name, Integer.parseInt(id)));
                    }
                }
            });

            supermarketButtonsPanel.add(marketButton);
        }

        // Panel para el botón de volver
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Volver");
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Volver al panel de selección de rol
                IPanelSwitcher.openPanel(new RoleSelectionPanel(IPanelSwitcher, currentUserNickname));
            }
        });
        backButtonPanel.add(backButton);

        // Añadir paneles al contenedor principal
        add(titlePanel, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.NORTH); // Añadido al NORTH junto con el título
        add(supermarketButtonsPanel, BorderLayout.CENTER);
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