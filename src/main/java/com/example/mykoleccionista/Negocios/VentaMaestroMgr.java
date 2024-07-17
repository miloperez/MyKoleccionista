package com.example.mykoleccionista.Negocios;

import com.example.mykoleccionista.Datos.Conectar;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;

public class VentaMaestroMgr {
    private Conectar coneccion = null;


    public VentaMaestroMgr() throws Exception{
        try {
            this.coneccion = Conectar.getInstance();
        } catch (Exception ex){
            throw new Exception(ex);
        }
    }

    public VentaMaestro getFromId(int id) throws Exception {
        for(VentaMaestro mae: getVentaMaestro()){
            if(mae.getIdVenta() == id){
                return mae;
            }
        }
        return null;
    }

    public int nextId() throws Exception {
        int next = 1;
        if (getVentaMaestro() != null){
            for(VentaMaestro mae: getVentaMaestro()){
                if(mae.getIdVenta() >= next){
                    next = mae.getIdVenta() + 1;
                }
            }
        }
        return next;
    }

    public LinkedList<VentaMaestro> getVentaMaestro() throws Exception{
        LinkedList<VentaMaestro> ListaVentaMaestro = new LinkedList<>();
        String consulta = "SELECT * FROM VentaMaestro";
        try {
            ResultSet rsConsulta = this.coneccion.getConsulta(consulta);
            while (rsConsulta.next()){
                ListaVentaMaestro.add(new VentaMaestro(rsConsulta.getInt(1), rsConsulta.getInt(2), rsConsulta.getString(3), rsConsulta.getInt(4), rsConsulta.getFloat(5)));
            }
        } catch (Exception ex){
            throw new Exception(String.format("La consulta '%s' no se pudo ejecutar", consulta));
        }
        return ListaVentaMaestro;
    }

    public void insertarVentaMaestro(VentaMaestro VenMae) throws Exception {
        String consulta = "INSERT INTO VentaMaestro(idVenta, Cliente, Fecha, Productos, Total) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement psInsertar = this.coneccion.getStatement(consulta);
            psInsertar.setInt(1, VenMae.getIdVenta());
            psInsertar.setInt(2, VenMae.getIdCliente());
            psInsertar.setString(3, VenMae.getFecha());
            psInsertar.setInt(4, VenMae.getProductos());
            psInsertar.setFloat(5, VenMae.getTotal());

            psInsertar.execute();
        } catch (Exception ex){
            throw new Exception(String.format("Error al insertar %s", VenMae));
        }
    }

    public void editarVentaMaestro(VentaMaestro VenMae) throws Exception {
        String consulta = "UPDATE VentaMaestro SET Cliente=?, Fecha=?, Productos=?, Total=? WHERE idVenta = ?";
        try {
            PreparedStatement psInsertar = this.coneccion.getStatement(consulta);
            psInsertar.setInt(5, VenMae.getIdVenta());
            psInsertar.setInt(1, VenMae.getIdCliente());
            psInsertar.setString(2, VenMae.getFecha());
            psInsertar.setInt(3, VenMae.getProductos());
            psInsertar.setFloat(4, VenMae.getTotal());

            psInsertar.execute();
        } catch (Exception ex){
            throw new Exception(String.format("Error al editar %s", VenMae));
        }
    }

    public void borrarVentaMaestro(VentaMaestro VenMae) throws Exception {
        String consulta = "DELETE FROM VentaMaestro WHERE idVenta = ?";
        try {
            PreparedStatement psInsertar = this.coneccion.getStatement(consulta);
            psInsertar.setInt(1, VenMae.getIdVenta());

            psInsertar.execute();
        } catch (Exception ex){
            throw new Exception(String.format("Error al borrar %s", VenMae));
        }
    }

}
