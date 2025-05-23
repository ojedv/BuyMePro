package visuals;

import GestionBBDD.AddBBDD;
import GestionBBDD.ConexionBD;
import Objetos.Producto;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Panel para mostrar y gestionar productos de un supermercado específico - Versión mejorada
 */
public class SupermarketProductsPanel extends JPanel implements IPanelSwitcher {
    private IPanelSwitcher IPanelSwitcher;
    private String currentUserNickname;
    private String selectedSupermarket;
    private int idSupermercado;
    private int idUsuario;

    // Componentes de la interfaz
    private DefaultListModel<String> productListModel;
    private JList<String> productList;
    private List<Producto> productos;
    private Map<Producto, Integer> selectedProducts;

    // Componentes para añadir productos
    private JTextField nombreField;
    private JTextField precioField;

    // Para la base de datos
    private AddBBDD addBBDD;
    private ConexionBD conexionBD;

    // Componente para mostrar carrito
    private JButton cartButton;

    public SupermarketProductsPanel(IPanelSwitcher IPanelSwitcher, String userNickname, String supermarket, int idSupermercado) {
        this.IPanelSwitcher = IPanelSwitcher;
        this.currentUserNickname = userNickname;
        this.selectedSupermarket = supermarket;
        this.idSupermercado = idSupermercado;
        this.productListModel = new DefaultListModel<>();
        this.productos = new ArrayList<>();
        this.selectedProducts = new HashMap<>();

        this.idUsuario = getUserId(userNickname);
        this.conexionBD = new ConexionBD();
        this.addBBDD = new AddBBDD(conexionBD);

        setupPanel();
        loadProducts();
    }

    private int getUserId(String nickname) {
        String sql = "SELECT id_usuario FROM usuario WHERE nickname = ?";
        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nickname);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_usuario");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void setupPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(UITheme.WHITE);
        setBorder(UITheme.EMPTY_BORDER);

        // Panel superior con título
        JPanel titlePanel = UITheme.createTitlePanel("Productos de " + selectedSupermarket);
        add(titlePanel, BorderLayout.NORTH);

        // Panel central dividido
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setBackground(UITheme.WHITE);

        // Panel izquierdo - Lista de productos
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBackground(UITheme.WHITE);
        leftPanel.setBorder(UITheme.RED_TITLED_BORDER("Productos Disponibles"));

        productList = new JList<>(productListModel);
        productList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        UITheme.applyListStyle(productList);

        JScrollPane scrollPane = new JScrollPane(productList);
        scrollPane.setPreferredSize(new Dimension(300, 350));
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel de botones para productos
        JPanel productButtonsPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        productButtonsPanel.setBackground(UITheme.WHITE);

        JButton addToCartButton = new JButton("➕ Añadir al Carrito");
        UITheme.applyPrimaryButtonStyle(addToCartButton);
        addToCartButton.addActionListener(e -> addSelectedProductsToCart());

        JButton removeFromCartButton = new JButton("➖ Quitar del Carrito");
        UITheme.applySecondaryButtonStyle(removeFromCartButton);
        removeFromCartButton.addActionListener(e -> removeSelectedProductsFromCart());

        productButtonsPanel.add(addToCartButton);
        productButtonsPanel.add(removeFromCartButton);
        leftPanel.add(productButtonsPanel, BorderLayout.SOUTH);

        centerPanel.add(leftPanel);

        // Panel derecho - Añadir nuevo producto
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBackground(UITheme.WHITE);
        rightPanel.setBorder(UITheme.RED_TITLED_BORDER("Añadir Nuevo Producto"));

        // Formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UITheme.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Campo nombre
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Nombre:");
        UITheme.applyFormLabelStyle(nameLabel);
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        nombreField = new JTextField(15);
        UITheme.applyTextFieldStyle(nombreField);
        formPanel.add(nombreField, gbc);

        // Campo precio
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel priceLabel = new JLabel("Precio (€):");
        UITheme.applyFormLabelStyle(priceLabel);
        formPanel.add(priceLabel, gbc);

