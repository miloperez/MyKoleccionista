package com.example.mykoleccionista.Negocios;

import com.example.mykoleccionista.Datos.Coneccion;
import com.example.mykoleccionista.Datos.Conectar;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;

public class TipoProductoMgr {
    private Conectar coneccion = null;

    public TipoProductoMgr() throws Exception {
        try {
            this.coneccion = Conectar.getInstance();
        } catch (Exception ex){
            throw new Exception(ex);
        }
    }

    public LinkedList<TipoProducto> getTipoProducto() throws Exception {
        LinkedList<TipoProducto> ListaTipoProducto = new LinkedList<>();

        String consulta = "SELECT * FROM TipoProductos";
        try {
            ResultSet rsConsulta = this.coneccion.getConsulta(consulta);

            while(rsConsulta.next()){
                ListaTipoProducto.add(new TipoProducto(rsConsulta.getInt(1), rsConsulta.getString(2)));
            }

        } catch (Exception ex){
            throw new Exception(String.format("La consulta '%s' no se pudo ejecutar", consulta));
        }
        return ListaTipoProducto;
    }

    public void insertarTipoProducto(TipoProducto tipoproducto) throws Exception {
        String consulta = "INSERT INTO TipoProductos(idTipoProducto, Descripcion) VALUES (?,?)";
        try {
            PreparedStatement psInsertar = this.coneccion.getStatement(consulta);
            psInsertar.setInt(1, tipoproducto.getId());
            psInsertar.setString(2, tipoproducto.getDescripcion());

            psInsertar.execute();
        } catch (Exception ex){
            throw new Exception(String.format("Error al insertar %s", tipoproducto));
        }
    }

    public void editarTipoProducto(TipoProducto tipoproducto) throws Exception {
        String consulta = "UPDATE TipoProductos SET Descripcion=? WHERE idTipoProducto = ?";
        try {
            PreparedStatement psInsertar = this.coneccion.getStatement(consulta);
            psInsertar.setInt(2, tipoproducto.getId());
            psInsertar.setString(1, tipoproducto.getDescripcion());

            psInsertar.execute();
        } catch (Exception ex){
            throw new Exception(String.format("Error al editar %s", tipoproducto));
        }
    }

    public void borrarTipoProducto(TipoProducto tipoproducto) throws Exception {
        String consulta = "DELETE FROM TipoProductos WHERE idTipoProducto = ?";
        try {
            PreparedStatement psInsertar = this.coneccion.getStatement(consulta);
            psInsertar.setInt(1, tipoproducto.getId());

            psInsertar.execute();
        } catch (Exception ex){
            throw new Exception(String.format("Error al borrar %s", tipoproducto));
        }
    }
}
