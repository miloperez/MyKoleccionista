package com.example.mykoleccionista.Negocios;

import com.example.mykoleccionista.Datos.Conectar;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;

public class PreventaAbonoMgr {
    private Conectar coneccion = null;

    public PreventaAbonoMgr() throws Exception{
        try {
            this.coneccion = Conectar.getInstance();
        } catch (Exception ex){
            throw new Exception(ex);
        }
    }

    public LinkedList<PreventaAbono> getFromId(int id) throws Exception {
        if(getPreventaAbono() != null){
            LinkedList<PreventaAbono> ListaPreventaAbono = new LinkedList<>();
            for(PreventaAbono dummy: getPreventaAbono()){
                if(dummy.getIdPreventa() == id){
                    ListaPreventaAbono.add(dummy);
                }
            }
            return ListaPreventaAbono;
        }
        return null;
    }

    public LinkedList<PreventaAbono> getPreventaAbono() throws Exception{
        LinkedList<PreventaAbono> ListaPreventaAbono = new LinkedList<>();
        String consulta = "SELECT * FROM PreventaAbono";
        try {
            ResultSet rsConsulta = this.coneccion.getConsulta(consulta);
            while (rsConsulta.next()){
                //PreventaAbono(int idPreventa, String fecha, Float cantidad) 
                ListaPreventaAbono.add(new PreventaAbono(rsConsulta.getInt(1), rsConsulta.getString(2), rsConsulta.getFloat(3)));
            }
        } catch (Exception ex){
            throw new Exception(String.format("La consulta '%s' no se pudo ejecutar", consulta));
        }
        return ListaPreventaAbono;
    }

    public void insertarPreventaAbono(PreventaAbono PrevAbono) throws Exception {
        String consulta = "INSERT INTO PreventaAbono(Preventa, Fecha, Cantidad) VALUES (?,?,?)";
        try {
            
            PreparedStatement psInsertar = this.coneccion.getStatement(consulta);
            psInsertar.setInt(1, PrevAbono.getIdPreventa());
            psInsertar.setString(2, PrevAbono.getFecha());
            psInsertar.setFloat(3, PrevAbono.getCantidad());

            psInsertar.execute();
        } catch (Exception ex){
            throw new Exception(String.format("Error al insertar %s", PrevAbono));
        }
    }

    public void editarPreventaAbono(PreventaAbono PrevAbono) throws Exception {
        //PreventaAbono(int idPreventa, String fecha, Float cantidad)
        String consulta = "UPDATE PreventaAbono SET Cantidad=? WHERE Preventa = ? and Fecha=?";
        try {
            PreparedStatement psInsertar = this.coneccion.getStatement(consulta);
            psInsertar.setInt(2, PrevAbono.getIdPreventa());
            psInsertar.setString(3, PrevAbono.getFecha());
            psInsertar.setFloat(1, PrevAbono.getCantidad());

            psInsertar.execute();
        } catch (Exception ex){
            throw new Exception(String.format("Error al editar %s", PrevAbono));
        }
    }

    public void borrarPreventaAbono(PreventaAbono PrevAbono) throws Exception {
        String consulta = "DELETE FROM PreventaAbono WHERE Preventa = ? and Fecha = ?";
        try {
            PreparedStatement psInsertar = this.coneccion.getStatement(consulta);
            psInsertar.setInt(1, PrevAbono.getIdPreventa());
            psInsertar.setString(2, PrevAbono.getFecha());

            psInsertar.execute();
        } catch (Exception ex){
            throw new Exception(String.format("Error al borrar %s", PrevAbono));
        }
    }
}
