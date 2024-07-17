package com.example.mykoleccionista.GUI;

import com.example.mykoleccionista.Negocios.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

public class CarritoVentasController {
    // TABLAS
    @FXML
    private TableView<VentaDetalle> CarritoTable;
    //COMBO BOX
    @FXML
    private ComboBox<Cliente> CarritoComboCliente;
    @FXML
    private ComboBox<Almacen> CarritoComboSKU;
    //FECHA
    @FXML
    private DatePicker CarritoDatePFecha;
    //SPINNERS
    @FXML
    private Spinner<Integer> CarritoSpinnerCantidad;
    //BOTONES
    @FXML
    private Button CarritoButtonGoBack;
    @FXML
    private Button CarritoButtonAceptar;
    @FXML
    private Button CarritoButtonCancelar;
    @FXML
    private Button CarritoButtonEditar;
    @FXML
    private Button CarritoButtonQuitar;
    @FXML
    private Button CarritoButtonAgregar;
    @FXML
    private Button CarritoButtonFinCompra;
    @FXML
    private Button CarritoButtonCancCompra;
    @FXML
    private Button CarritoVentasButtonReporte;
    //CAMPOS DE TEXTO
    @FXML
    private TextField CarritoTextId;
    @FXML
    private TextField CarritoTextProductos;
    @FXML
    private TextField CarritoTextTotalCompra;
    @FXML
    private TextField CarritoTextPrecio;
    @FXML
    private TextField CarritoTextTotal;

    //VARIABLES DE LA CLASE
    private ClienteMgr Clientes;
    private AlmacenMgr Almacenes;
    private VentaMaestroMgr VentaMaestros;
    private VentaDetalleMgr VentaDetalles;
    private Accion accion = Accion.NAVEGAR;
    private int id;
    private float totalCompra = 0f;
    private SpinnerValueFactory<Integer> aux;
    private LinkedList<VentaDetalle> ListaDetalles;
    private LinkedList<Almacen> ListaCambiosAlmacen;
    private LinkedList<Almacen> ListaSKUActivos;

    public CarritoVentasController() {
    }

    void initData(int id) {
        this.id = id;

        CarritoTextId.setText(String.format("%d", this.id));
    }

    public void initialize() {
        aux = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,100,1,1);
        CarritoSpinnerCantidad.setValueFactory(aux);

        try{
            ListaDetalles = new LinkedList<>();
            ListaCambiosAlmacen = new LinkedList<>();
            ListaSKUActivos = new LinkedList<>();

            Clientes = new ClienteMgr();
            Almacenes = new AlmacenMgr();
            VentaMaestros = new VentaMaestroMgr();
            VentaDetalles = new VentaDetalleMgr();
        }catch(Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error al inicializar");
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }

        //COMBO CLIENTES
        try {
            CarritoComboCliente.getItems().clear();
            for (Cliente cliente: Clientes.getClientes()){
                CarritoComboCliente.getItems().add(cliente);
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
            /*for (Almacen alma: Almacenes.getAlmacen()){
                if(Almacenes.getFromSKU(alma.getSKU()).size() > 1){
                    ListaSKUActivos.add(getSigSalir(Almacenes.getFromSKU(alma.getSKU())));
                }else if(alma.getDisponibles() > 1){
                    ListaSKUActivos.add(alma);
                }
            }*/
            actualizarSKU();
        }  catch (Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error en los Almacenes");
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }

        //TABLA
        try {
            TableColumn<VentaDetalle, Integer> tcidVenta = new TableColumn<>("idVenta");
            TableColumn<VentaDetalle, String> tcSKU = new TableColumn<>("SKU");
            TableColumn<VentaDetalle, Integer> tcCantidad = new TableColumn<>("Cantidad");
            TableColumn<VentaDetalle, String> tcFCompra = new TableColumn<>("FCompra");

            tcidVenta.setCellValueFactory(new PropertyValueFactory<>("idVenta"));
            tcSKU.setCellValueFactory(new PropertyValueFactory<>("SKU"));
            tcCantidad.setCellValueFactory(new PropertyValueFactory<>("Cantidad"));
            tcFCompra.setCellValueFactory(new PropertyValueFactory<>("FCompra"));

            CarritoTable.getColumns().clear();
            CarritoTable.getColumns().addAll(tcidVenta, tcSKU, tcCantidad, tcFCompra);
            CarritoTable.getItems().addAll(ListaDetalles);

        }catch(Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error al inicializar tabla");
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }
        MostrarControles();
        CarritoComboCliente.setDisable(false);
        CarritoDatePFecha.setDisable(false);
        CarritoButtonAgregar.setDisable(true);
    }

