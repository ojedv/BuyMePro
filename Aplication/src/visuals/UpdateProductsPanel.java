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
    private List<PedidoInfo> pedidosInfo;
    private JLabel statsLabel;

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
        UITheme.applyPanelStyle(this);

        // Panel superior con título estilizado
        JPanel titlePanel = UITheme.createTitlePanel("Pedidos Pendientes");

        // Subtítulo con información del supermercado
        JLabel subtitleLabel = new JLabel("Supermercado: " + selectedSupermarket);
        UITheme.applySubtitleLabelStyle(subtitleLabel);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(UITheme.WHITE);
        headerPanel.add(titlePanel);
        headerPanel.add(subtitleLabel);
        headerPanel.add(Box.createVerticalStrut(10));

        add(headerPanel, BorderLayout.NORTH);

        // Panel central estilizado
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(UITheme.WHITE);
        centerPanel.setBorder(UITheme.RED_TITLED_BORDER("Productos solicitados por otros usuarios"));

        // Lista de pedidos con estilo mejorado
        pendingOrdersList = new JList<>(pendingOrdersModel);
        pendingOrdersList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        UITheme.applyListStyle(pendingOrdersList);

        // Personalizar el renderer de la lista para mejor presentación
        pendingOrdersList.setCellRenderer(new PendingOrderCellRenderer());

        JScrollPane scrollPane = new JScrollPane(pendingOrdersList);
        scrollPane.setPreferredSize(new Dimension(700, 350));
        scrollPane.setBorder(BorderFactory.createLineBorder(UITheme.LIGHT_GRAY));
        scrollPane.getViewport().setBackground(UITheme.WHITE);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel de botones estilizado
        JPanel actionPanel = createActionPanel();
        centerPanel.add(actionPanel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        // Panel inferior con estadísticas y navegación
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Crea el panel de acciones con botones estilizados
     */
    private JPanel createActionPanel() {
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        actionPanel.setBackground(UITheme.WHITE);

        // Botón marcar como comprado (acción principal)
        JButton markAsBoughtButton = new JButton("✓ Marcar como Comprado");
        UITheme.applyPrimaryButtonStyle(markAsBoughtButton);
        markAsBoughtButton.setPreferredSize(new Dimension(200, 40));
        markAsBoughtButton.addActionListener(e -> markAsCompleted());

        // Botón ver detalles (acción secundaria)
        JButton viewDetailsButton = new JButton("👁 Ver Detalles");
        UITheme.applySecondaryButtonStyle(viewDetailsButton);
        viewDetailsButton.setPreferredSize(new Dimension(150, 40));
        viewDetailsButton.addActionListener(e -> showOrderDetails());

        // Botón actualizar (acción secundaria)
        JButton refreshButton = new JButton("🔄 Actualizar");
        UITheme.applySecondaryButtonStyle(refreshButton);
        refreshButton.setPreferredSize(new Dimension(120, 40));
        refreshButton.addActionListener(e -> {
            loadPendingOrders();
            JOptionPane.showMessageDialog(this, "Lista actualizada correctamente",
                    "Información", JOptionPane.INFORMATION_MESSAGE);
        });

        actionPanel.add(markAsBoughtButton);
        actionPanel.add(viewDetailsButton);
        actionPanel.add(refreshButton);

        return actionPanel;
    }

    /**
     * Crea el panel inferior con estadísticas y navegación
     */
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(UITheme.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        // Panel de estadísticas
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statsPanel.setBackground(UITheme.WHITE);

        statsLabel = new JLabel("Total pedidos pendientes: 0");
        UITheme.applyFormLabelStyle(statsLabel);
        statsLabel.setForeground(UITheme.PRIMARY_RED);
        statsPanel.add(statsLabel);

        // Panel de navegación
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        navPanel.setBackground(UITheme.WHITE);

        JButton backButton = new JButton("← Volver");
        UITheme.applySecondaryButtonStyle(backButton);
        backButton.setPreferredSize(new Dimension(100, 35));
        backButton.addActionListener(e ->
                IPanelSwitcher.openPanel(new SupermarketSelectionPanel(IPanelSwitcher, currentUserNickname, "comprador"))
        );
        navPanel.add(backButton);

        bottomPanel.add(statsPanel, BorderLayout.WEST);
        bottomPanel.add(navPanel, BorderLayout.EAST);

        return bottomPanel;
    }

    /**
     * Renderer personalizado para los elementos de la lista
     */
    private class PendingOrderCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {

            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            label.setFont(UITheme.REGULAR_FONT);
            label.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

            if (isSelected) {
                label.setBackground(UITheme.LIGHT_RED);
                label.setForeground(UITheme.TEXT_DARK);
            } else {
                label.setBackground(UITheme.WHITE);
                label.setForeground(UITheme.TEXT_DARK);
            }

            // Añadir icono según el estado
            if (index < pedidosInfo.size()) {
                PedidoInfo pedido = pedidosInfo.get(index);
                String icon = pedido.estado.equals("PENDIENTE") ? "⏳" : "📋";
                label.setText(icon + " " + value.toString());
            }

            return label;
        }
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
            showStyledErrorDialog("Error al cargar pedidos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Marca los pedidos seleccionados como completados
     */
    private void markAsCompleted() {
        int[] selectedIndices = pendingOrdersList.getSelectedIndices();

        if (selectedIndices.length == 0) {
            showStyledInfoDialog("Por favor, seleccione al menos un pedido");
            return;
        }

        int confirm = showStyledConfirmDialog(
                "¿Está seguro de marcar como comprados los productos seleccionados?");

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection connection = ConexionBD.getConnection()) {
                connection.setAutoCommit(false);

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

                    showStyledSuccessDialog("Pedidos marcados como completados exitosamente");
                    loadPendingOrders();

                } catch (SQLException e) {
                    connection.rollback();
                    throw e;
                }

            } catch (SQLException e) {
                showStyledErrorDialog("Error al actualizar pedidos: " + e.getMessage());
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
            showStyledInfoDialog("Por favor, seleccione un pedido");
            return;
        }

        if (selectedIndex < pedidosInfo.size()) {
            PedidoInfo pedido = pedidosInfo.get(selectedIndex);

            // Crear un panel personalizado para mostrar los detalles
            JPanel detailsPanel = new JPanel();
            detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
            detailsPanel.setBackground(UITheme.WHITE);
            detailsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            // Título del pedido
            JLabel titleLabel = new JLabel("Detalles del Pedido #" + pedido.idPedido);
            UITheme.applySubtitleLabelStyle(titleLabel);
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            detailsPanel.add(titleLabel);
            detailsPanel.add(Box.createVerticalStrut(15));

            // Información del pedido
            String[] details = {
                    "Usuario Solicitante: " + pedido.nicknameUsuario,
                    "Producto: " + pedido.nombreProducto,
                    "Precio: €" + String.format("%.2f", pedido.precio),
                    "Estado: " + pedido.estado,
                    "Supermercado: " + selectedSupermarket
            };

            for (String detail : details) {
                JLabel detailLabel = new JLabel(detail);
                UITheme.applyFormLabelStyle(detailLabel);
                detailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                detailsPanel.add(detailLabel);
                detailsPanel.add(Box.createVerticalStrut(5));
            }

            JOptionPane.showMessageDialog(this, detailsPanel, "Detalles del Pedido",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Actualiza las estadísticas mostradas
     */
    private void updateStats() {
        if (statsLabel != null) {
            statsLabel.setText("Total pedidos pendientes: " + pedidosInfo.size());
        }
    }

    // Métodos para diálogos estilizados
    private void showStyledInfoDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showStyledErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showStyledSuccessDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private int showStyledConfirmDialog(String message) {
        return JOptionPane.showConfirmDialog(this, message, "Confirmar", JOptionPane.YES_NO_OPTION);
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