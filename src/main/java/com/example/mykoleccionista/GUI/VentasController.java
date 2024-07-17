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

//CONTROLADOR DE VENTAS.fxml
public class VentasController {
    //TABLAS
    @FXML
    private TableView VentasTable;
    //COMBO BOX
    @FXML
    private ComboBox<Cliente> VentasComboCliente;
    //FECHAS
    @FXML
    private DatePicker VentasDatePFecha;
    //BOTONES
    @FXML
    private Button VentasButtonGoBack;
    @FXML
    private Button VentasButtonAceptar;
    @FXML
    private Button VentasButtonCancelar;
    @FXML
    private Button VentasButtonAgregar;
    @FXML
    private Button VentasButtonBorrar;
    @FXML
    private Button VentasButtonReporte;
    //CAMPOS DE TEXTO
    @FXML
    private TextField VentasTextProductos;
    @FXML
    private TextField VentasTextTotal;
    @FXML
    private TextField VentasTextId;
    //VARIABLES DE LA CLASE
    private Accion accion = Accion.NAVEGAR;
    private ClienteMgr Clientes;
    private VentaMaestroMgr VentaMaestros;
    private VentaDetalleMgr VentaDetalles;
    private AlmacenMgr Almacenes;
    private LinkedList<VentaDetalle> ListaDetalles;

