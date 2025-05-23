package visuals;

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
 * Panel para actualizar productos - Rol Comprador
 * Muestra productos pendientes de comprar de otros usuarios
 */
public class UpdateProductsPanel extends JPanel implements IPanelSwitcher {
    private IPanelSwitcher IPanelSwitcher;
    private String currentUserNickname;
    private String selectedSupermarket;
    private int idSupermercado;

    // Componentes de la interfaz
    private DefaultListModel<String> pendingOrdersModel;
    private JList<String> pendingOrdersList;
    private List<PedidoInfo> pedidosInfo; // Lista para almacenar información completa de pedidos

    // Clase interna para manejar información de pedidos
    private static class PedidoInfo {
        int idPedido;
        int idUsuarioSolicitante;
        String nicknameUsuario;
        String nombreProducto;
        float precio;
        int idProducto;
        String estado;

        PedidoInfo(int idPedido, int idUsuarioSolicitante, String nicknameUsuario,
                   String nombreProducto, float precio, int idProducto, String estado) {
            this.idPedido = idPedido;
            this.idUsuarioSolicitante = idUsuarioSolicitante;
            this.nicknameUsuario = nicknameUsuario;
            this.nombreProducto = nombreProducto;
            this.precio = precio;
            this.idProducto = idProducto;
            this.estado = estado;
        }
    }

    public UpdateProductsPanel(IPanelSwitcher IPanelSwitcher, String userNickname,
                               String supermarket, int idSupermercado) {
        this.IPanelSwitcher = IPanelSwitcher;
        this.currentUserNickname = userNickname;
        this.selectedSupermarket = supermarket;
        this.idSupermercado = idSupermercado;
        this.pendingOrdersModel = new DefaultListModel<>();
        this.pedidosInfo = new ArrayList<>();

        setupPanel();
        loadPendingOrders();
    }

    private void setupPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel superior con título
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Pedidos Pendientes - " + selectedSupermarket, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Panel central - Lista de pedidos pendientes
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Productos solicitados por otros usuarios"));

