package com.example.mykoleccionista.Negocios;

public class PreventaMaestro {
    private int idPreventa;
    private String Fecha;
    private int Productos;
    private Float Total;
    private int Estado;
    private int idCliente;

    public PreventaMaestro(int idPreventa, String fecha, int productos, Float total, int estado, int idCliente) {
        this.idPreventa = idPreventa;
        this.Fecha = fecha;
        this.Productos = productos;
        this.Total = total;
        this.Estado = estado;
        this.idCliente = idCliente;
    }

    public int getIdPreventa() {
        return idPreventa;
    }

    public void setIdPreventa(int idPreventa) {
        this.idPreventa = idPreventa;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public int getProductos() {
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

    public int getEstado() {
        return Estado;
    }

    public void setEstado(int estado) {
        Estado = estado;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    @Override
    public String toString() {
        return String.format("%d %s %d %f %d %d", this.idPreventa, this.Fecha, this.Productos, this.Total, this.Estado, this.idCliente);
    }
}
