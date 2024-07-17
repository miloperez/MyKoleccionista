package com.example.mykoleccionista.Negocios;

import com.example.mykoleccionista.Datos.Coneccion;
import com.example.mykoleccionista.Datos.Conectar;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;

public class TipoEstadoMgr {
    private Conectar coneccion = null;

    public TipoEstadoMgr() throws Exception {
        try {
            this.coneccion = Conectar.getInstance();
        } catch (Exception ex){
            throw new Exception(ex);
        }
    }

    public LinkedList<TipoEstado> getTipoEstado() throws Exception {
        LinkedList<TipoEstado> ListaTipoEstado = new LinkedList<>();

        String consulta = "SELECT * FROM TipoEstado";
        try {
            ResultSet rsConsulta = this.coneccion.getConsulta(consulta);

            while(rsConsulta.next()){
                ListaTipoEstado.add(new TipoEstado(rsConsulta.getInt(1), rsConsulta.getString(2)));
            }

        } catch (Exception ex){
            throw new Exception(String.format("La consulta '%s' no se pudo ejecutar", consulta));
        }
        return ListaTipoEstado;
    }

    public void insertarTipoEstado(TipoEstado tipoestado) throws Exception {
        String consulta = "INSERT INTO TipoEstado(idTipoEstado, Descripcion) VALUES (?,?)";
        try {
            PreparedStatement psInsertar = this.coneccion.getStatement(consulta);
            psInsertar.setInt(1, tipoestado.getId());
            psInsertar.setString(2, tipoestado.getDescripcion());

            psInsertar.execute();
        } catch (Exception ex){
            throw new Exception(String.format("Error al insertar %s", tipoestado));
        }
    }

    public void editarTipoEstado(TipoEstado tipoestado) throws Exception {
        String consulta = "UPDATE TipoEstado SET Descripcion=? WHERE idTipoEstado = ?";
        try {
            PreparedStatement psInsertar = this.coneccion.getStatement(consulta);
            psInsertar.setInt(2, tipoestado.getId());
            psInsertar.setString(1, tipoestado.getDescripcion());

            psInsertar.execute();
        } catch (Exception ex){
            throw new Exception(String.format("Error al editar %s", tipoestado));
        }
    }

    public void borrarTipoEstado(TipoEstado tipoestado) throws Exception {
        String consulta = "DELETE FROM TipoEstado WHERE idTipoEstado = ?";
        try {
            PreparedStatement psInsertar = this.coneccion.getStatement(consulta);
            psInsertar.setInt(1, tipoestado.getId());

            psInsertar.execute();
        } catch (Exception ex){
            throw new Exception(String.format("Error al borrar %s", tipoestado));
        }
    }
}
