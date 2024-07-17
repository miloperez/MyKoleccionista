package com.example.mykoleccionista.GUI;

import com.example.mykoleccionista.GUI.Accion;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.example.mykoleccionista.Negocios.Cliente;
import com.example.mykoleccionista.Negocios.ClienteMgr;
import com.example.mykoleccionista.Negocios.PreventaAbono;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import javax.swing.table.DefaultTableModel;

//CONTROLADOR DE CABC.fxml
public class CABCController {
    //TABLA
    @FXML
    private TableView CABCtableClient;
    //FECHAS
    @FXML
    private DatePicker CABCDatePFNac;
    //CAMPOS DE TEXTO
    @FXML
    private TextField CABCtextID;
    @FXML
    private TextField CABCtextNombre;
    @FXML
    private TextField CABCtextAPat;
    @FXML
    private TextField CABCtextAMat;
    @FXML
    private TextField CABCtextEmail;
    //BOTONES
    @FXML
    private Button CABCbuttonAgregar;
    @FXML
    private Button CABCbuttonEditar;
    @FXML
    private Button CABCbuttonBorrar;
    @FXML
    private Button CABCbuttonAceptar;
    @FXML
    private Button CABCbuttonCancelar;
    @FXML
    private Button CABCButtonReporte;
    @FXML
    private Button CABCbuttonGoBack;

    //VARIABLES DE LA CLASE
    private ClienteMgr clientes;
    private Accion accion = Accion.NAVEGAR;

    public void initialize(){
        try{
            clientes = new ClienteMgr();
            TableColumn<Cliente, Integer> tcId = new TableColumn<>("idCliente");
            TableColumn<Cliente, String> tcNombre = new TableColumn<>("Nombre");
            TableColumn<Cliente, String> tcAPat = new TableColumn<>("APaterno");
            TableColumn<Cliente, String> tcAMat = new TableColumn<>("AMaterno");
            TableColumn<Cliente, String> tcFNac = new TableColumn<>("FNacimiento");
            TableColumn<Cliente, String> tcEmail = new TableColumn<>("Email");

            tcId.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
            tcNombre.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
            tcAPat.setCellValueFactory(new PropertyValueFactory<>("APaterno"));
            tcAMat.setCellValueFactory(new PropertyValueFactory<>("AMaterno"));
            tcFNac.setCellValueFactory(new PropertyValueFactory<>("FNacimiento"));
            tcEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));

