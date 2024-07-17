package com.example.mykoleccionista.GUI;

import com.example.mykoleccionista.HelloApplication;
import com.example.mykoleccionista.Negocios.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
import java.util.Map;

//CONTROLADOR DE Almacen.fxml
public class AlmacenController {
    //IMAGENES
    @FXML
    public ImageView AlmacenImage;
    //TABLAS
    @FXML
    private TableView AlmacenTableProd;
    //COMBO BOX
    @FXML
    private ComboBox AlmacenComboSKU;
    //FECHAS
    @FXML
    private DatePicker AlmacenDatePFCompra;
    //SPINNERS
    @FXML
    private Spinner<Integer> AlmacenSpinnerDisponibles;
    @FXML
    private Spinner<Integer> AlmacenSpinnerCantidad;
    //BOTONES
    @FXML
    private Button AlmacenButtonAceptar;
    @FXML
    private Button AlmacenButtonCancelar;
    @FXML
    private Button AlmacenButtonGoBack;
    @FXML
    private Button AlmacenButtonAgregar;
    @FXML
    private Button AlmacenButtonEditar;
    @FXML
    private Button AlmacenButtonBorrar;
    @FXML
    private Button AlmacenButtonSKUMas;
    @FXML
    private Button AlmacenButtonReporte;
    //CAMPOS DE TEXTO
    @FXML
    private TextField AlmacenTextPrecioCompra;
    @FXML
    private TextField AlmacenTextPrecioVenta;

    // VARIABLES LOCALES
    private AlmacenMgr almacenes;
    private ProductoMgr prods;
    private Accion accion = Accion.NAVEGAR;
    SpinnerValueFactory<Integer> spCant;
    SpinnerValueFactory<Integer> spDisp;
    private String img_path = null;

