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
import java.util.ArrayList;
import java.util.List;

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

        // Panel superior combinado (título + información)
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        // Título
        JLabel titleLabel = new JLabel("Selección de Supermercado");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Información
        String infoText = userRole.equals("comprador") ?
                "Selecciona el supermercado donde quieres realizar la compra:" :
                "Selecciona el supermercado para tu lista de compras:";
        JLabel infoLabel = new JLabel(infoText);
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(titleLabel);
        topPanel.add(Box.createVerticalStrut(15));
        topPanel.add(infoLabel);

        // Panel para los botones de supermercado
        JPanel supermarketButtonsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        supermarketButtonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Cargar supermercados desde la base de datos
        loadSupermarkets(supermarketButtonsPanel);

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
        add(topPanel, BorderLayout.NORTH);
        add(supermarketButtonsPanel, BorderLayout.CENTER);
        add(backButtonPanel, BorderLayout.SOUTH);
    }

    /**
     * Carga los supermercados desde la base de datos y crea los botones
     */
    private void loadSupermarkets(JPanel panel) {
        boolean loadedFromDatabase = false;

        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT id_supermercado, nombre FROM supermercado");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int idSupermercado = rs.getInt("id_supermercado");
                String nombreSupermercado = rs.getString("nombre");

                JButton marketButton = new JButton(nombreSupermercado);
                marketButton.setFont(new Font("Arial", Font.BOLD, 16));
                marketButton.setPreferredSize(new Dimension(150, 100));

                // Configurar el ActionListener para cada botón
                marketButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (userRole.equals("comprador")) {
                            // Para compradores, ir al panel de actualización de productos
                            IPanelSwitcher.openPanel(new UpdateProductsPanel(IPanelSwitcher, currentUserNickname, nombreSupermercado , idSupermercado));
                        } else {
                            // Para solicitantes, ir al panel de gestión de productos del supermercado
                            IPanelSwitcher.openPanel(new SupermarketProductsPanel(IPanelSwitcher, currentUserNickname, nombreSupermercado, idSupermercado));
                        }
                    }
                });

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
                int idSupermercado = i + 1; // IDs por defecto

                JButton marketButton = new JButton(supermarket);
                marketButton.setFont(new Font("Arial", Font.BOLD, 16));
                marketButton.setPreferredSize(new Dimension(150, 100));

                marketButton.addActionListener(event -> {
                    if (userRole.equals("comprador")) {
                        IPanelSwitcher.openPanel(new UpdateProductsPanel(IPanelSwitcher, currentUserNickname, supermarket, idSupermercado));
                    } else {
                        IPanelSwitcher.openPanel(new SupermarketProductsPanel(IPanelSwitcher, currentUserNickname, supermarket, idSupermercado));
                    }
                });

                panel.add(marketButton);
            }
        }

        // Forzar actualización del panel
        panel.revalidate();
        panel.repaint();
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