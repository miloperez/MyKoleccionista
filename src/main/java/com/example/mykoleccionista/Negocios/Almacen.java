package com.example.mykoleccionista.Negocios;

public class Almacen {
    private String SKU;
    private Integer Cantidad;
    private Float PrecioCompra;
    private Float PrecioVenta;
    private Integer Disponibles;
    private String FCompra;

    public Almacen(String SKU, String FCompra, int cantidad, Float precioCompra, Float precioVenta, int disponibles) {
        this.SKU = SKU;
        this.Cantidad = cantidad;
        this.PrecioCompra = precioCompra;
        this.PrecioVenta = precioVenta;
        this.Disponibles = disponibles;
        this.FCompra = FCompra;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public Integer getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int cantidad) {
        Cantidad = cantidad;
    }

    public Float getPrecioCompra() {
        return PrecioCompra;
    }

    public void setPrecioCompra(Float precioCompra) {
        PrecioCompra = precioCompra;
    }

    public Float getPrecioVenta() {
        return PrecioVenta;
    }

    public void setPrecioVenta(Float precioVenta) {
        PrecioVenta = precioVenta;
    }

    public Integer getDisponibles() {
        return Disponibles;
    }

    public void setDisponibles(int disponibles) {
        Disponibles = disponibles;
    }

    public String getFCompra() {
        return FCompra;
    }

    public void setFCompra(String FCompra) {
        this.FCompra = FCompra;
    }

    //public Almacen(String SKU, String FCompra, int cantidad, Float precioCompra, Float precioVenta, int disponibles) {
    @Override
    public String toString(){
        return String.format("%s %s %d %f %f %d ", this.SKU, this.FCompra, this.Cantidad, this.PrecioCompra, this.PrecioVenta, this.Disponibles);
    }

    @Override
    public boolean equals(Object o) {
        if (o != null){
            return this.SKU.equals(((Almacen) o).getSKU()) && this.FCompra.equals(((Almacen) o).getFCompra());
        }else{
            return false;
        }
        /*if (o != null){
            return this.SKU.equals(((Almacen) o).getSKU()) && this.FCompra.equals(((Almacen) o).getFCompra()) && this.Cantidad.equals(((Almacen) o).getCantidad()) && this.PrecioCompra.equals(((Almacen) o).getPrecioCompra()) && this.PrecioVenta.equals(((Almacen) o).getPrecioVenta()) && this.Disponibles.equals(((Almacen) o).getDisponibles());
        }else{
            return false;
        }*/
    }
}