        pendingOrdersList = new JList<>(pendingOrdersModel);
        pendingOrdersList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        pendingOrdersList.setFont(new Font("Arial", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(pendingOrdersList);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel de botones para gestionar pedidos
        JPanel actionPanel = new JPanel(new FlowLayout());

        JButton markAsBoughtButton = new JButton("Marcar como Comprado");
        markAsBoughtButton.setFont(new Font("Arial", Font.BOLD, 14));
        markAsBoughtButton.setBackground(new Color(76, 175, 80));
        markAsBoughtButton.setForeground(Color.WHITE);
        markAsBoughtButton.addActionListener(e -> markAsCompleted());

        JButton viewDetailsButton = new JButton("Ver Detalles");
        viewDetailsButton.setFont(new Font("Arial", Font.BOLD, 14));
        viewDetailsButton.addActionListener(e -> showOrderDetails());

        JButton refreshButton = new JButton("Actualizar Lista");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.addActionListener(e -> {
            loadPendingOrders();
            JOptionPane.showMessageDialog(this, "Lista actualizada", "Información",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        actionPanel.add(markAsBoughtButton);
        actionPanel.add(viewDetailsButton);
        actionPanel.add(refreshButton);

        centerPanel.add(actionPanel, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);

        // Panel inferior con estadísticas y navegación
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Panel de estadísticas
        JPanel statsPanel = new JPanel(new FlowLayout());
        JLabel statsLabel = new JLabel("Total pedidos pendientes: " + pedidosInfo.size());
        statsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statsPanel.add(statsLabel);

        // Panel de navegación
        JPanel navPanel = new JPanel(new FlowLayout());
        JButton backButton = new JButton("Volver");
        backButton.addActionListener(e ->
                IPanelSwitcher.openPanel(new SupermarketSelectionPanel(IPanelSwitcher, currentUserNickname, "comprador"))
        );
        navPanel.add(backButton);

        bottomPanel.add(statsPanel, BorderLayout.WEST);
        bottomPanel.add(navPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Carga los pedidos pendientes desde la base de datos
     */
    private void loadPendingOrders() {
        pendingOrdersModel.clear();
        pedidosInfo.clear();

        String sql = """
            SELECT DISTINCT 
                p.id_pedido,
                p.id_usuario_solicitante,
                u.nickname,
                pr.nombre as nombre_producto,
                pr.precio,
                pr.id_producto,
                p.estado
            FROM pedido p
            JOIN usuario u ON p.id_usuario_solicitante = u.id_usuario
            JOIN pedido_producto pp ON p.id_pedido = pp.id_pedido
            JOIN producto pr ON pp.id_producto = pr.id_producto
            WHERE p.id_supermercado = ? 
            AND p.estado IN ('PENDIENTE', 'ASIGNADO')
            ORDER BY p.id_pedido, pr.nombre
            """;

        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, idSupermercado);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int idPedido = rs.getInt("id_pedido");
                int idUsuarioSolicitante = rs.getInt("id_usuario_solicitante");
                String nickname = rs.getString("nickname");
                String nombreProducto = rs.getString("nombre_producto");
                float precio = rs.getFloat("precio");
                int idProducto = rs.getInt("id_producto");
                String estado = rs.getString("estado");

                PedidoInfo pedidoInfo = new PedidoInfo(idPedido, idUsuarioSolicitante, nickname,
                        nombreProducto, precio, idProducto, estado);
                pedidosInfo.add(pedidoInfo);

                String displayText = String.format("Pedido #%d - %s solicitó: %s (€%.2f) - Estado: %s",
                        idPedido, nickname, nombreProducto, precio, estado);
                pendingOrdersModel.addElement(displayText);
            }

            if (pedidosInfo.isEmpty()) {
                pendingOrdersModel.addElement("No hay pedidos pendientes para este supermercado");
            }

            // Actualizar estadísticas
            updateStats();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar pedidos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Marca los pedidos seleccionados como completados
     */
    private void markAsCompleted() {
        int[] selectedIndices = pendingOrdersList.getSelectedIndices();

        if (selectedIndices.length == 0) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione al menos un pedido",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de marcar como comprados los productos seleccionados?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection connection = ConexionBD.getConnection()) {
                connection.setAutoCommit(false);

                // Obtener el ID del usuario comprador actual
                int compradorId = getCurrentUserId(connection);

                String updateSql = """
                    UPDATE pedido 
                    SET estado = 'COMPLETADO', 
                        id_usuario_comprador = ?, 
                        fecha_completado = CURRENT_TIMESTAMP 
                    WHERE id_pedido = ?
                    """;

                try (PreparedStatement ps = connection.prepareStatement(updateSql)) {
                    for (int index : selectedIndices) {
                        if (index < pedidosInfo.size()) {
                            PedidoInfo pedido = pedidosInfo.get(index);
                            ps.setInt(1, compradorId);
                            ps.setInt(2, pedido.idPedido);
                            ps.addBatch();
                        }
                    }

                    ps.executeBatch();
                    connection.commit();

                    JOptionPane.showMessageDialog(this,
                            "Pedidos marcados como completados",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Recargar la lista
                    loadPendingOrders();

                } catch (SQLException e) {
                    connection.rollback();
                    throw e;
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                        "Error al actualizar pedidos: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    /**
     * Obtiene el ID del usuario actual
     */
    private int getCurrentUserId(Connection connection) throws SQLException {
        String sql = "SELECT id_usuario FROM usuario WHERE nickname = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, currentUserNickname);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_usuario");
            }
        }
        throw new SQLException("Usuario no encontrado: " + currentUserNickname);
    }

    /**
     * Muestra detalles del pedido seleccionado
     */
    private void showOrderDetails() {
        int selectedIndex = pendingOrdersList.getSelectedIndex();

        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione un pedido",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (selectedIndex < pedidosInfo.size()) {
            PedidoInfo pedido = pedidosInfo.get(selectedIndex);

            StringBuilder details = new StringBuilder();
            details.append("Detalles del Pedido #").append(pedido.idPedido).append("\n\n");
            details.append("Usuario Solicitante: ").append(pedido.nicknameUsuario).append("\n");
            details.append("Producto: ").append(pedido.nombreProducto).append("\n");
            details.append("Precio: €").append(String.format("%.2f", pedido.precio)).append("\n");
            details.append("Estado: ").append(pedido.estado).append("\n");
            details.append("Supermercado: ").append(selectedSupermarket);

            JOptionPane.showMessageDialog(this,
                    details.toString(),
                    "Detalles del Pedido",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Actualiza las estadísticas mostradas
     */
    private void updateStats() {
        Component[] components = ((JPanel) getComponent(2)).getComponents(); // bottomPanel
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                for (Component subComp : panel.getComponents()) {
                    if (subComp instanceof JLabel &&
                            ((JLabel) subComp).getText().contains("Total pedidos")) {
                        ((JLabel) subComp).setText("Total pedidos pendientes: " + pedidosInfo.size());
                        break;
                    }
                }
            }
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