        gbc.gridx = 1;
        precioField = new JTextField(15);
        UITheme.applyTextFieldStyle(precioField);
        formPanel.add(precioField, gbc);

        rightPanel.add(formPanel, BorderLayout.CENTER);

        // Botón añadir producto
        JButton addProductButton = new JButton("Añadir Producto");
        UITheme.applyPrimaryButtonStyle(addProductButton);
        addProductButton.addActionListener(e -> addNewProduct());
        rightPanel.add(addProductButton, BorderLayout.SOUTH);

        centerPanel.add(rightPanel);
        add(centerPanel, BorderLayout.CENTER);

        // Panel inferior con botones principales
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bottomPanel.setBackground(UITheme.WHITE);

        cartButton = new JButton("🛒 Ver Carrito (0)");
        UITheme.applySecondaryButtonStyle(cartButton);
        cartButton.addActionListener(e -> showShoppingList());

        JButton orderButton = new JButton("ENCARGAR PEDIDO");
        orderButton.setPreferredSize(new Dimension(200, 40));
        orderButton.setBackground(new Color(0, 150, 0));
        orderButton.setForeground(Color.WHITE);
        orderButton.setFont(UITheme.BUTTON_FONT);
        orderButton.setFocusPainted(false);
        orderButton.setBorderPainted(false);
        orderButton.addActionListener(e -> createOrder());

        JButton backButton = new JButton("← Volver");
        UITheme.applySecondaryButtonStyle(backButton);
        backButton.addActionListener(e ->
                IPanelSwitcher.openPanel(new SupermarketSelectionPanel(IPanelSwitcher, currentUserNickname, "solicitante"))
        );

        bottomPanel.add(cartButton);
        bottomPanel.add(orderButton);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadProducts() {
        productListModel.clear();
        productos.clear();

        String sql = "SELECT id_producto, nombre, precio FROM producto WHERE id_supermercado = ?";

        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, idSupermercado);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int idProducto = rs.getInt("id_producto");
                String nombre = rs.getString("nombre");
                float precio = rs.getFloat("precio");

                Producto productoCompleto = new Producto(nombre, precio, 0, idSupermercado);
                productoCompleto.setIdProducto(idProducto);

