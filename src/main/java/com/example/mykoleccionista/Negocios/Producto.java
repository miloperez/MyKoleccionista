package com.example.mykoleccionista.Negocios;

public class Producto {
    private String SKU;
    private String Nombre;
    private String Descripcion;
    private String Fotografia;
    private Float Calificacion;
    private String FDisponibilidad;
    private int TipoProducto;
    private int TipoEstado;

    public Producto(String SKU, String nombre, String descripcion, String fotografia, Float calificacion, String FDisponibilidad, Integer tipoProducto, Integer tipoEstado) {
        this.SKU = SKU;
        this.Nombre = nombre;
        this.Descripcion = descripcion;
        this.Fotografia = fotografia;
        this.Calificacion = calificacion;
        this.FDisponibilidad = FDisponibilidad;
        this.TipoProducto = tipoProducto;
        this.TipoEstado = tipoEstado;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getFotografia() {
        return Fotografia;
    }

    public void setFotografia(String fotografia) {
        Fotografia = fotografia;
    }

    public Float getCalificacion() {
        return Calificacion;
    }

    public void setCalificacion(Float calificacion) {
        Calificacion = calificacion;
    }

    public String getFDisponibilidad() {
        return FDisponibilidad;
    }

    public void setFDisponibilidad(String FDisponibilidad) {
        this.FDisponibilidad = FDisponibilidad;
    }

    public int getTipoProducto() {
        return TipoProducto;
    }

    public void setTipoProducto(int tipoProducto) {
        TipoProducto = tipoProducto;
    }

    public int getTipoEstado() {
        return TipoEstado;
    }

    public void setTipoEstado(int tipoEstado) {
        TipoEstado = tipoEstado;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s %s %f %s %d %d", this.SKU, this.Nombre, this.Descripcion, this.Fotografia, this.Calificacion, this.FDisponibilidad, this.TipoProducto, this.TipoEstado);
    }
}
