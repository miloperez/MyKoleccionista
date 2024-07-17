package com.example.mykoleccionista.GUI;

import com.example.mykoleccionista.Negocios.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
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
import java.util.Map;
import java.util.Objects;

//CONTROLADOR DE Abonos.fxml
public class AbonosController {
    //TABLAS
    @FXML
    private TableView<PreventaAbono> AbonosTable;
    //FECHAS
    @FXML
    private DatePicker AbonosDatePFecha;
    //BOTONES
    @FXML
    private Button AbonosButtonGoBack;
    @FXML
    private Button AbonosButtonAgregar;
    @FXML
    private Button AbonosButtonEditar;
    @FXML
    private Button AbonosButtonBorrar;
    @FXML
    private Button AbonosButtonAceptar;
    @FXML
    private Button AbonosButtonCancelar;
    @FXML
    private Button AbonosButtonLiquidar;
    @FXML
    private Button AbonosButtonReporte;
    //CAMPOS DE TEXTO
    @FXML
    private TextField AbonosTextIdPreventa;
    @FXML
    private TextField AbonosTextTotal;
    @FXML
    private TextField AbonosTextAbonado;
    @FXML
    private TextField AbonosTextAbonar;
    @FXML
    private TextField AbonosTextRestante;

    //VARIABLES DE LA CLASE
    private PreventaAbonoMgr PreventaAbonos;
    private PreventaMaestroMgr PreventaMaestros;
    private PreventaDetalleMgr PreventaDetalles;
    private VentaMaestroMgr VentaMaestros;
    private VentaDetalleMgr VentaDetalles;
    private Accion accion = Accion.NAVEGAR;
    private int id;
    private Float total;
    private Float abonado;

    void initData(int id) {
        this.id = id;

        AbonosTextIdPreventa.setText(String.format("%d", this.id));

        try {
            PreventaMaestro PreMae = PreventaMaestros.getFromId(this.id);
            this.total = PreMae.getTotal();
            AbonosTextTotal.setText(this.total.toString());
            this.abonado = abonado();
            AbonosTextAbonado.setText(this.abonado.toString());
        }catch(Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error en initData");
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }
        initialize();
    }

    public void initialize() {
        try{
            PreventaAbonos = new PreventaAbonoMgr();
            PreventaMaestros = new PreventaMaestroMgr();
        }catch(Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error en la inicialización");
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }
        //TABLA
        try{
            TableColumn<PreventaAbono, Integer> tcIdPreventa = new TableColumn<>("idPreventa");
            TableColumn<PreventaAbono, String> tcFecha = new TableColumn<>("Fecha");
            TableColumn<PreventaAbono, Float> tcCantidad = new TableColumn<>("Cantidad");

            tcIdPreventa.setCellValueFactory(new PropertyValueFactory<>("idPreventa"));
            tcFecha.setCellValueFactory(new PropertyValueFactory<>("Fecha"));
            tcCantidad.setCellValueFactory(new PropertyValueFactory<>("Cantidad"));

            AbonosTable.getColumns().clear();
            AbonosTable.getColumns().addAll(tcIdPreventa, tcFecha, tcCantidad);
            AbonosTable.getItems().addAll(PreventaAbonos.getFromId(this.id));


        } catch (Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error en Tabla de Abonos");
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }
        MostrarControles();
    }

    public void clickAbonosTable(MouseEvent mouseEvent) {
        try{
            Object object  = AbonosTable.getSelectionModel().getSelectedItem();
            if (object==null){
                return;
            }

            PreventaAbono PreAbo = (PreventaAbono) object;
            PreventaMaestro PreMae = PreventaMaestros.getFromId(PreAbo.getIdPreventa());

            AbonosTextTotal.setText(PreMae.getTotal().toString());


            Float suma = 0f;
            if(PreventaAbonos.getFromId(this.id) != null){
                for(PreventaAbono i: PreventaAbonos.getFromId(this.id)){
                    suma += i.getCantidad();
                }
            }

            AbonosTextAbonado.setText(suma.toString());
            AbonosTextAbonar.setText(PreAbo.getCantidad().toString());
            AbonosTextRestante.setText(((Float)(PreMae.getTotal() - suma)).toString());
            AbonosDatePFecha.setValue(LocalDate.parse(PreAbo.getFecha()));
            AbonosButtonEditar.setDisable(false);
            AbonosButtonBorrar.setDisable(false);

        }catch(Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error en Tabla");
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }
    }

