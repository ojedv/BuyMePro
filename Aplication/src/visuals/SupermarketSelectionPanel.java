package visuals;

import GestionBBDD.ConexionBD;
import interfaces.IPanelSwitcher;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Panel para la selección de supermercado - Versión mejorada
 */
public class SupermarketSelectionPanel extends JPanel implements IPanelSwitcher {
    private IPanelSwitcher IPanelSwitcher;
    private String currentUserNickname;
    private String userRole;

    public SupermarketSelectionPanel(IPanelSwitcher IPanelSwitcher, String userNickname, String role) {
        this.IPanelSwitcher = IPanelSwitcher;
        this.currentUserNickname = userNickname;
        this.userRole = role;
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
        JLabel titleLabel = new JLabel("Selección de Supermercado");
        UITheme.applyTitleLabelStyle(titleLabel);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createVerticalStrut(15));

        // Subtítulo dinámico según el rol
        String infoText = userRole.equals("comprador") ?
                "Selecciona el supermercado donde quieres realizar la compra:" :
                "Selecciona el supermercado para tu lista de compras:";
        JLabel subtitleLabel = new JLabel(infoText);
        UITheme.applySubtitleLabelStyle(subtitleLabel);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(subtitleLabel);
        centerPanel.add(Box.createVerticalStrut(40));

        // Panel para los botones de supermercado
        JPanel supermarketPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        supermarketPanel.setBackground(UITheme.WHITE);
        supermarketPanel.setMaximumSize(new Dimension(500, 200));
        supermarketPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Cargar supermercados
        loadSupermarkets(supermarketPanel);

        centerPanel.add(supermarketPanel);
        centerPanel.add(Box.createVerticalStrut(40));

        // Botón volver
        JButton backButton = new JButton("← Volver");
        backButton.setPreferredSize(new Dimension(120, 35));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        UITheme.applySecondaryButtonStyle(backButton);
        backButton.addActionListener(e ->
                IPanelSwitcher.openPanel(new RoleSelectionPanel(IPanelSwitcher, currentUserNickname))
        );
        centerPanel.add(backButton);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void loadSupermarkets(JPanel panel) {
        boolean loadedFromDatabase = false;

        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT id_supermercado, nombre FROM supermercado");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int idSupermercado = rs.getInt("id_supermercado");
                String nombreSupermercado = rs.getString("nombre");

                JButton marketButton = createSupermarketButton(nombreSupermercado, idSupermercado);
                panel.add(marketButton);
                loadedFromDatabase = true;
            }

        } catch (SQLException e) {
            System.err.println("Error al cargar supermercados desde BD: " + e.getMessage());
        }

        // Si no se cargó nada de la base de datos, usar supermercados por defecto
        if (!loadedFromDatabase) {
            String[] defaultSupermarkets = {"Mercadona", "Lidl", "Dia", "Mas"};
            for (int i = 0; i < defaultSupermarkets.length; i++) {
                String supermarket = defaultSupermarkets[i];
                int idSupermercado = i + 1;
                JButton marketButton = createSupermarketButton(supermarket, idSupermercado);
                panel.add(marketButton);
            }
        }

        panel.revalidate();
        panel.repaint();
    }

    private JButton createSupermarketButton(String supermarketName, int idSupermercado) {
        JButton marketButton = new JButton("🏪 " + supermarketName);
        marketButton.setPreferredSize(new Dimension(200, 80));

        // Alternar estilos para variedad visual
        if (supermarketName.equals("Mercadona") || supermarketName.equals("Dia")) {
            UITheme.applyPrimaryButtonStyle(marketButton);
        } else {
            UITheme.applySecondaryButtonStyle(marketButton);
        }

        marketButton.addActionListener(e -> {
            if (userRole.equals("comprador")) {
                IPanelSwitcher.openPanel(new UpdateProductsPanel(IPanelSwitcher, currentUserNickname, supermarketName, idSupermercado));
            } else {
                IPanelSwitcher.openPanel(new SupermarketProductsPanel(IPanelSwitcher, currentUserNickname, supermarketName, idSupermercado));
            }
        });

        return marketButton;
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