    @FXML
    private void clickCarritoTable() {
        Object object  = CarritoTable.getSelectionModel().getSelectedItem();
        if (object==null){
            return;
        }

        try{
            VentaDetalle Detalle = (VentaDetalle) object;
            for(Almacen alm: ListaCambiosAlmacen){
                if (alm.getSKU().equals(Detalle.getSKU())){
                    CarritoComboSKU.getSelectionModel().select(alm);
                    break;
                }
            }

            CarritoTextPrecio.setText(String.format("%f", (CarritoComboSKU.getSelectionModel().getSelectedItem()).getPrecioVenta()));

            aux.setValue(Detalle.getCantidad());
            CarritoSpinnerCantidad.setValueFactory(aux);

            float total = Detalle.getCantidad() * (CarritoComboSKU.getSelectionModel().getSelectedItem()).getPrecioVenta();
            CarritoTextTotal.setText(String.format("%f", total));

            CarritoButtonQuitar.setVisible(true);

        }catch(Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error en click tabla");
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }

        CarritoButtonAgregar.setVisible(false);
        //CarritoButtonEditar.setVisible(true);
        CarritoButtonQuitar.setVisible(true);
        CarritoSpinnerCantidad.setDisable(true);
    }


    @FXML
    private void clickCarritoButtonAgregar(ActionEvent actionEvent) {
        accion = Accion.AGREGAR;
        MostrarControles();
    }

    @FXML
    private void clickCarritoButtonEditar(ActionEvent actionEvent) {
        /*accion = Accion.EDITAR;
        MostrarControles();*/
    }

    @FXML
    private void clickCarritoButtonQuitar(ActionEvent actionEvent) {
        accion = Accion.BORRAR;
        MostrarControles();
    }

    @FXML
    private void clickCarritoButtonAceptar(ActionEvent actionEvent) {
        Almacen almacen = CarritoComboSKU.getSelectionModel().getSelectedItem();
        VentaDetalle VenDet = new VentaDetalle(this.id, almacen.getSKU(), almacen.getFCompra(), CarritoSpinnerCantidad.getValue());
        Integer prod=0;

        switch (accion){
            case AGREGAR:
                //if(Integer.parseInt(CarritoTextProductos.getText()) != 0){
                    //quitar de la lista de activos
                    for (int i=0; i<ListaSKUActivos.size(); i++){
                        if(ListaSKUActivos.get(i).toString().equals(almacen.toString())){
                            ListaSKUActivos.remove(i);
                            break;
                        }
                    }

                    almacen.setDisponibles(almacen.getDisponibles() - VenDet.getCantidad());

                    ListaDetalles.add(VenDet);
                    ListaCambiosAlmacen.add(almacen);

                    for (VentaDetalle ven: ListaDetalles){
                        prod += ven.getCantidad();
                    }
                    this.totalCompra += Float.parseFloat(CarritoTextTotal.getText());
                //}
                break;
            case BORRAR:
                //quitar de la lista de cambios
                for (int i=0; i<ListaCambiosAlmacen.size(); i++){
                    if(ListaCambiosAlmacen.get(i).toString().equals(almacen.toString())){
                        ListaCambiosAlmacen.remove(i);
                        break;
                    }
                }
                almacen.setDisponibles(almacen.getDisponibles()+VenDet.getCantidad());
                //agregar a la lista de activos
                if(!ListaSKUActivos.contains(almacen)){
                    ListaSKUActivos.add(almacen);
                }

                //quitar de la lista de detalles
                for (int i=0; i<ListaDetalles.size(); i++){
                    if(ListaDetalles.get(i).toString().equals(VenDet.toString())){
                        ListaDetalles.remove(i);
                        break;
                    }
                }



                for (VentaDetalle ven: ListaDetalles){
                    prod += ven.getCantidad();
                }

                this.totalCompra -= Float.parseFloat(CarritoTextTotal.getText());
                break;
        }
        CarritoTextProductos.setText(prod.toString());
        CarritoTextTotalCompra.setText(String.format("%f", this.totalCompra));

        Limpiar();

        //ACTUALIZAR COMBO SKU
        actualizarSKU();

        //ACTUALIZAR TABLA
        actualizarTabla();
        accion = Accion.NAVEGAR;
        MostrarControles();

    }

    @FXML
    private void clickCarritoButtonCancelar(ActionEvent actionEvent) {
        Limpiar();
        accion = Accion.NAVEGAR;
        MostrarControles();
    }

