package com.example.mykoleccionista.GUI;

import com.example.mykoleccionista.HelloApplication;
import com.example.mykoleccionista.Negocios.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

//CONTROLADOR DE Preventas.fxml
public class PreventasController {
    //TABLAS
    @FXML
    private TableView<PreventaMaestro> PreventasTable;
    //COMBO BOX
    @FXML
    private ComboBox<Cliente> PreventasComboCliente;
    @FXML
    private ComboBox<PreventaEstado> PreventasComboEstado;
    //FECHAS
    @FXML
    private DatePicker PreventasDatePInicio;
    //BOTONES
    @FXML
    private Button PreventasButtonEstadoMas;
    @FXML
    private Button PreventasButtonGoBack;
    @FXML
    private Button PreventasButtonAceptar;
    @FXML
    private Button PreventasButtonCancelar;
    @FXML
    private Button PreventasButtonAbonar;
    @FXML
    private Button PreventasButtonAgregar;
    @FXML
    private Button PreventasButtonBorrar;
    @FXML
    private Button PreventasButtonReporte;

    //CAMPOS DE TEXTO
    @FXML
    private TextField PreventasTextId;
    @FXML
    private TextField PreventasTextTotal;
    @FXML
    private TextField PreventasTextProductos;

    //VARIABLES DE LA CLASE
    private Accion accion = Accion.NAVEGAR;
    private ClienteMgr Clientes;
    private PreventaEstadoMgr PreventaEstados;
    private ProductoMgr Productos;
    private PreventaDetalleMgr PreventaDetalles;
    private PreventaMaestroMgr PreventaMaestros;
    private LinkedList<PreventaDetalle> ListaDetalles;