                productos.add(productoCompleto);
                productListModel.addElement(nombre + " - €" + String.format("%.2f", precio));
            }

            if (productos.isEmpty()) {
                productListModel.addElement("No hay productos disponibles");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar productos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addNewProduct() {
        String nombre = nombreField.getText().trim();
        String precioText = precioField.getText().trim();

        if (nombre.isEmpty() || precioText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, complete todos los campos",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            float precio = Float.parseFloat(precioText);

            if (precio <= 0) {
                JOptionPane.showMessageDialog(this,
                        "El precio debe ser mayor que 0",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Producto nuevoProducto = new Producto(nombre, precio, 0, idSupermercado);
            boolean success = addBBDD.add(nuevoProducto);

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "✅ Producto añadido correctamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                nombreField.setText("");
                precioField.setText("");
                loadProducts();
            } else {
                JOptionPane.showMessageDialog(this,
                        "❌ Error al añadir el producto",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "El precio debe ser un número válido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addSelectedProductsToCart() {
        int[] selectedIndices = productList.getSelectedIndices();

        if (selectedIndices.length == 0) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione al menos un producto",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (int index : selectedIndices) {
            if (index < productos.size()) {
                Producto producto = productos.get(index);
                selectedProducts.put(producto, selectedProducts.getOrDefault(producto, 0) + 1);
            }
        }

        JOptionPane.showMessageDialog(this,
                "✅ Productos añadidos al carrito",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);

        updateCartButton();
    }

    private void removeSelectedProductsFromCart() {
        int[] selectedIndices = productList.getSelectedIndices();

        if (selectedIndices.length == 0) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione productos para quitar",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (int index : selectedIndices) {
            if (index < productos.size()) {
                Producto producto = productos.get(index);
                if (selectedProducts.containsKey(producto)) {
                    int cantidad = selectedProducts.get(producto);
                    if (cantidad > 1) {
                        selectedProducts.put(producto, cantidad - 1);
                    } else {
                        selectedProducts.remove(producto);
                    }
                }
            }
        }

        updateCartButton();
    }

    private void updateCartButton() {
        int totalItems = selectedProducts.values().stream().mapToInt(Integer::intValue).sum();
        cartButton.setText("🛒 Ver Carrito (" + totalItems + ")");
    }

    private void showShoppingList() {
        if (selectedProducts.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay productos en el carrito",
                    "Carrito vacío",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("CARRITO DE COMPRAS\n");
        sb.append("══════════════════════\n\n");
        double total = 0;

        for (Map.Entry<Producto, Integer> entry : selectedProducts.entrySet()) {
            Producto producto = entry.getKey();
            int cantidad = entry.getValue();
            double subtotal = cantidad * producto.getPrecio();

            sb.append("• ").append(producto.getNombre())
                    .append(" x").append(cantidad)
                    .append(" - €").append(String.format("%.2f", producto.getPrecio()))
                    .append(" c/u = €").append(String.format("%.2f", subtotal))
                    .append("\n");
            total += subtotal;
        }

        sb.append("\n══════════════════════");
        sb.append("\nTOTAL: €").append(String.format("%.2f", total));

        JOptionPane.showMessageDialog(this,
                sb.toString(),
                "Carrito de Compras",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void createOrder() {
        if (selectedProducts.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay productos en el carrito para encargar",
                    "Carrito vacío",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (idUsuario == -1) {
            JOptionPane.showMessageDialog(this,
                    "Error: No se pudo identificar al usuario",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = ConexionBD.getConnection()) {
            connection.setAutoCommit(false);

            // Calcular total
            double total = selectedProducts.entrySet().stream()
                    .mapToDouble(entry -> entry.getKey().getPrecio() * entry.getValue())
                    .sum();

            // Insertar pedido
            String sqlPedido = "INSERT INTO pedido (id_usuario_solicitante, id_supermercado, estado, total) VALUES (?, ?, 'PENDIENTE', ?)";
            PreparedStatement psPedido = connection.prepareStatement(sqlPedido, PreparedStatement.RETURN_GENERATED_KEYS);
            psPedido.setInt(1, idUsuario);
            psPedido.setInt(2, idSupermercado);
            psPedido.setDouble(3, total);
            psPedido.executeUpdate();

            // Obtener ID del pedido generado
            ResultSet rs = psPedido.getGeneratedKeys();
            if (!rs.next()) {
                throw new SQLException("No se pudo obtener el ID del pedido");
            }
            int idPedido = rs.getInt(1);

            // Insertar productos del pedido
            String sqlProducto = "INSERT INTO pedido_producto (id_pedido, id_producto, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
            PreparedStatement psProducto = connection.prepareStatement(sqlProducto);

            for (Map.Entry<Producto, Integer> entry : selectedProducts.entrySet()) {
                Producto producto = entry.getKey();
                int cantidad = entry.getValue();

                psProducto.setInt(1, idPedido);
                psProducto.setInt(2, producto.getIdProducto());
                psProducto.setInt(3, cantidad);
                psProducto.setDouble(4, producto.getPrecio());
                psProducto.executeUpdate();
            }

            connection.commit();

            JOptionPane.showMessageDialog(this,
                    "🎉 ¡PEDIDO CREADO EXITOSAMENTE!\n\n" +
                            "ID del pedido: " + idPedido + "\n" +
                            "Total: €" + String.format("%.2f", total) + "\n" +
                            "Estado: PENDIENTE\n\n" +
                            "Tu pedido estará disponible para que otros usuarios lo tomen.",
                    "Pedido Creado",
                    JOptionPane.INFORMATION_MESSAGE);

            // Limpiar carrito
            selectedProducts.clear();
            updateCartButton();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "❌ Error al crear el pedido: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
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