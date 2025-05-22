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
import java.util.List;

/**
 * Panel para mostrar y gestionar productos de un supermercado específico
 */
public class SupermarketProductsPanel extends JPanel implements IPanelSwitcher {
    private IPanelSwitcher IPanelSwitcher;
    private String currentUserNickname;
    private String selectedSupermarket;
    private int idSupermercado;

    // Componentes de la interfaz
    private DefaultListModel<String> productListModel;
    private JList<String> productList;
    private List<Producto> productos; // Lista para almacenar los productos completos
    private List<Producto> selectedProducts; // Lista de productos seleccionados

    // Componentes para añadir productos
    private JTextField nombreField;
    private JTextField precioField;

    // Para la base de datos
    private AddBBDD addBBDD;
    private ConexionBD conexionBD;

    public SupermarketProductsPanel(IPanelSwitcher IPanelSwitcher, String userNickname, String supermarket, int idSupermercado) {
        this.IPanelSwitcher = IPanelSwitcher;
        this.currentUserNickname = userNickname;
        this.selectedSupermarket = supermarket;
        this.idSupermercado = idSupermercado;
        this.productListModel = new DefaultListModel<>();
        this.productos = new ArrayList<>();
        this.selectedProducts = new ArrayList<>();

        // Inicializar conexión a base de datos
        this.conexionBD = new ConexionBD();
        this.addBBDD = new AddBBDD(conexionBD);

        setupPanel();
        loadProducts();
    }

    private void setupPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel superior con título
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Productos de " + selectedSupermarket, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Panel central dividido en dos partes
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));

        // Panel izquierdo - Lista de productos
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Productos Disponibles"));

        productList = new JList<>(productListModel);
        productList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(productList);
        scrollPane.setPreferredSize(new Dimension(300, 300));
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        // Botón para añadir productos seleccionados a la lista de compra
        JButton addToCartButton = new JButton("Añadir a Lista de Compra");
        addToCartButton.setFont(new Font("Arial", Font.BOLD, 14));
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSelectedProductsToCart();
            }
        });
        leftPanel.add(addToCartButton, BorderLayout.SOUTH);

        centerPanel.add(leftPanel);

        // Panel derecho - Añadir nuevo producto
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Añadir Nuevo Producto"));

        // Formulario para añadir producto
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Campo nombre
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        nombreField = new JTextField(15);
        formPanel.add(nombreField, gbc);

        // Campo precio
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Precio:"), gbc);
        gbc.gridx = 1;
        precioField = new JTextField(15);
        formPanel.add(precioField, gbc);

        rightPanel.add(formPanel, BorderLayout.CENTER);

        // Botón para añadir producto
        JButton addProductButton = new JButton("Añadir Producto");
        addProductButton.setFont(new Font("Arial", Font.BOLD, 14));
        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewProduct();
            }
        });
        rightPanel.add(addProductButton, BorderLayout.SOUTH);

        centerPanel.add(rightPanel);
        add(centerPanel, BorderLayout.CENTER);

        // Panel inferior con botones de navegación
        JPanel bottomPanel = new JPanel(new FlowLayout());

        JButton viewCartButton = new JButton("Ver Lista de Compra (" + selectedProducts.size() + ")");
        viewCartButton.setFont(new Font("Arial", Font.BOLD, 14));
        viewCartButton.addActionListener(e -> showShoppingList());

        JButton backButton = new JButton("Volver");
        backButton.addActionListener(e ->
                IPanelSwitcher.openPanel(new SupermarketSelectionPanel(IPanelSwitcher, currentUserNickname, "solicitante"))
        );

        bottomPanel.add(viewCartButton);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Carga los productos del supermercado desde la base de datos
     */
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

                Producto producto = new Producto();
                producto.setIdProducto(idProducto);
                producto.setIdSupermercado(idSupermercado);

                // Usar reflection o añadir setters faltantes en la clase Producto
                // Por ahora, creamos un nuevo producto con constructor
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

    /**
     * Añade un nuevo producto a la base de datos
     */
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

            // Crear nuevo producto
            Producto nuevoProducto = new Producto(nombre, precio, 0, idSupermercado);

            // Añadir a la base de datos
            boolean success = addBBDD.add(nuevoProducto);

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Producto añadido correctamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);

                // Limpiar campos
                nombreField.setText("");
                precioField.setText("");

                // Recargar la lista de productos
                loadProducts();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al añadir el producto",
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

    /**
     * Añade los productos seleccionados a la lista de compra
     */
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
                if (!selectedProducts.contains(producto)) {
                    selectedProducts.add(producto);

                    // Aquí podrías añadir el producto a la tabla producto_supermercado
                    // si es necesario para tu lógica de negocio
                    addProductToSupermercadoTable(producto);
                }
            }
        }

        JOptionPane.showMessageDialog(this,
                "Productos añadidos a la lista de compra",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);

        // Actualizar el contador en el botón
        updateCartButton();
    }

    /**
     * Añade el producto a la tabla producto_supermercado
     */
    private void addProductToSupermercadoTable(Producto producto) {
        String sql = "INSERT IGNORE INTO producto_supermercado (id_supermercado, id_producto) VALUES (?, ?)";

        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, idSupermercado);
            ps.setInt(2, producto.getIdProducto());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al añadir producto a tabla producto_supermercado: " + e.getMessage());
        }
    }

    /**
     * Actualiza el botón del carrito con el número de productos
     */
    private void updateCartButton() {
        Component[] components = ((JPanel) getComponent(2)).getComponents(); // bottomPanel
        for (Component comp : components) {
            if (comp instanceof JButton && ((JButton) comp).getText().contains("Ver Lista")) {
                ((JButton) comp).setText("Ver Lista de Compra (" + selectedProducts.size() + ")");
                break;
            }
        }
    }

    /**
     * Muestra la lista de compra
     */
    private void showShoppingList() {
        if (selectedProducts.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay productos en la lista de compra",
                    "Lista vacía",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Lista de Compra:\n\n");
        float total = 0;

        for (Producto producto : selectedProducts) {
            sb.append("• ").append(producto.getNombre())
                    .append(" - €").append(String.format("%.2f", producto.getPrecio()))
                    .append("\n");
            total += producto.getPrecio();
        }

        sb.append("\nTotal: €").append(String.format("%.2f", total));

        JOptionPane.showMessageDialog(this,
                sb.toString(),
                "Lista de Compra",
                JOptionPane.INFORMATION_MESSAGE);
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