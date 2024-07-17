package com.example.mykoleccionista.Negocios;

public class PreventaAbono {
    private Float Cantidad;
    private Integer idPreventa;
    private String Fecha;

    public PreventaAbono(Integer idPreventa, String fecha, Float cantidad) {
        this.Cantidad = cantidad;
        this.idPreventa = idPreventa;
        this.Fecha = fecha;
    }

    public Float getCantidad() {
        return Cantidad;
    }

    public void setCantidad(Float cantidad) {
        Cantidad = cantidad;
    }

    public Integer getIdPreventa() {
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

    @Override
    public String toString() {
        return String.format("%d %f %s", this.idPreventa, this.Cantidad, this.Fecha);
    }
}