    public void clickAbonosButtonAceptar(ActionEvent actionEvent) {
        //PreventaAbono(int idPreventa, String fecha, Float cantidad)

        PreventaAbono PreAbo = new PreventaAbono(this.id, AbonosDatePFecha.getValue().toString(), Float.parseFloat(AbonosTextAbonar.getText()));

        try{
            switch (accion) {
                case AGREGAR:
                    PreventaAbonos.insertarPreventaAbono(PreAbo);
                    break;
                case EDITAR:
                    PreventaAbonos.editarPreventaAbono(PreAbo);
                    break;
                case BORRAR:
                    PreventaAbonos.borrarPreventaAbono(PreAbo);
                    break;
            }

            if(abonado() == PreventaMaestros.getFromId(this.id).getTotal()){
                regLiquidar();

                Stage stage = (Stage)  AbonosButtonGoBack.getScene().getWindow();
                stage.close();
            }

            AbonosTable.getItems().clear();
            AbonosTable.getItems().addAll(PreventaAbonos.getFromId(this.id));
            Limpiar();
        } catch (Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText(String.format("Error al %s un Abono", accion.toString().toLowerCase()));
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }
        accion = Accion.NAVEGAR;
        MostrarControles();
    }

    public void clickAbonosButtonCancelar(ActionEvent actionEvent) {
        accion = Accion.NAVEGAR;
        Limpiar();
        MostrarControles();
    }

    public void clickAbonosButtonAgregar(ActionEvent actionEvent) {
        accion = Accion.AGREGAR;
        MostrarControles();
    }

    public void clickAbonosButtonEditar(ActionEvent actionEvent) {
        accion = Accion.EDITAR;
        MostrarControles();
    }

    public void clickAbonosButtonBorrar(ActionEvent actionEvent) {
        accion = Accion.BORRAR;
        MostrarControles();
    }

    private void MostrarControles() {
        AbonosTable.setDisable(true);

        AbonosTextIdPreventa.setDisable(true);
        AbonosTextTotal.setDisable(true);
        AbonosTextAbonado.setDisable(true);
        AbonosTextRestante.setDisable(true);
        AbonosTextAbonar.setDisable(true);
        AbonosDatePFecha.setDisable(true);

        AbonosButtonAgregar.setVisible(false);
        AbonosButtonEditar.setVisible(false);
        AbonosButtonEditar.setDisable(true);
        AbonosButtonBorrar.setVisible(false);
        AbonosButtonBorrar.setDisable(true);
        AbonosButtonLiquidar.setVisible(false);
        AbonosButtonAceptar.setVisible(false);
        AbonosButtonCancelar.setVisible(false);

        switch (accion){
            case NAVEGAR:
                AbonosTable.setDisable(false);
                AbonosButtonAgregar.setVisible(true);
                AbonosButtonBorrar.setVisible(true);
                AbonosButtonEditar.setVisible(true);
                break;
            case AGREGAR:
                Limpiar();
                AbonosButtonLiquidar.setDisable(false);
                AbonosButtonLiquidar.setVisible(true);
                AbonosTextRestante.setDisable(false);
                AbonosTextAbonado.setDisable(false);
                AbonosTextTotal.setDisable(false);
                AbonosTextAbonar.setDisable(false);
                AbonosDatePFecha.setDisable(false);

                AbonosButtonAceptar.setVisible(true);
                AbonosButtonCancelar.setVisible(true);
                restante();
                break;
            case EDITAR:
                AbonosButtonLiquidar.setDisable(false);
                AbonosTextAbonar.setDisable(false);

                AbonosButtonAceptar.setVisible(true);
                AbonosButtonCancelar.setVisible(true);
                restante();
                break;
            case BORRAR:
                AbonosButtonAceptar.setVisible(true);
                AbonosButtonCancelar.setVisible(true);
                restante();
                break;
        }
    }

    private void Limpiar() {
        AbonosTextRestante.setText("");
        AbonosTextAbonar.setText("");
        AbonosDatePFecha.setValue(null);
    }

