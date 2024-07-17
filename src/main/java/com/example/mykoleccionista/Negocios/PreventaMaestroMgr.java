package com.example.mykoleccionista.Negocios;

import com.example.mykoleccionista.Datos.Conectar;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;

public class PreventaMaestroMgr {
    private Conectar coneccion = null;


    public PreventaMaestroMgr() throws Exception{
        try {
            this.coneccion = Conectar.getInstance();
        } catch (Exception ex){
            throw new Exception(ex);
        }
    }

    public PreventaMaestro getFromId(int id) throws Exception {
        for(PreventaMaestro dummy: getPreventaMaestro()){
            if(dummy.getIdPreventa() == id){
                return dummy;
            }
        }
        return null;
    }

    public int nextId() throws Exception {
        int next = 1;
        if (getPreventaMaestro() != null){
            for(PreventaMaestro mae: getPreventaMaestro()){
                if(mae.getIdPreventa() >= next){
                    next = mae.getIdPreventa() + 1;
                }
            }
        }
        return next;
    }

    public LinkedList<PreventaMaestro> getPreventaMaestro() throws Exception{
        LinkedList<PreventaMaestro> ListaPreventaMaestro = new LinkedList<>();
        String consulta = "SELECT * FROM PreventaMaestro";
        try {
            ResultSet rsConsulta = this.coneccion.getConsulta(consulta);
            while (rsConsulta.next()){
                ListaPreventaMaestro.add(new PreventaMaestro(rsConsulta.getInt(1), rsConsulta.getString(2), rsConsulta.getInt(3), rsConsulta.getFloat(4), rsConsulta.getInt(5),rsConsulta.getInt(6)));
            }
        } catch (Exception ex){
            throw new Exception(String.format("La consulta '%s' no se pudo ejecutar", consulta));
        }
        return ListaPreventaMaestro;
    }

    //PreventaMaestro(int idPreventa, String fecha, int productos, Float total, int estado, int idCliente)

    public void insertarPreventaMaestro(PreventaMaestro PrevMaestro) throws Exception {
        String consulta = "INSERT INTO PreventaMaestro(Fecha, Productos, Total, Estado, Cliente) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement psInsertar = this.coneccion.getStatement(consulta);
            //psInsertar.setInt(1, PrevMaestro.getIdPreventa());
            psInsertar.setString(1, PrevMaestro.getFecha());
            psInsertar.setInt(2, PrevMaestro.getProductos());
            psInsertar.setFloat(3, PrevMaestro.getTotal());
            psInsertar.setInt(4, PrevMaestro.getEstado());
            psInsertar.setInt(5, PrevMaestro.getIdCliente());

            psInsertar.execute();
        } catch (Exception ex){
            throw new Exception(String.format("Error al insertar %s", PrevMaestro));
        }
    }

    public void editarPreventaMaestro(PreventaMaestro PrevMaestro) throws Exception {
        String consulta = "UPDATE PreventaMaestro SET Fecha=?, Productos=?, Total=?, Estado=?, Cliente=? WHERE idPreventa = ?";
        try {
            PreparedStatement psInsertar = this.coneccion.getStatement(consulta);
            psInsertar.setInt(6, PrevMaestro.getIdPreventa());
            psInsertar.setString(1, PrevMaestro.getFecha());
            psInsertar.setInt(2, PrevMaestro.getProductos());
            psInsertar.setFloat(3, PrevMaestro.getTotal());
            psInsertar.setInt(4, PrevMaestro.getEstado());
            psInsertar.setInt(5, PrevMaestro.getIdCliente());

            psInsertar.execute();
        } catch (Exception ex){
            throw new Exception(String.format("Error al editar %s", PrevMaestro));
        }
    }

    public void borrarPreventaMaestro(PreventaMaestro PrevMaestro) throws Exception {
        String consulta = "DELETE FROM PreventaMaestro WHERE idPreventa = ?";
        try {
            PreparedStatement psInsertar = this.coneccion.getStatement(consulta);
            psInsertar.setInt(1, PrevMaestro.getIdPreventa());

            psInsertar.execute();
        } catch (Exception ex){
            throw new Exception(String.format("Error al borrar %s", PrevMaestro));
        }
    }
}