    @FXML
    private void clickCarritoButtonFinCompra(ActionEvent actionEvent) {
        try{
            if(ListaDetalles != null){
                Cliente cliente = CarritoComboCliente.getSelectionModel().getSelectedItem();
                VentaMaestro venmae = new VentaMaestro(this.id, cliente.getIdCliente(),  CarritoDatePFecha.getValue().toString(), Integer.parseInt(CarritoTextProductos.getText()), Float.parseFloat(CarritoTextTotalCompra.getText()));

                VentaMaestros.insertarVentaMaestro(venmae);
                for(VentaDetalle i: ListaDetalles){
                    VentaDetalles.insertarVentaDetalle(i);
                }
            }

            if(ListaCambiosAlmacen != null){
                for(Almacen i: ListaCambiosAlmacen){
                    Almacenes.editarAlmacen(i);
                }
            }

            Stage stage = (Stage)  CarritoButtonGoBack.getScene().getWindow();
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
    private void clickCarritoButtonCancCompra(ActionEvent actionEvent) {
        Stage stage = (Stage)  CarritoButtonGoBack.getScene().getWindow();
        stage.close();
    }

    private void MostrarControles() {
        CarritoTable.setDisable(true);
        CarritoComboSKU.setDisable(true);
        CarritoTextPrecio.setDisable(true);
        CarritoSpinnerCantidad.setDisable(true);
        CarritoTextTotal.setDisable(true);

        CarritoButtonAceptar.setVisible(false);
        CarritoButtonCancelar.setVisible(false);
        CarritoButtonAgregar.setVisible(false);
        CarritoButtonEditar.setVisible(false);
        CarritoButtonQuitar.setVisible(false);

        switch (accion){
            case NAVEGAR:
                CarritoTable.setDisable(false);
                CarritoButtonAgregar.setVisible(true);

                break;
            case AGREGAR:
                CarritoComboSKU.setDisable(false);
                CarritoTextPrecio.setDisable(false);
                CarritoTextTotal.setDisable(false);

                CarritoButtonAceptar.setVisible(true);
                CarritoButtonCancelar.setVisible(true);
                break;
            /*case EDITAR:
                CarritoTextPrecio.setDisable(false);
                CarritoTextTotal.setDisable(false);

                CarritoButtonAceptar.setVisible(true);
                CarritoButtonCancelar.setVisible(true);
                break;*/
            case BORRAR:
                CarritoButtonAceptar.setVisible(true);
                CarritoButtonCancelar.setVisible(true);
                break;
        }
    }


    private void Limpiar() {
        CarritoComboSKU.getSelectionModel().clearSelection();
        CarritoTextPrecio.setText("");
        aux.setValue(0);
        CarritoSpinnerCantidad.setValueFactory(aux);
        CarritoSpinnerCantidad.setDisable(true);
        CarritoTextTotal.setText("");
    }

    @FXML
    private void clickCarritoButtonGoBack(ActionEvent actionEvent) {
        Stage stage = (Stage)  CarritoButtonGoBack.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void clickComboSKU(ActionEvent actionEvent) {
        if(CarritoComboSKU.getSelectionModel().getSelectedItem() != null){
            Almacen almacen = CarritoComboSKU.getSelectionModel().getSelectedItem();
            CarritoTextPrecio.setText(String.format("%f", almacen.getPrecioVenta()));
            aux = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, almacen.getDisponibles(), 1);
            CarritoSpinnerCantidad.setValueFactory(aux);
            CarritoTextTotal.setText(String.format("%f", CarritoSpinnerCantidad.getValue() * almacen.getPrecioVenta()));
            CarritoSpinnerCantidad.setDisable(false);
            CarritoComboSKU.setDisable(true);
        }
    }

    @FXML
    private void clickCarritoDatePFecha(ActionEvent actionEvent) {
        MostrarControles();
        CarritoComboCliente.setDisable(true);
        CarritoDatePFecha.setDisable(true);
        CarritoButtonAgregar.setDisable(false);
        actualizarSKU();
    }

    @FXML
    private void clickCarritoSpinnerCantidad(MouseEvent mouseEvent) {
        //capear cantidad
        if(CarritoSpinnerCantidad.getValue() != null){
            CarritoTextTotal.setText(String.format("%f", CarritoSpinnerCantidad.getValue() * Float.parseFloat(CarritoTextPrecio.getText())));
        }

    }

    private void actualizarTabla(){
        CarritoTable.getItems().clear();
        CarritoTable.getItems().addAll(ListaDetalles);
    }

    private void actualizarSKU(){
        try{
            CarritoComboSKU.getItems().clear();
            for (Almacen alma: Almacenes.getAlmacen()){
                if(!ListaSKUActivos.contains(getSigSalir(Almacenes.getFromSKU(alma.getSKU()))) && !ListaCambiosAlmacen.contains(getSigSalir(Almacenes.getFromSKU(alma.getSKU())))){
                    ListaSKUActivos.add(alma);
                }
            }
            CarritoComboSKU.getItems().addAll(ListaSKUActivos);

        }catch(Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error al actualizar SKUs");
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }
    }

    public Almacen getSigSalir(LinkedList<Almacen> almacen) throws Exception{
        if(almacen != null){
            LocalDate d1 = LocalDate.now();
            LocalDate d2;
            Almacen sig = null;
            for(Almacen alma: almacen){
                d2 = LocalDate.parse(alma.getFCompra());
                if(d2.isBefore(d1) && alma.getDisponibles()>1 ){
                    d1 = d2;
                    sig = alma;
                }
            }
            return sig;
        }
        return null;
    }

    public void clickCarritoVentasButtonReporte(ActionEvent actionEvent) {
        try{
            JasperReport jrReporte = (JasperReport) JRLoader.loadObject(new File("reportes\\CarritoVentas.jasper"));
            DefaultTableModel datos = new DefaultTableModel();
            datos.setColumnIdentifiers(new Object[]{"Venta", "SKU", "FCompra", "Cantidad"});

            for(VentaDetalle VenDet: ListaDetalles){
                datos.addRow(new Object[]{VenDet.getIdVenta(), VenDet.getSKU(), VenDet.getFCompra(), VenDet.getCantidad()});
            }

            Map<String,Object> parametros = new HashMap<>();
            parametros.put("TITULO", "Ticket de Venta");
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
