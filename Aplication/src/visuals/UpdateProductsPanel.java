package visuals;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import interfaces.IPanelSwitcher;

/**
 * Panel para actualizar los productos seleccionados
 */
public class UpdateProductsPanel extends JPanel implements IPanelSwitcher {
    // Referencia al gestor de paneles
    private IPanelSwitcher IPanelSwitcher;

    // Datos de la compra
    private String currentUserNickname;
    private String selectedSupermarket;

    /**
     * Constructor del panel de actualización de productos
     * @param IPanelSwitcher Gestor para cambiar entre paneles
     * @param userNickname Nickname del usuario actual
     * @param supermarket Supermercado seleccionado
     */
    public UpdateProductsPanel(IPanelSwitcher IPanelSwitcher, String userNickname, String supermarket) {
        this.IPanelSwitcher = IPanelSwitcher;
        this.currentUserNickname = userNickname;
        this.selectedSupermarket = supermarket;
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
        JLabel titleLabel = new JLabel("Actualizar Productos");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);

        // Panel de información
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JLabel supermarketLabel = new JLabel("Supermercado seleccionado: " + selectedSupermarket);
        supermarketLabel.setFont(new Font("Arial", Font.BOLD, 16));
        supermarketLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel instructionLabel = new JLabel("Presiona el botón para actualizar los productos solicitados por otros usuarios.");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(supermarketLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(instructionLabel);
        infoPanel.add(Box.createVerticalStrut(20));

        // Panel central para el botón de actualizar
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton updateButton = new JButton("Actualizar Productos");
        updateButton.setFont(new Font("Arial", Font.BOLD, 16));
        updateButton.setPreferredSize(new Dimension(200, 60));
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Aquí iría la lógica para actualizar los productos
                // Por ahora, mostramos un mensaje informativo
                JOptionPane.showMessageDialog(UpdateProductsPanel.this,
                        "Los productos han sido actualizados correctamente.",
                        "Actualización exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        centerPanel.add(updateButton);

        // Panel para el botón de volver
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Volver");
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Volver al panel de selección de supermercado
                IPanelSwitcher.openPanel(new SupermarketSelectionPanel(IPanelSwitcher, currentUserNickname, "comprador"));
            }
        });
        backButtonPanel.add(backButton);

        // Añadir paneles al contenedor principal
        add(titlePanel, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
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