            CABCtableClient.getColumns().clear();
            CABCtableClient.getColumns().addAll(tcId, tcNombre, tcAPat, tcAMat, tcFNac, tcEmail);
            //System.out.println(clientes.getClientes());
            CABCtableClient.getItems().addAll(clientes.getClientes());

        } catch (Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error en los Clientes");
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }
        MostrarControles();
    }

    @FXML
    private void clickTablaClientes(MouseEvent mouseEvent) {
        Object object  = CABCtableClient.getSelectionModel().getSelectedItem();
        if (object==null){
            return;
        }
        Cliente cliente = (Cliente) object;
        CABCtextID.setText(String.format("%d", cliente.getIdCliente()));
        CABCtextNombre.setText(cliente.getNombre());
        CABCtextAPat.setText(cliente.getAPaterno());
        CABCtextAMat.setText(cliente.getAMaterno());
        CABCtextEmail.setText(cliente.getEmail());
        CABCDatePFNac.setValue(LocalDate.parse(cliente.getFNacimiento()));
    }

    public void clickCABCbuttonAgregar(ActionEvent actionEvent) {
        accion = Accion.AGREGAR;
        MostrarControles();
    }

    public void clickCABCbuttonEditar(ActionEvent actionEvent) {
        accion = Accion.EDITAR;
        MostrarControles();
    }

    public void clickCABCbuttonBorrar(ActionEvent actionEvent) {
        accion = Accion.BORRAR;
        MostrarControles();
    }

    public void clickCABCbuttonAceptar(ActionEvent actionEvent) {
        Cliente cliente = new Cliente(Integer.valueOf(CABCtextID.getText()), CABCtextNombre.getText(), CABCtextAPat.getText(), CABCtextAMat.getText(), CABCDatePFNac.getValue().toString(), CABCtextEmail.getText());
        try {
            switch (accion) {
                case AGREGAR:
                    clientes.insertarCliente(cliente);
                    break;
                case EDITAR:
                    clientes.editarCliente(cliente);
                    break;
                case BORRAR:
                    clientes.borrarCliente(cliente);
                    break;
            }

            CABCtableClient.getItems().clear();
            CABCtableClient.getItems().addAll(clientes.getClientes());
            Limpiar();
        } catch (Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText(String.format("Error al %s un Cliente", accion.toString().toLowerCase()));
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }

        accion = Accion.NAVEGAR;
        MostrarControles();
    }

    public  void clickCABCbuttonCancelar(ActionEvent actionEvent) {
        accion = Accion.NAVEGAR;
        Limpiar();
        MostrarControles();
    }

    private void MostrarControles(){
        CABCtableClient.setDisable(true);
        CABCtextID.setDisable(true);
        CABCtextNombre.setDisable(true);
        CABCtextAPat.setDisable(true);
        CABCtextAMat.setDisable(true);
        CABCDatePFNac.setDisable(true);
        CABCtextEmail.setDisable(true);
        CABCbuttonAceptar.setVisible(false);
        CABCbuttonCancelar.setVisible(false);
        CABCbuttonAgregar.setVisible(false);
        CABCbuttonBorrar.setVisible(false);
        CABCbuttonEditar.setVisible(false);


        switch (accion){
            case NAVEGAR:
                CABCtableClient.setDisable(false);
                CABCbuttonAgregar.setVisible(true);
                CABCbuttonBorrar.setVisible(true);
                CABCbuttonEditar.setVisible(true);
                break;
            case AGREGAR:
                Limpiar();
                CABCtextID.setDisable(false);
            case EDITAR:
                CABCtextNombre.setDisable(false);
                CABCtextAPat.setDisable(false);
                CABCtextAMat.setDisable(false);
                //CABCtextFNac.setDisable(false);
                CABCDatePFNac.setDisable(false);
                CABCtextEmail.setDisable(false);
            case BORRAR:
                CABCbuttonAceptar.setVisible(true);
                CABCbuttonCancelar.setVisible(true);
        }
    }

    private void Limpiar() {
        CABCtextID.setText("");
        CABCtextNombre.setText("");
        CABCtextAPat.setText("");
        CABCtextAMat.setText("");
        CABCDatePFNac.setValue(null);
        CABCtextEmail.setText("");
    }

    @FXML
    private void clickCABCbuttonGoBack(ActionEvent actionEvent) {
        Stage stage = (Stage) CABCbuttonGoBack.getScene().getWindow();
        stage.close();
    }

    public void clickCABCButtonReporte(ActionEvent actionEvent) {
        try{
            JasperReport jrReporte = (JasperReport) JRLoader.loadObject(new File("reportes\\Clientes.jasper"));
            DefaultTableModel datos = new DefaultTableModel();
            datos.setColumnIdentifiers(new Object[]{"idCliente", "Nombre", "APaterno", "AMaterno", "FNacimiento", "Email"});

            //datos.addRow(new Object[]{1,2,3,4,5,6});
            //ClienteMgr clien = this.Negocios.Cliente();
            for(Cliente cliente: clientes.getClientes()){
                datos.addRow(new Object[]{cliente.getIdCliente(), cliente.getNombre(), cliente.getAPaterno(), cliente.getAMaterno(), cliente.getFNacimiento(), cliente.getEmail()});
            }

            Map<String,Object> parametros = new HashMap<>();
            parametros.put("TITULO", "Reporte de Clientes");
            parametros.put("FECHA", LocalDate.now().toString());

            JasperPrint jpReporte = JasperFillManager.fillReport(jrReporte, parametros, new JRTableModelDataSource(datos));

            JasperViewer.viewReport(jpReporte, false);

        } catch (Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error al generar un reporte en clientes");
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }
    }

    public void clickCABCDatePFNac(ActionEvent actionEvent) {
        if (CABCDatePFNac.getValue() != null) {
            LocalDate today = LocalDate.now();
            LocalDate d2 = CABCDatePFNac.getValue();

            int x = (Period.between(d2,today)).getYears();

            if (x<15 || x>99){

                Alert msg = new Alert(Alert.AlertType.WARNING);
                msg.setTitle("Advertencia");
                msg.setHeaderText("Fecha de nacimiento no válida.");
                msg.showAndWait();
                CABCDatePFNac.setValue(null);
            }
        }
    }
}