    public void initialize() {
        try{
            Clientes = new ClienteMgr();
            Productos = new ProductoMgr();
            PreventaDetalles = new PreventaDetalleMgr();
            PreventaMaestros = new PreventaMaestroMgr();
            PreventaEstados = new PreventaEstadoMgr();

            ListaDetalles = new LinkedList<>();

        }catch(Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error en la inicialización");
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }

        //COMBO Clientes
        try {
            PreventasComboCliente.getItems().clear();
            for (Cliente clien: Clientes.getClientes()){
                PreventasComboCliente.getItems().add(clien);
            }
        }  catch (Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error en los Clientes");
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }

        //COMBO Estado
        try {
            PreventasComboEstado.getItems().clear();
            for (PreventaEstado edo: PreventaEstados.getPreventaEstado()){
                PreventasComboEstado.getItems().add(edo);
            }
        }  catch (Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error en los Productos");
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }

        //TABLA
        try{
            TableColumn<PreventaMaestro, Integer> tcIdPreventa = new TableColumn<>("idPreventa");
            TableColumn<PreventaMaestro, String> tcFecha = new TableColumn<>("Fecha");
            TableColumn<PreventaMaestro, Integer> tcProductos = new TableColumn<>("Productos");
            TableColumn<PreventaMaestro, Float> tcTotal = new TableColumn<>("Total");
            TableColumn<PreventaMaestro, String> tcEstado = new TableColumn<>("Estado");
            TableColumn<PreventaMaestro, String> tcCliente = new TableColumn<>("Cliente");

            tcIdPreventa.setCellValueFactory(new PropertyValueFactory<>("idPreventa"));
            tcFecha.setCellValueFactory(new PropertyValueFactory<>("Fecha"));
            tcProductos.setCellValueFactory(new PropertyValueFactory<>("Productos"));
            tcTotal.setCellValueFactory(new PropertyValueFactory<>("Total"));

            tcCliente.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PreventaMaestro, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<PreventaMaestro, String> dato) {
                    StringProperty spCliente = new SimpleStringProperty();
                    for (Cliente cliente: PreventasComboCliente.getItems()){
                        if (cliente.getIdCliente().equals(dato.getValue().getIdCliente())){
                            spCliente.set(String.format(cliente.getNombre() + cliente.getAPaterno() + cliente.getAMaterno()));
                        }
                    }
                    return spCliente;
                }
            });

            tcEstado.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PreventaMaestro, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<PreventaMaestro, String> dato) {
                    StringProperty spEstado = new SimpleStringProperty();
                    for (PreventaEstado edo: PreventasComboEstado.getItems()){
                        if (edo.getId() == (dato.getValue().getEstado())){
                            spEstado.set(edo.getDescripcion());
                        }
                    }
                    return spEstado;
                }
            });

            PreventasTable.getColumns().clear();
            PreventasTable.getColumns().addAll(tcIdPreventa, tcFecha, tcProductos, tcTotal, tcEstado, tcCliente);
            PreventasTable.getItems().addAll(PreventaMaestros.getPreventaMaestro());

        } catch (Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error en TablaPreventas");
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }
        MostrarControles();
    }

    public void clickPreventasTable()  throws Exception {
        Object object  = PreventasTable.getSelectionModel().getSelectedItem();
        if (object==null){
            return;
        }

        PreventaMaestro PreMae = (PreventaMaestro) object;

        PreventasTextId.setText(String.format("%d", PreMae.getIdPreventa()));
        PreventasTextProductos.setText(String.format("%d", PreMae.getProductos()));
        PreventasTextTotal.setText(String.format("%f", PreMae.getTotal()));
        PreventasDatePInicio.setValue(LocalDate.parse(PreMae.getFecha()));

        //COMBOS
        for(Cliente cliente: PreventasComboCliente.getItems()){
            if (cliente.getIdCliente().equals(PreMae.getIdCliente())){
                PreventasComboCliente.getSelectionModel().select(cliente);
                break;
            }
        }

        for(PreventaEstado edo: PreventasComboEstado.getItems()){
            if (edo.getId() == (PreMae.getEstado())){
                PreventasComboEstado.getSelectionModel().select(edo);
                break;
            }
        }

        PreventaEstado edoo = (PreventaEstado) PreventasComboEstado.getSelectionModel().getSelectedItem();

        if( edoo.getId() == 1 ){
            PreventasButtonAbonar.setDisable(false);
            PreventasButtonAbonar.setVisible(true);
        }else{
            PreventasButtonAbonar.setDisable(true);
            PreventasButtonAbonar.setVisible(false);
        }
    }

    public void clickPreventasButtonAbonar(ActionEvent actionEvent) {
        int id = Integer.parseInt(PreventasTextId.getText());

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(HelloApplication.class.getResource("Abonos.fxml"));
            //fxmlLoader.setController(this);
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Abonos");
            stage.setScene(scene);

            AbonosController controller = fxmlLoader.getController();
            controller.initData(id);

            stage.setUserData(id);

            stage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clickPreventasButtonAgregar(ActionEvent actionEvent) {
        //abrir una nueva ventana para agregar los productos
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(HelloApplication.class.getResource("CarritoPreventas.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Carrito de compras");
            stage.setScene(scene);

            CarritoPreventasController controller = fxmlLoader.getController();
            controller.initData(PreventaMaestros.nextId());
            stage.setUserData(PreventaMaestros.nextId());
            stage.show();

        }catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void clickPreventasButtonBorrar(ActionEvent actionEvent) {
        accion = Accion.BORRAR;
        MostrarControles();
    }

    public void clickPreventasButtonAceptar(ActionEvent actionEvent) {
        Cliente cliente = PreventasComboCliente.getSelectionModel().getSelectedItem();
        PreventaEstado edo =  PreventasComboEstado.getSelectionModel().getSelectedItem();

        //PreventaMaestro(int idPreventa, String fecha, int productos, Float total, int estado, int idCliente)
        //PreventaDetalle( int idPreventa, String SKU, int cantidad, Float costo)

        PreventaMaestro PreMae = new PreventaMaestro(Integer.parseInt(PreventasTextId.getText()), PreventasDatePInicio.getValue().toString(), Integer.parseInt(PreventasTextProductos.getText()), Float.parseFloat(PreventasTextTotal.getText()), edo.getId(), cliente.getIdCliente());

        try{
            switch (accion) {
                case BORRAR:
                    ListaDetalles.addAll(PreventaDetalles.getFromId(PreMae.getIdPreventa()));

                    for(PreventaDetalle detalle: ListaDetalles){
                        PreventaDetalles.borrarPreventaDetalle(detalle);
                    }

                    PreventaMaestros.borrarPreventaMaestro(PreMae);
                    break;
            }

            PreventasTable.getItems().clear();
            PreventasTable.getItems().addAll(PreventaMaestros.getPreventaMaestro());
            Limpiar();
        } catch (Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText(String.format("Error al %s una Venta", accion.toString().toLowerCase()));
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }

    accion = Accion.NAVEGAR;
    MostrarControles();

    }

    public void clickPreventasButtonCancelar(ActionEvent actionEvent) {
        accion = Accion.NAVEGAR;
        Limpiar();
        MostrarControles();
    }

    public void clickPreventasButtonEditar(ActionEvent actionEvent) {
        accion = Accion.EDITAR;
        MostrarControles();
    }

    private void MostrarControles() {
        PreventasTable.setDisable(true);

        PreventasTextId.setDisable(true);
        PreventasComboCliente.setDisable(true);
        PreventasTextTotal.setDisable(true);
        PreventasComboEstado.setDisable(true);
        PreventasDatePInicio.setDisable(true);
        PreventasTextProductos.setDisable(true);

        PreventasButtonAbonar.setVisible(false);
        PreventasButtonAceptar.setVisible(false);
        PreventasButtonCancelar.setVisible(false);
        PreventasButtonAgregar.setVisible(false);
        PreventasButtonBorrar.setVisible(false);

        switch (accion){
            case NAVEGAR:
                PreventasTable.setDisable(false);
                PreventasButtonAgregar.setVisible(true);
                PreventasButtonBorrar.setVisible(true);
                break;

            case AGREGAR:
                Limpiar();
                /*PreventasTextId.setDisable(false);
                PreventasComboCliente.setDisable(false);
                PreventasTextTotal.setDisable(false);
                PreventasComboEstado.setDisable(false);
                PreventasDatePInicio.setDisable(false);
                PreventasTextProductos.setDisable(false);

                PreventasButtonAceptar.setVisible(true);
                PreventasButtonCancelar.setVisible(true);*/
                break;

            /*case EDITAR:
                PreventasComboCliente.setDisable(false);
                PreventasTextTotal.setDisable(false);
                PreventasComboEstado.setDisable(false);
                PreventasDatePInicio.setDisable(false);
                PreventasTextProductos.setDisable(false);

                PreventasButtonAceptar.setVisible(true);
                PreventasButtonCancelar.setVisible(true);
                break;*/

            case BORRAR:
                PreventasButtonAceptar.setVisible(true);
                PreventasButtonCancelar.setVisible(true);
                break;
        }

    }

    private void Limpiar() {
        PreventasTextId.setText("");
        PreventasComboCliente.getSelectionModel().clearSelection();
        PreventasComboEstado.getSelectionModel().clearSelection();
        PreventasDatePInicio.setValue(null);
        PreventasTextProductos.setText("");
        PreventasTextTotal.setText("");
    }

    @FXML
    private void clickPreventasButtonGoBack(ActionEvent actionEvent) {
        Stage stage = (Stage) PreventasButtonGoBack.getScene().getWindow();
        stage.close();
    }

    public void clickPreventasButtonEstadoMas(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(HelloApplication.class.getResource("ListEstadoPV.fxml"));
            //fxmlLoader.setController(this);
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Estados Preventa");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clickPreventasButtonReporte(ActionEvent actionEvent) {
        try{
            JasperReport jrReporte = (JasperReport) JRLoader.loadObject(new File("reportes\\Preventas.jasper"));
            DefaultTableModel datos = new DefaultTableModel();
            datos.setColumnIdentifiers(new Object[]{"idPreventa", "Fecha", "Productos", "Total", "Estado", "Cliente"});

            for(PreventaMaestro PreDet: PreventaMaestros.getPreventaMaestro()){
                for(PreventaEstado preedo: PreventaEstados.getPreventaEstado()){
                    if(PreDet.getEstado() == preedo.getId()){
                        datos.addRow(new Object[]{PreDet.getIdPreventa(), PreDet.getFecha(), PreDet.getProductos(), PreDet.getTotal(), preedo.getDescripcion(), PreDet.getIdCliente()});
                    }
                }
            }

            Map<String,Object> parametros = new HashMap<>();
            parametros.put("TITULO", "Reporte de Preventas");
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

}
