package com.example.mykoleccionista.Negocios;

public class Cliente {
    private Integer idCliente;
    private String Nombre;
    private String APaterno;
    private String AMaterno;
    private String FNacimiento;
    private String Email;

    public Cliente(int idCliente, String nombre, String APaterno, String AMaterno, String FNacimiento, String email) {
        this.idCliente = idCliente;
        this.Nombre = nombre;
        this.APaterno = APaterno;
        this.AMaterno = AMaterno;
        this.FNacimiento = FNacimiento;
        this.Email = email;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getAPaterno() {
        return APaterno;
    }

    public void setAPaterno(String APaterno) {
        this.APaterno = APaterno;
    }

    public String getAMaterno() {
        return AMaterno;
    }

    public void setAMaterno(String AMaterno) {
        this.AMaterno = AMaterno;
    }

    public String getFNacimiento() {
        return FNacimiento;
    }

    public void setFNacimiento(String FNacimiento) {
        this.FNacimiento = FNacimiento;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    @Override
    public String toString() {
        return String.format("%d %s %s %s %s %s", this.idCliente, this.Nombre, this.APaterno, this.AMaterno, this.FNacimiento, this.Email);
    }
}
