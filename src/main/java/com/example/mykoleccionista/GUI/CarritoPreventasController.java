package com.example.mykoleccionista.GUI;

import com.example.mykoleccionista.Negocios.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

public class CarritoPreventasController {
    //TABLA
    @FXML
    private TableView<PreventaDetalle> CarritoPrevTable;
    //COMBO BOX
    @FXML
    private ComboBox<Producto> CarritoPrevComboSKU;
    @FXML
    private ComboBox<Cliente> CarritoPrevComboCliente;
    //FECHAS
    @FXML
    private DatePicker CarritoPrevDatePFecha;
    //SPINNERS
    @FXML
    private Spinner<Integer> CarritoPrevSpinnerCantidad;
    //BOTONES
    @FXML
    private Button CarritoPrevButtonGoBack;
    @FXML
    private Button CarritoPrevButtonAceptar;
    @FXML
    private Button CarritoPrevButtonCancelar;
    @FXML
    private Button CarritoPrevButtonEditar;
    @FXML
    private Button CarritoPrevButtonQuitar;
    @FXML
    private Button CarritoPrevButtonAgregar;
    @FXML
    private Button CarritoPrevButtonFinCompra;
    @FXML
    private Button CarritoPrevButtonCancCompra;
    @FXML
    private Button CarritoPrevButtonReporte;
    //CAMPOS DE TEXTO
    @FXML
    private TextField CarritoPrevTextId;
    @FXML
    private TextField CarritoPrevTextProductos;
    @FXML
    private TextField CarritoPrevTextTotalCompra;
    @FXML
    private TextField CarritoPrevTextPrecio;
    @FXML
    private TextField CarritoPrevTextTotal;

    //VARIABLES DE LA CLASE
    private ClienteMgr Clientes;
    private ProductoMgr Productos;
    private PreventaMaestroMgr PreventaMaestros;
    private PreventaDetalleMgr PreventaDetalles;
    private Accion accion = Accion.NAVEGAR;
    private int id;
    private float totalCompra = 0f;
    private SpinnerValueFactory<Integer> aux;
    private LinkedList<PreventaDetalle> ListaDetalles;
    private LinkedList<Producto> ListaDisponibles;

    void initData(int id) {
        this.id = id;

        CarritoPrevTextId.setText(String.format("%d", this.id));
    }

    public void initialize() {
        aux = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,100,1,1);
        CarritoPrevSpinnerCantidad.setValueFactory(aux);

        try{
            ListaDetalles = new LinkedList<>();
            ListaDisponibles = new LinkedList<>();

            Clientes = new ClienteMgr();
            Productos = new ProductoMgr();
            PreventaMaestros = new PreventaMaestroMgr();
            PreventaDetalles = new PreventaDetalleMgr();
        }catch(Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error al inicializar");
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }

        //COMBO CLIENTES
        try {
            CarritoPrevComboCliente.getItems().clear();
            for (Cliente cliente: Clientes.getClientes()){
                CarritoPrevComboCliente.getItems().add(cliente);
            }
        }  catch (Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error en los Clientes");
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }

        //COMBO SKU
        try {
            //actualizarSKU();
        }  catch (Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error en los Productos");
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }

