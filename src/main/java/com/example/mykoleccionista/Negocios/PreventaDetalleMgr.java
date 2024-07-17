package com.example.mykoleccionista.Negocios;

import com.example.mykoleccionista.Datos.Conectar;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;

public class PreventaDetalleMgr {
    private Conectar coneccion = null;


    public PreventaDetalleMgr() throws Exception{
        try {
            this.coneccion = Conectar.getInstance();
        } catch (Exception ex){
            throw new Exception(ex);
        }
    }

    public LinkedList<PreventaDetalle> getFromId(int id) throws Exception {
        if(getPreventaDetalle() != null){
            LinkedList<PreventaDetalle> ListaPreventaDetalle = new LinkedList<>();
            for(PreventaDetalle dummy: getPreventaDetalle()){
                if(dummy.getIdPreventa() == id){
                    ListaPreventaDetalle.add(dummy);
                }
            }
            return ListaPreventaDetalle;
        }
        return null;
    }


    public LinkedList<PreventaDetalle> getPreventaDetalle() throws Exception{
        LinkedList<PreventaDetalle> ListaPreventaDetalle = new LinkedList<>();
        String consulta = "SELECT * FROM PreventaDetalle";
        try {
            ResultSet rsConsulta = this.coneccion.getConsulta(consulta);
            while (rsConsulta.next()){
                ListaPreventaDetalle.add(new PreventaDetalle(rsConsulta.getInt(1), rsConsulta.getString(2), rsConsulta.getInt(3), rsConsulta.getFloat(4)));
            }
        } catch (Exception ex){
            throw new Exception(String.format("La consulta '%s' no se pudo ejecutar", consulta));
        }
        return ListaPreventaDetalle;
    }
    //PreventaDetalle( int idPreventa, String SKU, int cantidad, Float costo)
    public void insertarPreventaDetalle(PreventaDetalle PrevDetalle) throws Exception {
        String consulta = "INSERT INTO PreventaDetalle(Preventa, SKU, Cantidad, Costo) VALUES (?,?,?,?)";
        try {
            PreparedStatement psInsertar = this.coneccion.getStatement(consulta);
            psInsertar.setInt(1, PrevDetalle.getIdPreventa());
            psInsertar.setString(2, PrevDetalle.getSKU());
            psInsertar.setInt(3, PrevDetalle.getCantidad());
            psInsertar.setFloat(4, PrevDetalle.getCosto());

            psInsertar.execute();
        } catch (Exception ex){
            throw new Exception(String.format("Error al insertar %s", PrevDetalle));
        }
    }

    public void editarPreventaDetalle(PreventaDetalle PrevDetalle) throws Exception {
        String consulta = "UPDATE PreventaDetalle SET SKU = ?, Cantidad = ?, Costo = ? WHERE Preventa = ?";
        try {
            PreparedStatement psInsertar = this.coneccion.getStatement(consulta);
            psInsertar.setInt(4, PrevDetalle.getIdPreventa());
            psInsertar.setString(1, PrevDetalle.getSKU());
            psInsertar.setInt(2, PrevDetalle.getCantidad());
            psInsertar.setFloat(3, PrevDetalle.getCosto());

            psInsertar.execute();
        } catch (Exception ex){
            throw new Exception(String.format("Error al editar %s", PrevDetalle));
        }
    }

    public void borrarPreventaDetalle(PreventaDetalle PrevDetalle) throws Exception {
        String consulta = "DELETE FROM PreventaDetalle WHERE SKU = ?";
        try {
            PreparedStatement psInsertar = this.coneccion.getStatement(consulta);
            psInsertar.setInt(1, PrevDetalle.getIdPreventa());

            psInsertar.execute();
        } catch (Exception ex){
            throw new Exception(String.format("Error al borrar %s", PrevDetalle));
        }
    }

}
