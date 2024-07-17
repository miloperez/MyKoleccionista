package com.example.mykoleccionista.Negocios;

import com.example.mykoleccionista.Datos.Conectar;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;

public class PreventaEstadoMgr {
    private Conectar coneccion = null;


    public PreventaEstadoMgr() throws Exception{
        try {
            this.coneccion = Conectar.getInstance();
        } catch (Exception ex){
            throw new Exception(ex);
        }
    }

    public LinkedList<PreventaEstado> getPreventaEstado() throws Exception {
        LinkedList<PreventaEstado> ListaPreventaEstado = new LinkedList<>();

        String consulta = "SELECT * FROM PreventaEstado";
        try {
            ResultSet rsConsulta = this.coneccion.getConsulta(consulta);

            while(rsConsulta.next()){
                ListaPreventaEstado.add(new PreventaEstado(rsConsulta.getInt(1), rsConsulta.getString(2)));
            }

        } catch (Exception ex){
            throw new Exception(String.format("La consulta '%s' no se pudo ejecutar", consulta));
        }
        return ListaPreventaEstado;
    }

    public void insertarPreventaEstado(PreventaEstado PrevEstado) throws Exception {
        String consulta = "INSERT INTO PreventaEstado(idPreventaEstado, Descripcion) VALUES (?,?)";
        try {
            PreparedStatement psInsertar = this.coneccion.getStatement(consulta);
            psInsertar.setInt(1, PrevEstado.getId());
            psInsertar.setString(2, PrevEstado.getDescripcion());

            psInsertar.execute();
        } catch (Exception ex){
            throw new Exception(String.format("Error al insertar %s", PrevEstado));
        }
    }

    public void editarPreventaEstado(PreventaEstado PrevEstado) throws Exception {
        String consulta = "UPDATE PreventaEstado SET Descripcion=? WHERE idPreventaEstado = ?";
        try {
            PreparedStatement psInsertar = this.coneccion.getStatement(consulta);
            psInsertar.setInt(2, PrevEstado.getId());
            psInsertar.setString(1, PrevEstado.getDescripcion());

            psInsertar.execute();
        } catch (Exception ex){
            throw new Exception(String.format("Error al editar %s", PrevEstado));
        }
    }

    public void borrarPreventaEstado(PreventaEstado PrevEstado) throws Exception {
        String consulta = "DELETE FROM PreventaEstado WHERE idPreventaEstado = ?";
        try {
            PreparedStatement psInsertar = this.coneccion.getStatement(consulta);
            psInsertar.setInt(1, PrevEstado.getId());

            psInsertar.execute();
        } catch (Exception ex){
            throw new Exception(String.format("Error al borrar %s", PrevEstado));
        }
    }
}
