package com.example.mykoleccionista.Negocios;

public class TipoProducto {
    private Integer id;
    private String Descripcion;

    public TipoProducto(Integer id, String descripcion) {
        this.id = id;
        this.Descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    @Override
    public String toString() {
        return String.format("%d %s", this.id, this.Descripcion);
    }
}
