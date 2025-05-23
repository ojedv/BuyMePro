package Objetos;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class Pedido {
    private int idPedido;
    private int idUsuarioSolicitante;
    private Integer idUsuarioComprador; // Puede ser null
    private int idSupermercado;
    private String estado; // PENDIENTE, ASIGNADO, COMPLETADO, CANCELADO
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaAsignacion;
    private LocalDateTime fechaCompletado;
    private double total;
    private List<PedidoProducto> productos;

    // Constructor para nuevo pedido
    public Pedido(int idUsuarioSolicitante, int idSupermercado) {
        this.idUsuarioSolicitante = idUsuarioSolicitante;
        this.idSupermercado = idSupermercado;
        this.estado = "PENDIENTE";
        this.fechaCreacion = LocalDateTime.now();
        this.productos = new ArrayList<>();
        this.total = 0.0;
    }

    // Constructor completo
    public Pedido(int idPedido, int idUsuarioSolicitante, Integer idUsuarioComprador,
                  int idSupermercado, String estado, double total) {
        this.idPedido = idPedido;
        this.idUsuarioSolicitante = idUsuarioSolicitante;
        this.idUsuarioComprador = idUsuarioComprador;
        this.idSupermercado = idSupermercado;
        this.estado = estado;
        this.total = total;
        this.productos = new ArrayList<>();
    }

    public void añadirProducto(PedidoProducto pedidoProducto) {
        productos.add(pedidoProducto);
        calcularTotal();
    }

    public void calcularTotal() {
        this.total = productos.stream()
                .mapToDouble(pp -> pp.getCantidad() * pp.getPrecioUnitario())
                .sum();
    }

    // Getters y Setters
    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdUsuarioSolicitante() {
        return idUsuarioSolicitante;
    }

    public void setIdUsuarioSolicitante(int idUsuarioSolicitante) {
        this.idUsuarioSolicitante = idUsuarioSolicitante;
    }

    public Integer getIdUsuarioComprador() {
        return idUsuarioComprador;
    }

    public void setIdUsuarioComprador(Integer idUsuarioComprador) {
        this.idUsuarioComprador = idUsuarioComprador;
        if (idUsuarioComprador != null && "PENDIENTE".equals(estado)) {
            this.estado = "ASIGNADO";
            this.fechaAsignacion = LocalDateTime.now();
        }
    }

    public int getIdSupermercado() {
        return idSupermercado;
    }

    public void setIdSupermercado(int idSupermercado) {
        this.idSupermercado = idSupermercado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
        if ("COMPLETADO".equals(estado)) {
            this.fechaCompletado = LocalDateTime.now();
        }
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<PedidoProducto> getProductos() {
        return productos;
    }

    public void setProductos(List<PedidoProducto> productos) {
        this.productos = productos;
        calcularTotal();
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public LocalDateTime getFechaCompletado() {
        return fechaCompletado;
    }

    public void setFechaCompletado(LocalDateTime fechaCompletado) {
        this.fechaCompletado = fechaCompletado;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "idPedido=" + idPedido +
                ", idUsuarioSolicitante=" + idUsuarioSolicitante +
                ", idUsuarioComprador=" + idUsuarioComprador +
                ", idSupermercado=" + idSupermercado +
                ", estado='" + estado + '\'' +
                ", total=" + total +
                ", productos=" + productos.size() +
                '}';
    }
}