package com.example.mykoleccionista.Negocios;

public class PreventaDetalle {
    private Integer Cantidad;
    private Float Costo;
    private Integer idPreventa;
    private String SKU;

    public PreventaDetalle( int idPreventa, String SKU, int cantidad, Float costo) {
        this.Cantidad = cantidad;
        this.Costo = costo;
        this.idPreventa = idPreventa;
        this.SKU = SKU;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int cantidad) {
        Cantidad = cantidad;
    }

    public Float getCosto() {
        return Costo;
    }

    public void setCosto(Float costo) {
        Costo = costo;
    }

    public int getIdPreventa() {
        return idPreventa;
    }

    public void setIdPreventa(int idPreventa) {
        this.idPreventa = idPreventa;
    }

    public String getSKU() {
        return this.SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    @Override
    public String toString() {
        return String.format("%d, %f, %d, %s", this.Cantidad, this.Costo, this.idPreventa, this.SKU);
    }
}
