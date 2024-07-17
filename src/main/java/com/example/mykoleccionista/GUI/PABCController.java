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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

//CONTROLADOR DE PABC.fxml
public class PABCController {
    //IMAGENES
    @FXML
    private ImageView PABCimage;
    //TABLA
    @FXML
    private TableView PABCtableProd;
    //COMBOBOX
    @FXML
    private ComboBox PABCcomboEstado;
    @FXML
    private ComboBox PABCcomboTipo;
    //FECHAS
    @FXML
    private DatePicker PABCDatePFDisp;
    //SPINNERS
    @FXML
    private Spinner<Double> PABCSpinnerCalif;
    //BOTONES
    @FXML
    private Button PABCbuttonAgregar;
    @FXML
    private Button PABCbuttonEditar;
    @FXML
    private Button PABCbuttonBorrar;
    @FXML
    private Button PABCbuttonAceptar;
    @FXML
    private Button PABCbuttonCancelar;
    @FXML
    private Button PABCbuttonGoBack;
    @FXML
    private Button PABCButtonCargar;
    @FXML
    private Button PABCbuttonMasEstado;
    @FXML
    private Button PABCbuttonMasTipo;
    @FXML
    private Button PABCButtonReporte;
    //CAMPOS DE TEXTO
    @FXML
    private TextField PABCtextSKU;
    @FXML
    private TextField PABCtextNombre;
    @FXML
    private TextField PABCtextDesc;

    //VARIABLES DE LA CLASE
    private TipoEstadoMgr tipoEdos;
    private TipoProductoMgr tipoProds;
    private ProductoMgr productos;
    private Accion accion = Accion.NAVEGAR;
    private SpinnerValueFactory<Double> aux;
    private String img_path = null;

