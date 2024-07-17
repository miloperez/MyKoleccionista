package com.example.mykoleccionista.Negocios;

import com.example.mykoleccionista.Datos.Coneccion;
import com.example.mykoleccionista.Datos.Conectar;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;


public class ProductoMgr {
    private Conectar coneccion = null;

    public ProductoMgr() throws Exception{
        try {
            this.coneccion = Conectar.getInstance();
        } catch (Exception ex){
            throw new Exception(ex);
        }
    }

    public Producto getFromId(String id) throws Exception {
        for(Producto dummy: getProductos()){
            if(dummy.getSKU().equals(id)){
                return dummy;
            }
        }
        return null;
    }

    public LinkedList<Producto> getProductos() throws Exception{
        LinkedList<Producto> ListaProductos = new LinkedList<>();
        String consulta = "SELECT * FROM Productos";
        try {
            ResultSet rsConsulta = this.coneccion.getConsulta(consulta);
            //ResultSet rsConsulta = this.conectar.getResultados(consulta);
            while (rsConsulta.next()){
                ListaProductos.add(new Producto(rsConsulta.getString(1),
                                                rsConsulta.getString(2),
                                                rsConsulta.getString(3),
                                                rsConsulta.getString(4),
                                                rsConsulta.getFloat(5),
                                                rsConsulta.getString(6),
                                                rsConsulta.getInt(7),
                                                rsConsulta.getInt(8)));
            }
        } catch (Exception ex){
            throw new Exception(String.format("La consulta '%s' no se pudo ejecutar", consulta));
        }
        return ListaProductos;
    }

    public void insertarProducto(Producto producto) throws Exception {
        String consulta = "INSERT INTO Productos(SKU, Nombre, Descripcion, Fotografia, Calificacion, FDisponibilidad, TipoProducto, TipoEstado) VALUES (?,?,?,?,?,?,?,?)";
        try {
            //Producto(String SKU, String nombre, String descripcion, String fotografia, Float calificacion, String FDisponibilidad, Integer tipoProducto, Integer tipoEstado)
            PreparedStatement psInsertar = this.coneccion.getStatement(consulta);
            psInsertar.setString(1, producto.getSKU());
            psInsertar.setString(2, producto.getNombre());
            psInsertar.setString(3, producto.getDescripcion());
            psInsertar.setString(4, producto.getFotografia());
            psInsertar.setFloat(5, producto.getCalificacion());
            psInsertar.setString(6, producto.getFDisponibilidad());
            psInsertar.setInt(7, producto.getTipoProducto());
            psInsertar.setInt(8, producto.getTipoEstado());

            psInsertar.execute();
        } catch (Exception ex){
            throw new Exception(String.format("Error al insertar %s", producto));
        }
    }

    public void editarProducto(Producto producto) throws Exception {
        String consulta = "UPDATE Productos SET Nombre=?, Descripcion=?, Fotografia=?, Calificacion=?, FDisponibilidad=?, TipoProducto=?, TipoEstado=? WHERE SKU = ?";
        try {
            //Producto(String SKU, String nombre, String descripcion, String fotografia, Float calificacion, String FDisponibilidad, Integer tipoProducto, Integer tipoEstado)
            PreparedStatement psInsertar = this.coneccion.getStatement(consulta);
            psInsertar.setString(8, producto.getSKU());
            psInsertar.setString(1, producto.getNombre());
            psInsertar.setString(2, producto.getDescripcion());
            psInsertar.setString(3, producto.getFotografia());
            psInsertar.setFloat(4, producto.getCalificacion());
            psInsertar.setString(5, producto.getFDisponibilidad());
            psInsertar.setInt(6, producto.getTipoProducto());
            psInsertar.setInt(7, producto.getTipoEstado());

            psInsertar.execute();
        } catch (Exception ex){
            throw new Exception(String.format("Error al editar %s", producto));
        }
    }

    public void borrarProducto(Producto producto) throws Exception {
        String consulta = "DELETE FROM Productos WHERE SKU = ?";
        try {
            PreparedStatement psInsertar = this.coneccion.getStatement(consulta);
            psInsertar.setString(1, producto.getSKU());

            psInsertar.execute();
        } catch (Exception ex){
            throw new Exception(String.format("Error al borrar %s", producto));
        }
    }



}
