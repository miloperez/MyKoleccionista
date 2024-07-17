package com.example.mykoleccionista.Negocios;

import com.example.mykoleccionista.Datos.Conectar;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;

public class ClienteMgr {
    private Conectar coneccion = null;


    public ClienteMgr() throws Exception{
        try {
            this.coneccion = Conectar.getInstance();
        } catch (Exception ex){
            throw new Exception(ex);
        }
    }

    public LinkedList<Cliente> getClientes() throws Exception{
        LinkedList<Cliente> ListaClientes = new LinkedList<>();
        String consulta = "SELECT * FROM Clientes";
        try {
            ResultSet rsConsulta = this.coneccion.getConsulta(consulta);
            //ResultSet rsConsulta = this.conectar.getResultados(consulta);
            while (rsConsulta.next()){
                ListaClientes.add(new Cliente(rsConsulta.getInt(1), rsConsulta.getString(2), rsConsulta.getString(3), rsConsulta.getString(4), rsConsulta.getString(5),rsConsulta.getString(6)));
            }
        } catch (Exception ex){
            throw new Exception(String.format("La consulta '%s' no se pudo ejecutar", consulta));
        }
        return ListaClientes;
    }

    public void insertarCliente(Cliente cliente) throws Exception {
        String consulta = "INSERT INTO Clientes(Nombre, APaterno, AMaterno, FNacimiento, Email) VALUES (?,?,?,?,?)";
        // "INSERT INTO Clientes(idCliente, Nombre, APaterno, AMaterno, FNacimiento, Email) VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement psInsertar = this.coneccion.getStatement(consulta);
            //psInsertar.setInt(1, cliente.getIdCliente());
            psInsertar.setString(1, cliente.getNombre());
            psInsertar.setString(2, cliente.getAPaterno());
            psInsertar.setString(3, cliente.getAMaterno());
            psInsertar.setString(4, cliente.getFNacimiento());
            psInsertar.setString(5, cliente.getEmail());

            psInsertar.execute();
        } catch (Exception ex){
            throw new Exception(String.format("Error al insertar %s", cliente));
        }
    }

    public void editarCliente(Cliente cliente) throws Exception {
        String consulta = "UPDATE Clientes SET Nombre=?, APaterno=?, AMaterno=?, FNacimiento=?, Email=? WHERE idCliente = ?";
        try {
            PreparedStatement psInsertar = this.coneccion.getStatement(consulta);
            psInsertar.setInt(6, cliente.getIdCliente());
            psInsertar.setString(1, cliente.getNombre());
            psInsertar.setString(2, cliente.getAPaterno());
            psInsertar.setString(3, cliente.getAMaterno());
            psInsertar.setString(4, cliente.getFNacimiento());
            psInsertar.setString(5, cliente.getEmail());

            psInsertar.execute();
        } catch (Exception ex){
            throw new Exception(String.format("Error al editar %s", cliente));
        }
    }

    public void borrarCliente(Cliente cliente) throws Exception {
        String consulta = "DELETE FROM Clientes WHERE idCliente = ?";
        try {
            PreparedStatement psInsertar = this.coneccion.getStatement(consulta);
            psInsertar.setInt(1, cliente.getIdCliente());

            psInsertar.execute();
        } catch (Exception ex){
            throw new Exception(String.format("Error al borrar %s", cliente));
        }
    }

}
