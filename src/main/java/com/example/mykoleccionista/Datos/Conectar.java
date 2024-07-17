package com.example.mykoleccionista.Datos;

import java.sql.*;

public class Conectar {
    private static Conectar conectar = null;
    private Connection con;

    public Connection getCon(){
        return  con;
    }

    public ResultSet getResultados(String consulta) throws Exception{
        PreparedStatement ps = this.con.prepareStatement(consulta);
        return ps.executeQuery();
    }
    private Conectar(){
        //String url_conexion = "jdbc:mysql://localhost:3306?user=root&password=root";
        String url_conexion = "jdbc:sqlite:datos.db";
        try {
            this.con = DriverManager.getConnection(url_conexion);
        } catch (Exception ex){
            System.out.println(ex);
        }
    };

    public static Conectar getInstance(){
        if (conectar==null){
            conectar = new Conectar();
        }

        return conectar;
    }

    public PreparedStatement getStatement(String cad) throws SQLException {
        return this.con.prepareStatement(cad);
    }

    public ResultSet getConsulta(String cad) throws SQLException {
        PreparedStatement psConsulta = this.con.prepareStatement(cad);
        return psConsulta.executeQuery();
    }

}
