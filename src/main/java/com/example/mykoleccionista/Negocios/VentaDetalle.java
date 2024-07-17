package com.example.mykoleccionista.Negocios;

public class VentaDetalle {
    private Integer idVenta;
    private Integer Cantidad;
    private String SKU;
    private String FCompra;

    public VentaDetalle(int idVenta, String SKU, String FCompra, int cantidad) {
        this.idVenta = idVenta;
        this.Cantidad = cantidad;
        this.SKU = SKU;
        this.FCompra = FCompra;
    }

    public Integer getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public Integer getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int cantidad) {
        Cantidad = cantidad;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public String getFCompra() {
        return FCompra;
    }

    public void setFCompra(String FCompra) {
        this.FCompra = FCompra;
    }

    //VentaDetalle(int idVenta, String SKU, String FCompra, int cantidad)
    @Override
    public String toString() {
        return String.format("%d %d %s %s", this.idVenta, this.Cantidad, this.FCompra, this.SKU);
    }
}
