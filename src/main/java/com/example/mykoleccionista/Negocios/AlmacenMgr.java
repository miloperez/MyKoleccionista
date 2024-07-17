package com.example.mykoleccionista.Negocios;

import com.example.mykoleccionista.Datos.Conectar;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.LinkedList;

public class AlmacenMgr {
    private Conectar coneccion = null;


    public AlmacenMgr() throws Exception{
        try {
            this.coneccion = Conectar.getInstance();
        } catch (Exception ex){
            throw new Exception(ex);
        }
    }

    public LinkedList<Almacen> getFromSKU(String SKU) throws Exception {
        LinkedList<Almacen> ListaAlmacen = new LinkedList<>();
        if(getAlmacen() != null){
            for(Almacen alma: getAlmacen()){
                if(alma.getSKU().equals(SKU)){
                    ListaAlmacen.add(alma);
                }
            }
            return ListaAlmacen;
        }
        return null;
    }

    public LinkedList<Almacen> getAlmacen() throws Exception{
        LinkedList<Almacen> ListaAlmacen = new LinkedList<>();
        String consulta = "SELECT * FROM Almacen";
        try {
            ResultSet rsConsulta = this.coneccion.getConsulta(consulta);
            while (rsConsulta.next()){
                //Almacen(String SKU, String FCompra, int cantidad, Float precioCompra, Float precioVenta, int disponibles)
                ListaAlmacen.add(new Almacen(rsConsulta.getString(1), rsConsulta.getString(2), rsConsulta.getInt(3), rsConsulta.getFloat(4), rsConsulta.getFloat(5),rsConsulta.getInt(6)));
            }
        } catch (Exception ex){
            throw new Exception(String.format("La consulta '%s' no se pudo ejecutar", consulta));
        }
        return ListaAlmacen;
    }

    public void insertarAlmacen(Almacen almacen) throws Exception {
        String consulta = "INSERT INTO Almacen(SKU, FCompra, Cantidad, PrecioCompra, PrecioVenta, Disponibles) VALUES (?,?,?,?,?,?)";
        try {
            //Almacen(String SKU, String FCompra, int cantidad, Float precioCompra, Float precioVenta, int disponibles)
            PreparedStatement psInsertar = this.coneccion.getStatement(consulta);
            psInsertar.setString(1, almacen.getSKU());
            psInsertar.setString(2, almacen.getFCompra());
            psInsertar.setInt(3, almacen.getCantidad());
            psInsertar.setFloat(4, almacen.getPrecioCompra());
            psInsertar.setFloat(5, almacen.getPrecioVenta());
            psInsertar.setInt(6, almacen.getDisponibles());

            psInsertar.execute();
        } catch (Exception ex){
            throw new Exception(String.format("Error al insertar %s", almacen));
        }
    }

    public void editarAlmacen(Almacen almacen) throws Exception {
        String consulta = "UPDATE Almacen SET Cantidad=?, PrecioCompra=?, PrecioVenta=?, Disponibles=? WHERE SKU = ? and FCompra=?";
        try {
            PreparedStatement psInsertar = this.coneccion.getStatement(consulta);
            //Almacen(String SKU, String FCompra, int cantidad, Float precioCompra, Float precioVenta, int disponibles)
            psInsertar.setString(5, almacen.getSKU());
            psInsertar.setString(6, almacen.getFCompra());
            psInsertar.setInt(1, almacen.getCantidad());
            psInsertar.setFloat(2, almacen.getPrecioCompra());
            psInsertar.setFloat(3, almacen.getPrecioVenta());
            psInsertar.setInt(4, almacen.getDisponibles());

            psInsertar.execute();
        } catch (Exception ex){
            throw new Exception(String.format("Error al editar %s", almacen));
        }
    }

    public void borrarAlmacen(Almacen almacen) throws Exception {
        String consulta = "DELETE FROM Almacen WHERE SKU = ?";
        try {
            PreparedStatement psInsertar = this.coneccion.getStatement(consulta);
            psInsertar.setString(1, almacen.getSKU());

            psInsertar.execute();
        } catch (Exception ex){
            throw new Exception(String.format("Error al borrar %s", almacen));
        }
    }

}