    public void initialize() {

        // SPINNERS
        spCant = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,1000, 0, 1);
        AlmacenSpinnerCantidad.setValueFactory(spCant);
        spDisp = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,1000, 0, 1);
        AlmacenSpinnerDisponibles.setValueFactory(spDisp);

        // COMBO
        try {
            prods = new ProductoMgr();
            AlmacenComboSKU.getItems().clear();
            for (Producto prod: prods.getProductos()){
                AlmacenComboSKU.getItems().add(prod);
            }
        }  catch (Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error en los SKU");
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }

        // TABLA
        try{
            almacenes = new AlmacenMgr();
            TableColumn<Almacen, String> tcSKU = new TableColumn<>("SKU");
            TableColumn<Almacen, String> tcFCompra = new TableColumn<>("FCompra");
            TableColumn<Almacen, Integer> tcCantidad = new TableColumn<>("Cantidad");
            TableColumn<Almacen, Float> tcPrecioCompra = new TableColumn<>("PrecioCompra");
            TableColumn<Almacen, Float> tcPrecioVenta = new TableColumn<>("PrecioVenta");
            TableColumn<Almacen, Integer> tcDisponibles = new TableColumn<>("Disponibles");

            //Almacen(String SKU, String FCompra, int cantidad, Float precioCompra, Float precioVenta, int disponibles)

            tcSKU.setCellValueFactory(new PropertyValueFactory<>("SKU"));
            tcFCompra.setCellValueFactory(new PropertyValueFactory<>("FCompra"));
            tcCantidad.setCellValueFactory(new PropertyValueFactory<>("Cantidad"));
            tcPrecioCompra.setCellValueFactory(new PropertyValueFactory<>("PrecioCompra"));
            tcPrecioVenta.setCellValueFactory(new PropertyValueFactory<>("PrecioVenta"));
            tcDisponibles.setCellValueFactory(new PropertyValueFactory<>("Disponibles"));

            AlmacenTableProd.getColumns().clear();
            AlmacenTableProd.getColumns().addAll(tcSKU, tcFCompra, tcCantidad, tcPrecioCompra, tcPrecioVenta, tcDisponibles);
            //System.out.println(almacenes.getClientes());
            AlmacenTableProd.getItems().addAll(almacenes.getAlmacen());

        } catch (Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error en el almacen");
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }
        MostrarControles();

    }

    public void clickAlmacenTableProd(MouseEvent mouseEvent) {
        Object object  = AlmacenTableProd.getSelectionModel().getSelectedItem();
        if (object==null){
            return;
        }

        Almacen almacen = (Almacen) object;
        spCant.setValue(almacen.getCantidad());
        AlmacenSpinnerCantidad.setValueFactory(spCant);
        AlmacenTextPrecioVenta.setText(almacen.getPrecioVenta().toString());
        AlmacenTextPrecioCompra.setText(almacen.getPrecioCompra().toString());
        spDisp.setValue(almacen.getCantidad());
        AlmacenSpinnerDisponibles.setValueFactory(spDisp);
        AlmacenDatePFCompra.setValue(LocalDate.parse(almacen.getFCompra()));

        for(Object producto: AlmacenComboSKU.getItems()){
            if (((Producto) producto).getSKU().equals(almacen.getSKU())){
                AlmacenComboSKU.getSelectionModel().select(producto);
                //AlmacenTextImage.setText(((Producto) producto).getFotografia());
                img_path = ((Producto) producto).getFotografia();
                AlmacenImage.setImage(new Image("file:img\\" + img_path));
                break;
            }
        }

        AlmacenButtonEditar.setDisable(false);
        AlmacenButtonBorrar.setDisable(false);
    }

    public void clickAlmacenButtonAceptar(ActionEvent actionEvent) {
        Producto producto = (Producto) AlmacenComboSKU.getSelectionModel().getSelectedItem();
        //Almacen(String SKU, String FCompra, int cantidad, Float precioCompra, Float precioVenta, int disponibles) {
        Almacen almacen = new Almacen(producto.getSKU(), AlmacenDatePFCompra.getValue().toString(), AlmacenSpinnerCantidad.getValue(), Float.parseFloat(AlmacenTextPrecioCompra.getText()), Float.parseFloat(AlmacenTextPrecioVenta.getText()), AlmacenSpinnerDisponibles.getValue());

        try {
            switch (accion) {
                case AGREGAR:
                    almacenes.insertarAlmacen(almacen);
                    break;
                case EDITAR:
                    almacenes.editarAlmacen(almacen);
                    break;
                case BORRAR:
                    almacenes.borrarAlmacen(almacen);
                    AlmacenImage.setImage(null);
                    break;
            }

            AlmacenTableProd.getItems().clear();
            AlmacenTableProd.getItems().addAll(almacenes.getAlmacen());
            Limpiar();
        } catch (Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText(String.format("Error al %s en almacen", accion.toString().toLowerCase()));
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }

        accion = Accion.NAVEGAR;
        MostrarControles();
    }

    public void clickAlmacenButtonCancelar(ActionEvent actionEvent) {
        accion = Accion.NAVEGAR;
        Limpiar();
        MostrarControles();
    }

    public void clickAlmacenButtonAgregar(ActionEvent actionEvent) {
        accion = Accion.AGREGAR;
        MostrarControles();
    }

    public void clickAlmacenButtonEditar(ActionEvent actionEvent) {
        accion = Accion.EDITAR;
        MostrarControles();
    }

    public void clickAlmacenButtonBorrar(ActionEvent actionEvent) {
        accion = Accion.BORRAR;
        MostrarControles();
    }

    private void MostrarControles() {
        AlmacenTableProd.setDisable(true);

        AlmacenTextPrecioVenta.setDisable(true);
        AlmacenTextPrecioCompra.setDisable(true);
        AlmacenDatePFCompra.setDisable(true);
        spCant.setValue(0);
        AlmacenSpinnerCantidad.setDisable(true);
        spDisp.setValue(0);
        AlmacenSpinnerDisponibles.setDisable(true);
        AlmacenComboSKU.setDisable(true);

        AlmacenButtonAceptar.setVisible(false);
        AlmacenButtonCancelar.setVisible(false);
        AlmacenButtonAgregar.setVisible(false);
        AlmacenButtonBorrar.setVisible(false);
        AlmacenButtonBorrar.setDisable(true);
        AlmacenButtonEditar.setVisible(false);
        AlmacenButtonEditar.setDisable(true);

        switch (accion) {
            case NAVEGAR:
                AlmacenTableProd.setDisable(false);
                AlmacenButtonAgregar.setVisible(true);
                AlmacenButtonBorrar.setVisible(true);
                AlmacenButtonEditar.setVisible(true);
                break;
            case AGREGAR:
                AlmacenComboSKU.setDisable(false);
                AlmacenTextPrecioVenta.setDisable(false);
                AlmacenTextPrecioCompra.setDisable(false);
                AlmacenDatePFCompra.setDisable(false);
                AlmacenSpinnerCantidad.setDisable(false);
                AlmacenSpinnerDisponibles.setDisable(false);

                Limpiar();
                AlmacenButtonAceptar.setVisible(true);
                AlmacenButtonCancelar.setVisible(true);
                break;

            case EDITAR:
                AlmacenTextPrecioVenta.setDisable(false);
                AlmacenTextPrecioCompra.setDisable(false);
                AlmacenDatePFCompra.setDisable(false);
                AlmacenSpinnerCantidad.setDisable(false);
                AlmacenSpinnerDisponibles.setDisable(false);

                AlmacenButtonAceptar.setVisible(true);
                AlmacenButtonCancelar.setVisible(true);
                break;

            case BORRAR:
                AlmacenButtonAceptar.setVisible(true);
                AlmacenButtonCancelar.setVisible(true);
                break;
        }
    }

    private void Limpiar() {
        AlmacenTextPrecioVenta.setText("");
        AlmacenTextPrecioCompra.setText("");
        AlmacenDatePFCompra.setValue(null);
        spCant.setValue(0);
        AlmacenSpinnerCantidad.setValueFactory(spCant);
        spDisp.setValue(0);
        AlmacenSpinnerDisponibles.setValueFactory(spDisp);
        AlmacenComboSKU.getSelectionModel().clearSelection();
    }

    @FXML
    private void clickAlmacenButtonGoBack(ActionEvent actionEvent) {
        Stage stage = (Stage) AlmacenButtonGoBack.getScene().getWindow();
        stage.close();
    }

    public void clickAlmacenButtonSKUMas(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(HelloApplication.class.getResource("PABC.fxml"));
            //fxmlLoader.setController(this);
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Gestionar Productos");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clickAlmacenComboSKU(ActionEvent actionEvent) {
        if(AlmacenComboSKU.getSelectionModel().getSelectedItem() != null){
            Producto producto = (Producto) AlmacenComboSKU.getSelectionModel().getSelectedItem();
            img_path = (producto).getFotografia();
            AlmacenImage.setImage(new Image("file:img\\" + img_path));
        }

    }

    public void clickAlmacenButtonReporte(ActionEvent actionEvent) {
        try{
            JasperReport jrReporte = (JasperReport) JRLoader.loadObject(new File("reportes\\Almacen.jasper"));
            DefaultTableModel datos = new DefaultTableModel();
            datos.setColumnIdentifiers(new Object[]{"SKU", "FCompra", "Cantidad", "Precio de Compra", "Precio de Venta", "Disponibles"});

            for(Almacen almacen: almacenes.getAlmacen()){
                datos.addRow(new Object[]{almacen.getSKU(), almacen.getFCompra(), almacen.getCantidad(), almacen.getPrecioCompra(), almacen.getPrecioVenta(), almacen.getDisponibles()});
            }

            Map<String,Object> parametros = new HashMap<>();
            parametros.put("TITULO", "Reporte de Almacen");
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
