package com.example.mykoleccionista.Negocios;

public class VentaMaestro {
    private Integer idVenta;
    private Integer idCliente;
    private String Fecha;
    private int Productos;
    private Float Total;

    public VentaMaestro(int idVenta, int idCliente, String fecha, int productos, Float total) {
        this.idVenta = idVenta;
        this.Fecha = fecha;
        this.Productos = productos;
        this.Total = total;
        this.idCliente = idCliente;
    }

    public Integer getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public Integer getProductos() {
        return Productos;
    }

    public void setProductos(int productos) {
        Productos = productos;
    }

    public Float getTotal() {
        return Total;
    }

    public void setTotal(Float total) {
        Total = total;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    @Override
    public String toString() {
        return String.format("%d %d %s %d %f", this.idVenta, this.idCliente, this.Fecha, this.Productos, this.Total);
    }
}
