package visuals;

import GestionBBDD.ConexionBD;
import interfaces.IPanelSwitcher;
import Objetos.Producto;
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
 * Panel para mostrar productos de un supermercado específico
 */
public class SupermarketProductsPanel extends JPanel implements IPanelSwitcher {
    private IPanelSwitcher IPanelSwitcher;
    private String currentUserNickname;
    private String selectedSupermarket;
    private int supermarketId;
    private DefaultListModel<String> productListModel;
    private JList<String> productList;
    private List<Producto> productos; // Lista para almacenar los objetos Producto
    private JLabel totalProductsLabel;

    /**
     * Constructor del panel de productos del supermercado
     * @param IPanelSwitcher Gestor para cambiar entre paneles
     * @param userNickname Nickname del usuario actual
     * @param supermarket Nombre del supermercado seleccionado
     * @param supermarketId ID del supermercado
     */
    public SupermarketProductsPanel(IPanelSwitcher IPanelSwitcher, String userNickname, String supermarket, int supermarketId) {
        this.IPanelSwitcher = IPanelSwitcher;
        this.currentUserNickname = userNickname;
        this.selectedSupermarket = supermarket;
        this.supermarketId = supermarketId;
        this.productListModel = new DefaultListModel<>();
        this.productos = new ArrayList<>();
        setupPanel();
        loadProducts(); // Cargar productos al inicializar
    }

    /**
     * Configura los componentes del panel
     */
    private void setupPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Panel de título
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Productos de " + selectedSupermarket, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        totalProductsLabel = new JLabel("Cargando productos...", SwingConstants.CENTER);
        totalProductsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        totalProductsLabel.setForeground(Color.GRAY);
        totalProductsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(totalProductsLabel);

        add(titlePanel, BorderLayout.NORTH);

        // Panel central para mostrar productos
        productList = new JList<>(productListModel);
        productList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        productList.setFont(new Font("Arial", Font.PLAIN, 14));

        // Configurar el renderer para mostrar información más detallada
        productList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(productList);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Productos"));
        add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Botón para actualizar productos
        JButton refreshButton = new JButton("Actualizar Lista");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.setPreferredSize(new Dimension(150, 40));
        refreshButton.addActionListener(e -> loadProducts());

        // Botón para crear lista de compra (futuro)
        JButton createListButton = new JButton("Crear Lista de Compra");
        createListButton.setFont(new Font("Arial", Font.BOLD, 14));
        createListButton.setPreferredSize(new Dimension(180, 40));
        createListButton.addActionListener(e -> {
            int[] selectedIndices = productList.getSelectedIndices();
            if (selectedIndices.length == 0) {
                JOptionPane.showMessageDialog(this,
                        "Por favor, selecciona al menos un producto para crear la lista de compra.",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                StringBuilder selectedProducts = new StringBuilder("Productos seleccionados:\n");
                for (int index : selectedIndices) {
                    selectedProducts.append("- ").append(productListModel.getElementAt(index)).append("\n");
                }
                JOptionPane.showMessageDialog(this,
                        selectedProducts.toString() + "\n(Funcionalidad en desarrollo)",
                        "Lista de Compra",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Botón para volver
        JButton backButton = new JButton("Volver");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.addActionListener(e ->
                IPanelSwitcher.openPanel(new SupermarketSelectionPanel(IPanelSwitcher, currentUserNickname, "solicitante"))
        );

        buttonPanel.add(refreshButton);
        buttonPanel.add(createListButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Carga los productos desde la base de datos filtrados por ID del supermercado
     */
    private void loadProducts() {
        productListModel.clear();
        productos.clear();
        totalProductsLabel.setText("Cargando productos...");

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try (Connection connection = ConexionBD.getConnection();
                     PreparedStatement ps = connection.prepareStatement(
                             "SELECT id_producto, nombre, precio, peso FROM producto WHERE id_supermercado = ?")) {

                    ps.setInt(1, supermarketId);

                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            Producto producto = new Producto(
                                    rs.getString("nombre"),
                                    rs.getFloat("precio"),
                                    rs.getFloat("peso"),
                                    supermarketId
                            );
                            producto.setIdProducto(rs.getInt("id_producto"));
                            productos.add(producto);

                            // Formatear la información del producto para mostrar en la lista
                            String productInfo = String.format("%s - %.2f€ (%.2fkg)",
                                    producto.getNombre(),
                                    producto.getPrecio(),
                                    producto.getPeso());
                            productListModel.addElement(productInfo);
                        }
                    }
                } catch (SQLException e) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(SupermarketProductsPanel.this,
                                "Error al cargar productos: " + e.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    });
                }
                return null;
            }

            @Override
            protected void done() {
                SwingUtilities.invokeLater(() -> {
                    if (productListModel.isEmpty()) {
                        totalProductsLabel.setText("No hay productos disponibles en este supermercado");
                        productListModel.addElement("No hay productos disponibles");
                    } else {
                        totalProductsLabel.setText("Total de productos: " + productos.size());
                    }
                });
            }
        };

        worker.execute();
    }

    /**
     * Obtiene la lista de productos cargados
     * @return Lista de productos
     */
    public List<Producto> getProductos() {
        return productos;
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