    @FXML
    private void clickAbonosButtonGoBack(ActionEvent actionEvent) {
        Stage stage = (Stage)  AbonosButtonGoBack.getScene().getWindow();
        stage.close();
    }

    public void clickAbonosTextAbonar(KeyEvent actionEvent) {
        if(!Objects.equals(AbonosTextAbonar.getText(), "")){
            if( Float.parseFloat(AbonosTextAbonar.getText()) > ((float) this.total-this.abonado)  || Float.parseFloat(AbonosTextAbonar.getText()) <= 0){
                AbonosTextAbonar.setText("0");
                Alert msg = new Alert(Alert.AlertType.ERROR);
                msg.setTitle("Error");
                msg.setHeaderText("Cantidad no válida");
                msg.showAndWait();
            }
        }
        restante();
    }

    private void restante(){
        if(AbonosTextAbonar.getText() != null && !Objects.equals(AbonosTextAbonar.getText(), "")){
            AbonosTextRestante.setText( String.format("%f", this.total-this.abonado - Float.parseFloat(AbonosTextAbonar.getText())) );
        }else{
            AbonosTextRestante.setText( String.format("%f", this.total-this.abonado - 0) );
        }
    }

    public void clickAbonosButtonLiquidar(ActionEvent actionEvent) {
        AbonosTextAbonar.setText(String.format("%f", this.total-this.abonado));
        AbonosTextRestante.setText( String.format("%f", this.total-this.abonado - Float.parseFloat(AbonosTextAbonar.getText())) );
    }

    private float abonado(){
        try{
            Float suma = 0f;
            if(PreventaAbonos.getFromId(this.id) != null){
                for(PreventaAbono i: PreventaAbonos.getFromId(this.id)){
                    suma += i.getCantidad();
                }
            }

            return (suma);

        }catch(Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error en abonado");
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }
        return 0;
    }

    private void regLiquidar(){
        try{
            PreventaMaestro PreMae = PreventaMaestros.getFromId(this.id);
            PreMae.setEstado(2);
            PreventaMaestros.editarPreventaMaestro(PreMae);

        }catch(Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error en liquidar");
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }
    }

    public void clickAbonosDatePFecha(ActionEvent actionEvent) {
        try{
            /*if (AbonosDatePFecha.getValue() != null) {
                Alert msg = new Alert(Alert.AlertType.WARNING);
                msg.setTitle("Advertencia");
                LocalDate today = LocalDate.now();
                if ((AbonosDatePFecha.getValue()).isAfter(today)) {
                    msg.setHeaderText("La fecha no puede ser después del día de hoy.");
                    msg.showAndWait();
                    AbonosDatePFecha.setValue(null);
                } else{
                    for(PreventaAbono PreAbo: PreventaAbonos.getPreventaAbono()){
                        LocalDate date = LocalDate.parse(PreAbo.getFecha());
                        if((AbonosDatePFecha.getValue()).equals(date) ){
                            msg.setHeaderText("La fecha no puede repetirse");
                            msg.showAndWait();
                            AbonosDatePFecha.setValue(null);
                            break;
                        } else if ((AbonosDatePFecha.getValue()).isBefore(date)) {
                            msg.setHeaderText("La fecha no puede ser antes de un abono a registrado.");
                            msg.showAndWait();
                            AbonosDatePFecha.setValue(null);
                            break;
                        }
                    }
                }
            }*/
        }catch(Exception ex){
        Alert msg = new Alert(Alert.AlertType.ERROR);
        msg.setTitle("Error");
        msg.setHeaderText("Error en fechas");
        msg.setContentText(ex.getMessage());
        msg.showAndWait();
        }
    }

    public void clickAbonosButtonReporte(ActionEvent actionEvent) {
        try{
            JasperReport jrReporte = (JasperReport) JRLoader.loadObject(new File("reportes\\Abonos.jasper"));
            DefaultTableModel datos = new DefaultTableModel();
            datos.setColumnIdentifiers(new Object[]{"idPreventa", "Fecha", "Cantidad"});

            for(PreventaAbono abono: PreventaAbonos.getFromId(this.id)){
                datos.addRow(new Object[]{abono.getIdPreventa(), abono.getFecha(), abono.getCantidad()});
            }

            Map<String,Object> parametros = new HashMap<>();
            parametros.put("TITULO", "Reporte de Abonos");
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
