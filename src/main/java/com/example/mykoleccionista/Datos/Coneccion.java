package com.example.mykoleccionista.Datos;


import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

public class Coneccion {
    private Connection connection = null;
    private String dbNombre;

    public Coneccion() throws Exception {
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream("configuracion.properties"));
            this.dbNombre = prop.getProperty("dbname");
        } catch (Exception ex){
            throw new Exception("El archivo de configuración configuracion.properties no existe");
        }
        try {
            String url = "jdbc:sqlite:" + dbNombre;
            this.connection = DriverManager.getConnection(url);
        } catch (Exception ex){
            throw new Exception(String.format("No se pudo conectar a la base de datos %s", this.dbNombre));
        }
    }

    public Connection getConnection(){
        return this.connection;
    }

    public PreparedStatement getStatement(String cad) throws SQLException {
        return this.connection.prepareStatement(cad);
    }

    public ResultSet getConsulta(String cad) throws SQLException {
        PreparedStatement psConsulta = this.connection.prepareStatement(cad);
        return psConsulta.executeQuery();
    }
}
