package com.example.mykoleccionista.Negocios;

public class PreventaEstado {
    private int id;
    private String Descripcion;

    public PreventaEstado(int id, String descripcion) {
        this.id = id;
        this.Descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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