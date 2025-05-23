package visuals;

import GestionBBDD.ConexionBD;
import interfaces.IPanelSwitcher;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateProductsPanel extends JPanel implements IPanelSwitcher {
    private IPanelSwitcher IPanelSwitcher;
    private String currentUserNickname;
    private String selectedSupermarket;
    private DefaultListModel<String> productListModel; // Modelo para la lista de productos
    private JList<String> productList; // Componente visual para mostrar productos
    private int idSupermarket;

    public UpdateProductsPanel(IPanelSwitcher IPanelSwitcher, String userNickname, String supermarket, int idSupermarket) {
        this.IPanelSwitcher = IPanelSwitcher;
        this.currentUserNickname = userNickname;
        this.selectedSupermarket = supermarket;
        this.productListModel = new DefaultListModel<>();
        this.idSupermarket = idSupermarket;
        setupPanel();
    }

    private void setupPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Título
        JLabel titleLabel = new JLabel("Actualizar Productos", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Panel central para mostrar productos
        productList = new JList<>(productListModel);
        productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(productList);
        add(scrollPane, BorderLayout.CENTER);

        // Botón para actualizar productos
        JButton updateButton = new JButton("Actualizar Productos");
        updateButton.setFont(new Font("Arial", Font.BOLD, 16));
        updateButton.setPreferredSize(new Dimension(200, 50));
        updateButton.addActionListener(e -> loadProducts()); // Simplemente llamamos a la función

        // Botón para volver
        JButton backButton = new JButton("Volver");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.addActionListener(e ->
                IPanelSwitcher.openPanel(new SupermarketSelectionPanel(IPanelSwitcher, currentUserNickname, "comprador"))
        );

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(updateButton);
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Carga los productos desde la base de datos y actualiza el modelo de la lista.
     */
    private void loadProducts() {
        productListModel.clear(); // Limpia la lista actual en el modelo

        String query = """
        SELECT p.nombre, p.precio, u.nickname 
        FROM producto p
        INNER JOIN producto_supermercado ps ON p.id_producto = ps.id_producto
        INNER JOIN usuario u ON ps.id_usuario = u.id_usuario
        WHERE ps.id_supermercado = ?;
    """;

        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, idSupermarket); // Usa el idSupermarket para filtrar

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String nombre = rs.getString("nombre");
                    float precio = rs.getFloat("precio");
                    String nickname = rs.getString("nickname");

                    // Formato: "Producto - Precio - Nickname"
                    String productoInfo = String.format("%s - %.2f€ - Añadido por: %s", nombre, precio, nickname);
                    productListModel.addElement(productoInfo); // Agrega cada producto al modelo
                }
            }

            if (productListModel.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay productos disponibles para este supermercado.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
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
