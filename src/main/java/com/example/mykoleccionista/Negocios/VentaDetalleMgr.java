package com.example.mykoleccionista.Negocios;

import com.example.mykoleccionista.Datos.Conectar;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;

public class VentaDetalleMgr {
    private Conectar coneccion = null;


    public VentaDetalleMgr() throws Exception{
        try {
            this.coneccion = Conectar.getInstance();
        } catch (Exception ex){
            throw new Exception(ex);
        }
    }

    public LinkedList<VentaDetalle> getFromId(int id) throws Exception {
        LinkedList<VentaDetalle> ListaVentaDetalle = new LinkedList<>();
        if(getVentaDetalle() != null){
            for(VentaDetalle mae: getVentaDetalle()){
                if(mae.getIdVenta() == id){
                    ListaVentaDetalle.add(mae);
                }
            }
            return ListaVentaDetalle;
        }
        return null;
    }

    public LinkedList<VentaDetalle> getVentaDetalle() throws Exception{
        LinkedList<VentaDetalle> ListaVentaDetalle = new LinkedList<>();
        String consulta = "SELECT * FROM VentaDetalle";
        try {
            ResultSet rsConsulta = this.coneccion.getConsulta(consulta);
            while (rsConsulta.next()){

                ListaVentaDetalle.add(new VentaDetalle(rsConsulta.getInt(1), rsConsulta.getString(2), rsConsulta.getString(3), rsConsulta.getInt(4)));
            }
        } catch (Exception ex){
            throw new Exception(String.format("La consulta '%s' no se pudo ejecutar", consulta));
        }
        return ListaVentaDetalle;
    }

    public void insertarVentaDetalle(VentaDetalle VenDetalle) throws Exception {
        String consulta = "INSERT INTO VentaDetalle(Venta, SKU, FCompra, Cantidad) VALUES (?,?,?,?)";
        try {
            PreparedStatement psInsertar = this.coneccion.getStatement(consulta);
            psInsertar.setInt(1, VenDetalle.getIdVenta());
            psInsertar.setString(2, VenDetalle.getSKU());
            psInsertar.setString(3, VenDetalle.getFCompra());
            psInsertar.setInt(4, VenDetalle.getCantidad());

            psInsertar.execute();
        } catch (Exception ex){
            throw new Exception(String.format("Error al insertar %s", VenDetalle));
        }
    }
    // VentaDetalle(int idVenta, String SKU, String FCompra, int cantidad)
    public void editarVentaDetalle(VentaDetalle VenDetalle) throws Exception {
        String consulta = "UPDATE VentaDetalle SET SKU=?, FCompra=?, Cantidad=? WHERE Venta = ?";
        try {
            PreparedStatement psInsertar = this.coneccion.getStatement(consulta);
            psInsertar.setInt(4, VenDetalle.getIdVenta());
            psInsertar.setString(1, VenDetalle.getSKU());
            psInsertar.setString(2, VenDetalle.getFCompra());
            psInsertar.setInt(3, VenDetalle.getCantidad());

            psInsertar.execute();
        } catch (Exception ex){
            throw new Exception(String.format("Error al editar %s", VenDetalle));
        }
    }

    public void borrarVentaDetalle(VentaDetalle VenDetalle) throws Exception {
        String consulta = "DELETE FROM VentaDetalle WHERE Venta = ?";
        try {
            PreparedStatement psInsertar = this.coneccion.getStatement(consulta);
            psInsertar.setInt(1, VenDetalle.getIdVenta());

            psInsertar.execute();
        } catch (Exception ex){
            throw new Exception(String.format("Error al borrar %s", VenDetalle));
        }
    }
}