        //TABLA
        try {
            TableColumn<PreventaDetalle, Integer> tcidPreventa = new TableColumn<>("idPreventa");
            TableColumn<PreventaDetalle, String> tcSKU = new TableColumn<>("SKU");
            TableColumn<PreventaDetalle, Integer> tcCantidad = new TableColumn<>("Cantidad");
            TableColumn<PreventaDetalle, Float> tcCosto = new TableColumn<>("Costo");

            tcidPreventa.setCellValueFactory(new PropertyValueFactory<>("idPreventa"));
            tcSKU.setCellValueFactory(new PropertyValueFactory<>("SKU"));
            tcCantidad.setCellValueFactory(new PropertyValueFactory<>("Cantidad"));
            tcCosto.setCellValueFactory(new PropertyValueFactory<>("Costo"));

            CarritoPrevTable.getColumns().clear();
            CarritoPrevTable.getColumns().addAll(tcidPreventa, tcSKU, tcCantidad, tcCosto);
            CarritoPrevTable.getItems().addAll(ListaDetalles);

        }catch(Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error al inicializar tabla");
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }
        MostrarControles();
        CarritoPrevComboCliente.setDisable(false);
        CarritoPrevDatePFecha.setDisable(false);
        CarritoPrevButtonAgregar.setDisable(true);
    }


    @FXML
    private void clickCarritoTable(MouseEvent mouseEvent) {
        Object object  = CarritoPrevTable.getSelectionModel().getSelectedItem();
        if (object==null){
            return;
        }

        try{
            PreventaDetalle Detalle = (PreventaDetalle) object;
            for(Producto prod: Productos.getProductos()){
                if (prod.getSKU().equals(Detalle.getSKU())){
                    CarritoPrevComboSKU.getSelectionModel().select(prod);
                    break;
                }
            }

            CarritoPrevTextPrecio.setText(String.format("%f", Detalle.getCosto()));

            aux.setValue(Detalle.getCantidad());
            CarritoPrevSpinnerCantidad.setValueFactory(aux);

            float total = Detalle.getCantidad() * Detalle.getCosto();
            CarritoPrevTextTotal.setText(String.format("%f", total));

            CarritoPrevButtonQuitar.setVisible(true);

        }catch(Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error en click tabla");
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }
        CarritoPrevButtonAgregar.setVisible(false);
        CarritoPrevButtonQuitar.setVisible(true);
        CarritoPrevSpinnerCantidad.setDisable(true);
    }

    @FXML
    private void clickCarritoPrevButtonAgregar(ActionEvent actionEvent) {
        accion = Accion.AGREGAR;
        MostrarControles();
    }

    @FXML
    private void clickCarritoPrevButtonEditar(ActionEvent actionEvent) {
    }

    @FXML
    private void clickCarritoPrevButtonQuitar(ActionEvent actionEvent) {
        accion = Accion.BORRAR;
        MostrarControles();
    }

    @FXML
    private void clickCarritoPrevButtonAceptar(ActionEvent actionEvent) {
        Producto produ = CarritoPrevComboSKU.getSelectionModel().getSelectedItem();
        PreventaDetalle preDet = new PreventaDetalle(this.id, produ.getSKU(), CarritoPrevSpinnerCantidad.getValue(), Float.parseFloat(CarritoPrevTextPrecio.getText()));
        Integer prod=0;

        switch (accion){
            case AGREGAR:
                ListaDetalles.add(preDet);

                for (PreventaDetalle prev: ListaDetalles){
                    prod += prev.getCantidad();
                }

                this.totalCompra += Float.parseFloat(CarritoPrevTextTotal.getText());
                break;
            case BORRAR:
                //quitar de la lista de detalles
                for (int i=0; i<ListaDetalles.size(); i++){
                    if(ListaDetalles.get(i).toString().equals(preDet.toString())){
                        ListaDetalles.remove(i);
                        break;
                    }
                }

                for (PreventaDetalle pre: ListaDetalles){
                    prod += pre.getCantidad();
                }

                this.totalCompra -= Float.parseFloat(CarritoPrevTextTotal.getText());
                break;
        }
        CarritoPrevTextProductos.setText(prod.toString());
        CarritoPrevTextTotalCompra.setText(String.format("%f", this.totalCompra));

        Limpiar();

        //ACTUALIZAR COMBO SKU
        actualizarSKU();

        //ACTUALIZAR TABLA
        actualizarTabla();
        accion = Accion.NAVEGAR;
        MostrarControles();
    }

    @FXML
    private void clickCarritoPrevButtonCancelar(ActionEvent actionEvent) {
        Limpiar();
        accion = Accion.NAVEGAR;
        MostrarControles();
    }

    @FXML
    private void clickCarritoPrevButtonFinCompra(ActionEvent actionEvent) {
        try{
            if(ListaDetalles != null){
                Cliente cliente = CarritoPrevComboCliente.getSelectionModel().getSelectedItem();
                PreventaMaestro premae = new PreventaMaestro(this.id, CarritoPrevDatePFecha.getValue().toString(), Integer.parseInt(CarritoPrevTextProductos.getText()), Float.parseFloat(CarritoPrevTextTotalCompra.getText()), 1, cliente.getIdCliente());

                PreventaMaestros.insertarPreventaMaestro(premae);
                for(PreventaDetalle i: ListaDetalles){
                    PreventaDetalles.insertarPreventaDetalle(i);
                }
            }

            Stage stage = (Stage)  CarritoPrevButtonGoBack.getScene().getWindow();
            stage.close();
        }catch(Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText(String.format("Error al %s los detalles", accion.toString().toLowerCase()));
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }
    }

    @FXML
    private void clickCarritoPrevButtonCancCompra(ActionEvent actionEvent) {
        Stage stage = (Stage)  CarritoPrevButtonGoBack.getScene().getWindow();
        stage.close();
    }

    private void MostrarControles() {
        CarritoPrevTable.setDisable(true);
        CarritoPrevComboSKU.setDisable(true);
        CarritoPrevTextPrecio.setDisable(true);
        CarritoPrevSpinnerCantidad.setDisable(true);
        CarritoPrevTextTotal.setDisable(true);

        CarritoPrevButtonAceptar.setVisible(false);
        CarritoPrevButtonCancelar.setVisible(false);
        CarritoPrevButtonAgregar.setVisible(false);
        CarritoPrevButtonEditar.setVisible(false);
        CarritoPrevButtonQuitar.setVisible(false);

        switch (accion){
            case NAVEGAR:
                CarritoPrevTable.setDisable(false);
                CarritoPrevButtonAgregar.setVisible(true);

                break;
            case AGREGAR:
                CarritoPrevComboSKU.setDisable(false);
                CarritoPrevTextPrecio.setDisable(false);
                CarritoPrevTextTotal.setDisable(false);

                CarritoPrevButtonAceptar.setVisible(true);
                CarritoPrevButtonCancelar.setVisible(true);
                break;
            /*case EDITAR:
                CarritoTextPrecio.setDisable(false);
                CarritoTextTotal.setDisable(false);

                CarritoButtonAceptar.setVisible(true);
                CarritoButtonCancelar.setVisible(true);
                break;*/
            case BORRAR:
                CarritoPrevButtonAceptar.setVisible(true);
                CarritoPrevButtonCancelar.setVisible(true);
                break;
        }
    }

    private void Limpiar() {
        CarritoPrevComboSKU.getSelectionModel().clearSelection();
        CarritoPrevTextPrecio.setText("");
        aux.setValue(0);
        CarritoPrevSpinnerCantidad.setValueFactory(aux);
        CarritoPrevSpinnerCantidad.setDisable(true);
        CarritoPrevTextTotal.setText("");
    }

    @FXML
    private void clickCarritoPrevButtonGoBack(ActionEvent actionEvent) {
        Stage stage = (Stage) CarritoPrevButtonGoBack.getScene().getWindow();
        stage.close();

    }

    @FXML
    private void clickCarritoPrevTextPrecio(ActionEvent actionEvent) {
        if(!Objects.equals(CarritoPrevTextPrecio.getText(), "")){
            CarritoPrevSpinnerCantidad.setDisable(false);
            CarritoPrevTextTotal.setText(String.format("%f", CarritoPrevSpinnerCantidad.getValue() * Float.parseFloat(CarritoPrevTextPrecio.getText())));
        }
    }

    @FXML
    private void clickComboPrevSKU(ActionEvent actionEvent) {
    }

    @FXML
    private void clickCarritoPrevDatePFecha(ActionEvent actionEvent) {
        if(CarritoPrevDatePFecha.getValue() == null){
            return;
        }

        int disp = 0;

        try{
            for (Producto prod: Productos.getProductos()) {
                LocalDate d1 = CarritoPrevDatePFecha.getValue();
                LocalDate d2 = LocalDate.parse(prod.getFDisponibilidad());
                //si la fecha de la preventa es después de la fecha de disponibilidad no se puede hacer una preventa
                if (d1.isBefore(d2)) {
                    disp +=1;
                    ListaDisponibles.add(prod);
                }
            }

        }catch(Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error al consultar productos en la fecha seleccionada");
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }

        Dialog dialog = new Dialog();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.managedProperty().bind(closeButton.visibleProperty());
        closeButton.setVisible(false);
        dialog.setContentText(String.format("Hay %d productos disponibles para preventa en esa fecha%n", disp));
        dialog.showAndWait();

        if(disp > 0){
            MostrarControles();
            CarritoPrevComboCliente.setDisable(true);
            CarritoPrevDatePFecha.setDisable(true);
            CarritoPrevButtonAgregar.setDisable(false);
            actualizarSKU();
        }else{
            CarritoPrevDatePFecha.setValue(null);
        }
    }

    @FXML
    private void clickCarritoPrevSpinnerCantidad(MouseEvent mouseEvent) {
        if(CarritoPrevSpinnerCantidad.getValue() != null){
            CarritoPrevTextTotal.setText(String.format("%f", CarritoPrevSpinnerCantidad.getValue() * Float.parseFloat(CarritoPrevTextPrecio.getText())));
        }
    }

    private void actualizarTabla(){
        CarritoPrevTable.getItems().clear();
        CarritoPrevTable.getItems().addAll(ListaDetalles);
    }

    @FXML
    private void actualizarSKU(){
        try{
            boolean flag = false;
            CarritoPrevComboSKU.getItems().clear();
            //CarritoPrevComboSKU.getItems().addAll(ListaDisponibles);
            if(ListaDetalles  != null ) {
                for(Producto prod: ListaDisponibles){
                    //revisar si está en la lista de detalles
                    for(PreventaDetalle predet: ListaDetalles){
                        if(predet.getSKU().equals(prod.getSKU())){
                            flag=true;
                            break;
                        }
                    }

                    if(!flag){
                        CarritoPrevComboSKU.getItems().add(prod);
                    }
                    flag = false;
                }
            }else{
                CarritoPrevComboSKU.getItems().addAll(ListaDisponibles);
            }

        }catch(Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error al actualizar SKUs");
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }
    }

    public void clickCarritoPrevButtonReporte(ActionEvent actionEvent) {
        try{
            JasperReport jrReporte = (JasperReport) JRLoader.loadObject(new File("reportes\\CarritoPreventas.jasper"));
            DefaultTableModel datos = new DefaultTableModel();
            datos.setColumnIdentifiers(new Object[]{"Preventa", "SKU", "Cantidad", "Costo"});

            for(PreventaDetalle PreDet: ListaDetalles){
                datos.addRow(new Object[]{PreDet.getIdPreventa(), PreDet.getSKU(), PreDet.getCantidad(), PreDet.getCosto()});
            }

            Map<String,Object> parametros = new HashMap<>();
            parametros.put("TITULO", "Ticket de Preventa");
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