    public void initialize(){
        aux = new SpinnerValueFactory.DoubleSpinnerValueFactory(0,5, 0, 0.1);
        PABCSpinnerCalif.setValueFactory(aux);

        //COMBO TIPO ESTADO
        try {
            tipoEdos = new TipoEstadoMgr();
            PABCcomboEstado.getItems().clear();
            for (TipoEstado tipoedo: tipoEdos.getTipoEstado()){
                PABCcomboEstado.getItems().add(tipoedo);
            }
        }  catch (Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error en los TipoEstado");
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }

        //COMBO TIPO DE PRODUCTO
        try {
            tipoProds = new TipoProductoMgr();
            PABCcomboTipo.getItems().clear();
            for (TipoProducto tipoprod: tipoProds.getTipoProducto()){
                PABCcomboTipo.getItems().add(tipoprod);
            }
        }  catch (Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error en los TipoProducto");
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }

        //TABLA
        try{
            productos = new ProductoMgr();
            TableColumn<Producto, String> tcSKU = new TableColumn<>("SKU");
            TableColumn<Producto, String> tcNombre = new TableColumn<>("Nombre");
            TableColumn<Producto, String> tcDescripcion = new TableColumn<>("Descripcion");
            TableColumn<Producto, String> tcFotografia = new TableColumn<>("Fotografia");
            TableColumn<Producto, Float> tcCalificacion = new TableColumn<>("Calificacion");
            TableColumn<Producto, String> tcFDisponibilidad = new TableColumn<>("FDisponibilidad");
            TableColumn<Producto, String> tcTipoProducto = new TableColumn<>("TipoProducto");
            TableColumn<Producto, String> tcTipoEstado = new TableColumn<>("TipoEstado");


            tcSKU.setCellValueFactory(new PropertyValueFactory<>("SKU"));
            tcNombre.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
            tcDescripcion.setCellValueFactory(new PropertyValueFactory<>("Descripcion"));
            tcFotografia.setCellValueFactory(new PropertyValueFactory<>("Fotografia"));
            tcCalificacion.setCellValueFactory(new PropertyValueFactory<>("Calificacion"));
            tcFDisponibilidad.setCellValueFactory(new PropertyValueFactory<>("FDisponibilidad"));

            tcTipoProducto.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Producto, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Producto, String> dato) {
                    StringProperty spTipoProd = new SimpleStringProperty();
                    for (Object tipoprod: PABCcomboTipo.getItems()){
                        if (((TipoProducto) tipoprod).getId().equals(dato.getValue().getTipoProducto())){
                            spTipoProd.set(((TipoProducto) tipoprod).getDescripcion());
                        }
                    }
                    return spTipoProd;
                }
            });

            tcTipoEstado.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Producto, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Producto, String> dato) {
                    StringProperty spTipoEdo = new SimpleStringProperty();
                    for (Object tipoedo: PABCcomboEstado.getItems()){
                        if (((TipoEstado) tipoedo).getId().equals(dato.getValue().getTipoEstado())){
                            spTipoEdo.set(((TipoEstado) tipoedo).getDescripcion());
                        }
                    }
                    return spTipoEdo;
                }
            });

            PABCtableProd.getColumns().clear();
            PABCtableProd.getColumns().addAll(tcSKU, tcNombre, tcDescripcion, tcFotografia, tcCalificacion, tcFDisponibilidad,tcTipoProducto, tcTipoEstado);
            PABCtableProd.getItems().addAll(productos.getProductos());

        } catch (Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error en los Productos");
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }
        MostrarControles();
    }

    @FXML
    private void clickTablaClientes(MouseEvent mouseEvent) {
        Object object  = PABCtableProd.getSelectionModel().getSelectedItem();
        if (object==null){
            return;
        }
        Producto producto = (Producto) object;
        PABCtextSKU.setText(producto.getSKU());
        PABCtextNombre.setText(producto.getNombre());
        PABCtextDesc.setText(producto.getDescripcion());
        aux.setValue(producto.getCalificacion().doubleValue());
        PABCSpinnerCalif.setValueFactory(aux);
        PABCDatePFDisp.setValue(LocalDate.parse(producto.getFDisponibilidad()));

        for(Object tipoprod: PABCcomboTipo.getItems()){
            if (((TipoProducto) tipoprod).getId().equals(producto.getTipoProducto())){
                PABCcomboTipo.getSelectionModel().select(tipoprod);
                break;
            }
        }

        for(Object tipoedo: PABCcomboEstado.getItems()){
            if (((TipoEstado) tipoedo).getId().equals(producto.getTipoEstado())){
                PABCcomboEstado.getSelectionModel().select(tipoedo);
                break;
            }
        }

        img_path = producto.getFotografia();
        PABCimage.setImage(new Image("file:img\\" + img_path));

    }

    @FXML
    private void clickPABCbuttonAgregar(ActionEvent actionEvent) {
        accion = Accion.AGREGAR;
        MostrarControles();
    }

    @FXML
    private void clickPABCbuttonEditar(ActionEvent actionEvent) {
        accion = Accion.EDITAR;
        MostrarControles();
    }

    @FXML
    private void clickPABCbuttonBorrar(ActionEvent actionEvent) {
        accion = Accion.BORRAR;
        MostrarControles();
    }

    @FXML
    private void clickPABCbuttonAceptar(ActionEvent actionEvent) {
        TipoProducto tipoprod = (TipoProducto) PABCcomboTipo.getSelectionModel().getSelectedItem();
        TipoEstado tipoedo = (TipoEstado) PABCcomboEstado.getSelectionModel().getSelectedItem();
        //Producto(String SKU, String nombre, String descripcion, String fotografia, Float calificacion, String FDisponibilidad, int tipoProducto, int tipoEstado)
        Producto producto;
        if(img_path != null){
            producto = new Producto(PABCtextSKU.getText(), PABCtextNombre.getText(), PABCtextDesc.getText(), img_path, PABCSpinnerCalif.getValue().floatValue(), PABCDatePFDisp.getValue().toString(), tipoprod.getId(), tipoedo.getId());
        }else{
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("No olvides cargar la imagen");
            msg.showAndWait();
            return;
        }


        try {
            switch (accion) {
                case AGREGAR:
                    productos.insertarProducto(producto);
                    img_path = null;
                    break;
                case EDITAR:
                    productos.editarProducto(producto);
                    img_path = null;
                    break;
                case BORRAR:
                    File temp = new File(img_path);
                    if(temp.exists()){
                        temp.delete();
                    }
                    productos.borrarProducto(producto);
                    img_path = null;
                    break;
            }

            PABCtableProd.getItems().clear();
            PABCtableProd.getItems().addAll(productos.getProductos());
            Limpiar();
        } catch (Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText(String.format("Error al %s un Producto", accion.toString().toLowerCase()));
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }

        accion = Accion.NAVEGAR;
        MostrarControles();
    }

    @FXML
    private void clickPABCbuttonCancelar(ActionEvent actionEvent) {
        accion = Accion.NAVEGAR;
        Limpiar();
        MostrarControles();
    }

    private void MostrarControles(){
        PABCtableProd.setDisable(true);
        PABCtextSKU.setDisable(true);
        PABCtextNombre.setDisable(true);
        PABCtextDesc.setDisable(true);
        PABCButtonCargar.setDisable(true);
        PABCSpinnerCalif.setDisable(true);
        PABCDatePFDisp.setDisable(true);
        PABCcomboEstado.setDisable(true);
        PABCcomboTipo.setDisable(true);

        PABCButtonCargar.setVisible(false);
        PABCbuttonAceptar.setVisible(false);
        PABCbuttonCancelar.setVisible(false);
        PABCbuttonAgregar.setVisible(false);
        PABCbuttonBorrar.setVisible(false);
        PABCbuttonEditar.setVisible(false);


        switch (accion){
            case NAVEGAR:
                PABCtableProd.setDisable(false);
                PABCbuttonAgregar.setVisible(true);
                PABCbuttonBorrar.setVisible(true);
                PABCbuttonEditar.setVisible(true);
                PABCButtonCargar.setVisible(false);
                break;
            case AGREGAR:
                Limpiar();
                PABCtextSKU.setDisable(false);
            case EDITAR:
                PABCtextNombre.setDisable(false);
                PABCtextDesc.setDisable(false);
                PABCButtonCargar.setDisable(false);
                PABCButtonCargar.setVisible(true);
                PABCSpinnerCalif.setDisable(false);
                PABCDatePFDisp.setDisable(false);
                PABCcomboEstado.setDisable(false);
                PABCcomboTipo.setDisable(false);
            case BORRAR:
                PABCbuttonAceptar.setVisible(true);
                PABCbuttonCancelar.setVisible(true);
        }
    }

    private void Limpiar(){
        PABCtextSKU.setText("");
        PABCtextNombre.setText("");
        aux.setValue(0d);
        PABCSpinnerCalif.setValueFactory(aux);
        PABCDatePFDisp.setValue(null);
        PABCcomboEstado.getSelectionModel().clearSelection();
        PABCcomboTipo.getSelectionModel().clearSelection();
    }

    @FXML
    private void clickPABCbuttonGoBack(ActionEvent actionEvent) {
        Stage stage = (Stage) PABCbuttonGoBack.getScene().getWindow();
        stage.close();
    }



    @FXML
    private void clickPABCbuttonMasEstado(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(HelloApplication.class.getResource("ListTE.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Gestionar TipoEstado");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clickPABCbuttonMasTipo(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(HelloApplication.class.getResource("ListTP.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Gestionar TipoProducto");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clickPABCButtonCargar(ActionEvent actionEvent) {
        try {
            File dirImages = new File("img");
            if (!dirImages.exists()){
                dirImages.mkdirs();
            }

            FileChooser fcArchivo = new FileChooser();
            fcArchivo.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Todos los Archivos", "*.*"),
                    new FileChooser.ExtensionFilter("Imagen PNG", "*.png"), new FileChooser.ExtensionFilter("Imagen JPG", "*.jpg")
            );
            File imageFile = fcArchivo.showOpenDialog(null);
            if (imageFile==null){
                return;
            }

            File imgCpy = new File(Paths.get(dirImages.getAbsolutePath(), imageFile.getName()).toString());

            Files.copy(Path.of(imageFile.getAbsolutePath()), new FileOutputStream(imgCpy));

            String[] arrOfStr = imgCpy.getPath().split("\\\\");

            for (String a : arrOfStr){
                if(a.contains(".png") || a.contains(".jpg")){
                    img_path = a;
                }
            }

            PABCimage.setImage(new Image(imgCpy.getAbsolutePath()));
        } catch (Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error al cargar imagen");
            msg.showAndWait();
        }
    }

    public void clickPABCButtonReporte(ActionEvent actionEvent) {
        try{
            JasperReport jrReporte = (JasperReport) JRLoader.loadObject(new File("reportes\\Productos.jasper"));
            DefaultTableModel datos = new DefaultTableModel();
            datos.setColumnIdentifiers(new Object[]{"SKU", "Nombre", "Descripcion", "Fotografia", "Calificacion", "FDisponibilidad", "TipoProducto", "TipoEstado"});

            //datos.addRow(new Object[]{1,2,3,4,5,6});
            //ClienteMgr clien = this.Negocios.Cliente();

            String tedo = "";
            String tprod = "";
            for(Producto producto: productos.getProductos()){
                for(TipoEstado edo: tipoEdos.getTipoEstado()){
                    if(edo.getId() == producto.getTipoEstado()){
                        tedo = edo.getDescripcion();
                    }
                }

                for(TipoProducto prod: tipoProds.getTipoProducto()){
                    if(prod.getId() == producto.getTipoEstado()){
                        tprod = prod.getDescripcion();
                    }
                }

                datos.addRow(new Object[]{producto.getSKU(), producto.getNombre(), producto.getDescripcion(), producto.getFotografia(), producto.getCalificacion(), producto.getFDisponibilidad(), tprod, tedo});
            }

            Map<String,Object> parametros = new HashMap<>();
            parametros.put("TITULO", "Reporte de Productos");
            parametros.put("FECHA", LocalDate.now().toString());

            JasperPrint jpReporte = JasperFillManager.fillReport(jrReporte, parametros, new JRTableModelDataSource(datos));

            JasperViewer.viewReport(jpReporte, false);

        } catch (Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error al generar un reporte en productos");
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }
    }

}