    public void initialize() {

        try{
            Clientes = new ClienteMgr();
            Almacenes = new AlmacenMgr();
            VentaMaestros = new VentaMaestroMgr();
            VentaDetalles = new VentaDetalleMgr();

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
            VentasComboCliente.getItems().clear();
            for (Cliente cliente: Clientes.getClientes()){
                VentasComboCliente.getItems().add(cliente);
            }
        }  catch (Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error en los Clientes");
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }

        //TABLA
        try{
            VentaMaestros = new VentaMaestroMgr();
            TableColumn<VentaMaestro, Integer> tcIdVenta = new TableColumn<>("idVenta");
            TableColumn<VentaMaestro, String> tcCliente = new TableColumn<>("Cliente");
            TableColumn<VentaMaestro, String> tcFecha = new TableColumn<>("Fecha");
            TableColumn<VentaMaestro, Integer> tcProductos = new TableColumn<>("Productos");
            TableColumn<VentaMaestro, Float> tcTotal = new TableColumn<>("Total");

            tcIdVenta.setCellValueFactory(new PropertyValueFactory<>("idVenta"));

            tcCliente.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<VentaMaestro, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<VentaMaestro, String> dato) {
                    StringProperty spCliente = new SimpleStringProperty();
                    for (Cliente cliente: VentasComboCliente.getItems()){
                        if (cliente.getIdCliente().equals(dato.getValue().getIdCliente())){
                            spCliente.set(String.format(cliente.getNombre() + cliente.getAPaterno() + cliente.getAMaterno()));
                        }
                    }
                    return spCliente;
                }
            });
            tcFecha.setCellValueFactory(new PropertyValueFactory<>("Fecha"));
            tcProductos.setCellValueFactory(new PropertyValueFactory<>("Productos"));
            tcTotal.setCellValueFactory(new PropertyValueFactory<>("Total"));

            VentasTable.getColumns().clear();
            VentasTable.getColumns().addAll(tcIdVenta, tcCliente, tcFecha, tcProductos, tcTotal);
            VentasTable.getItems().addAll(VentaMaestros.getVentaMaestro());

        } catch (Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error en VentasMaes");
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }
        MostrarControles();
    }
    public void clickVentasTable(MouseEvent mouseEvent) {
        Object object  = VentasTable.getSelectionModel().getSelectedItem();
        if (object==null){
            return;
        }

        VentaMaestro VenMae = (VentaMaestro) object;

        VentasTextId.setText(String.format("%d", VenMae.getIdVenta()));
        VentasTextTotal.setText(String.format("%f", VenMae.getTotal()));
        VentasDatePFecha.setValue(LocalDate.parse(VenMae.getFecha()));
        VentasTextProductos.setText(String.format("%d", VenMae.getProductos()));

        //COMBOS
        for(Cliente cliente: VentasComboCliente.getItems()){
            if (cliente.getIdCliente().equals(VenMae.getIdCliente())){
                VentasComboCliente.getSelectionModel().select(cliente);
                break;
            }
        }
    }

    public void clickVentasButtonAgregar(ActionEvent actionEvent) {
        //abrir una nueva ventana para agregar los productos
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(HelloApplication.class.getResource("CarritoVentas.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Carrito de compras");
            stage.setScene(scene);

            CarritoVentasController controller = fxmlLoader.getController();
            controller.initData(VentaMaestros.nextId());
            stage.setUserData(VentaMaestros.nextId());
            stage.show();

        }catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void clickVentasButtonBorrar(ActionEvent actionEvent) {
        accion = Accion.BORRAR;
        MostrarControles();
    }

    public void clickVentasButtonAceptar(ActionEvent actionEvent) {
        Cliente cliente = VentasComboCliente.getSelectionModel().getSelectedItem();
        VentaMaestro VenMae = new VentaMaestro(Integer.parseInt(VentasTextId.getText()), cliente.getIdCliente(), VentasDatePFecha.getValue().toString(), Integer.parseInt(VentasTextProductos.getText()), Float.parseFloat(VentasTextTotal.getText()));

        try {
            switch (accion) {
                case BORRAR:
                    ListaDetalles.addAll(VentaDetalles.getFromId(VenMae.getIdVenta()));

                    for (VentaDetalle detalle: ListaDetalles) {
                        LinkedList<Almacen> almacen = Almacenes.getFromSKU(detalle.getSKU());
                        if(almacen != null){
                            for(Almacen i: almacen){
                                i.setCantidad(i.getCantidad() + detalle.getCantidad());
                                Almacenes.editarAlmacen(i);
                            }
                        }
                        VentaDetalles.borrarVentaDetalle(detalle);
                    }
                    VentaMaestros.borrarVentaMaestro(VenMae);
                    break;
            }

            VentasTable.getItems().clear();
            VentasTable.getItems().addAll(VentaMaestros.getVentaMaestro());
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

    public void clickVentasButtonCancelar(ActionEvent actionEvent) {
        accion = Accion.NAVEGAR;
        Limpiar();
        MostrarControles();
    }

    private void MostrarControles() {
        VentasTable.setDisable(true);

        VentasTextId.setDisable(true);
        VentasComboCliente.setDisable(true);
        VentasTextTotal.setDisable(true);
        VentasDatePFecha.setDisable(true);
        VentasTextProductos.setDisable(true);


        VentasButtonAceptar.setVisible(false);
        VentasButtonCancelar.setVisible(false);
        VentasButtonAgregar.setVisible(false);
        VentasButtonBorrar.setVisible(false);
        //VentasButtonEditar.setVisible(false);

        switch (accion){
            case NAVEGAR:
                VentasTable.setDisable(false);
                VentasButtonAgregar.setVisible(true);
                VentasButtonBorrar.setVisible(true);

                break;
            case AGREGAR:
                Limpiar();
                /*VentasTextId.setDisable(false);
                VentasComboCliente.setDisable(false);
                VentasTextTotal.setDisable(false);
                VentasDatePFecha.setDisable(false);
                VentasTextProductos.setDisable(false);

                VentasButtonAceptar.setVisible(true);
                VentasButtonCancelar.setVisible(true);*/
                break;
            /*case EDITAR:
                VentasComboCliente.setDisable(false);
                VentasTextTotal.setDisable(false);
                VentasDatePFecha.setDisable(false);
                VentasTextProductos.setDisable(false);

                VentasButtonAceptar.setVisible(true);
                VentasButtonCancelar.setVisible(true);
                break;*/
            case BORRAR:
                VentasButtonAceptar.setVisible(true);
                VentasButtonCancelar.setVisible(true);
                break;
        }
    }

    private void Limpiar() {
        VentasTextId.setText("");
        VentasComboCliente.getSelectionModel().clearSelection();
        VentasTextTotal.setText("");
        VentasDatePFecha.setValue(null);
        VentasTextProductos.setText("");
    }

    @FXML
    private void clickVentasButtonGoBack(ActionEvent actionEvent) {
        Stage stage = (Stage) VentasButtonGoBack.getScene().getWindow();
        stage.close();
    }

    public void clickVentasButtonReporte(ActionEvent actionEvent) {
        try{
            JasperReport jrReporte = (JasperReport) JRLoader.loadObject(new File("reportes\\Ventas.jasper"));
            DefaultTableModel datos = new DefaultTableModel();
            datos.setColumnIdentifiers(new Object[]{"idVenta", "Cliente", "Fecha", "Productos", "Total"});

            for(VentaMaestro VenMae: VentaMaestros.getVentaMaestro()){
                datos.addRow(new Object[]{VenMae.getIdVenta(), VenMae.getIdCliente(), VenMae.getFecha(), VenMae.getProductos(), VenMae.getTotal()});
            }

            Map<String,Object> parametros = new HashMap<>();
            parametros.put("TITULO", "Reporte de Ventas");
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
