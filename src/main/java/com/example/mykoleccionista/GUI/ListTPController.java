package com.example.mykoleccionista.GUI;

import com.example.mykoleccionista.Negocios.TipoProducto;
import com.example.mykoleccionista.Negocios.TipoProductoMgr;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

//CONTROLADOR DE ListTP.fxml
public class ListTPController {
    //TABLA
    @FXML
    private TableView ListTable;
    //BOTONES
    @FXML
    private Button ListbuttonAgregar;
    @FXML
    private Button ListbuttonEditar;
    @FXML
    private Button ListbuttonBorrar;
    @FXML
    private Button ListbuttonAceptar;
    @FXML
    private Button ListbuttonCancelar;
    @FXML
    private Button ListbuttonGoBack;
    //CAMPOS DE TEXTO
    @FXML
    private TextField ListTextId;
    @FXML
    private TextField ListTextDesc;

    //VARIABLES DE LA CLASE
    private Accion accion = Accion.NAVEGAR;
    private TipoProductoMgr tipoproductos;

    public void initialize(){
        try{
            tipoproductos = new TipoProductoMgr();
            TableColumn<TipoProducto, Integer> tcId = new TableColumn<>("id");
            TableColumn<TipoProducto, String> tcDesc = new TableColumn<>("Descripcion");

            tcId.setCellValueFactory(new PropertyValueFactory<>("id"));
            tcDesc.setCellValueFactory(new PropertyValueFactory<>("Descripcion"));

            ListTable.getColumns().clear();
            ListTable.getColumns().addAll(tcId, tcDesc);
            ListTable.getItems().addAll(tipoproductos.getTipoProducto());

        } catch (Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error en el TipoProducto");
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }
        MostrarControles();
    }

    public void clickListTable(MouseEvent mouseEvent) {
        Object object  = ListTable.getSelectionModel().getSelectedItem();
        if (object==null){
            return;
        }
        TipoProducto tipoprod = (TipoProducto) object;
        ListTextId.setText(String.format("%d", tipoprod.getId()));
        ListTextDesc.setText(tipoprod.getDescripcion());
    }

    public void clickListbuttonAgregar(ActionEvent actionEvent) {
        accion = Accion.AGREGAR;
        MostrarControles();
    }

    public void clickListbuttonEditar(ActionEvent actionEvent) {
        accion = Accion.EDITAR;
        MostrarControles();
    }

    public void clickListbuttonBorrar(ActionEvent actionEvent) {
        accion = Accion.BORRAR;
        MostrarControles();
    }

    public void clickListbuttonAceptar(ActionEvent actionEvent) {
        TipoProducto tipoprod = new TipoProducto(Integer.valueOf(ListTextId.getText()), ListTextDesc.getText());
        try {
            switch (accion) {
                case AGREGAR:
                    tipoproductos.insertarTipoProducto(tipoprod);
                    break;
                case EDITAR:
                    tipoproductos.editarTipoProducto(tipoprod);
                    break;
                case BORRAR:
                    tipoproductos.borrarTipoProducto(tipoprod);
                    break;
            }

            ListTable.getItems().clear();
            ListTable.getItems().addAll(tipoproductos.getTipoProducto());
        } catch (Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText(String.format("Error al %s un TipoProducto", accion.toString().toLowerCase()));
            msg.setContentText(ex.getMessage());
            msg.showAndWait();
        }

        accion = Accion.NAVEGAR;
        MostrarControles();
        Limpiar();
    }

    public void clickListbuttonCancelar(ActionEvent actionEvent) {
        accion = Accion.NAVEGAR;
        Limpiar();
        MostrarControles();
    }

    private void MostrarControles(){
        ListTextId.setDisable(true);
        ListTextDesc.setDisable(true);
        ListTable.setDisable(true);
        ListbuttonAceptar.setVisible(false);
        ListbuttonCancelar.setVisible(false);
        ListbuttonAgregar.setVisible(false);
        ListbuttonBorrar.setVisible(false);
        ListbuttonEditar.setVisible(false);


        switch (accion){
            case NAVEGAR:
                ListTable.setDisable(false);
                ListbuttonAgregar.setVisible(true);
                ListbuttonBorrar.setVisible(true);
                ListbuttonEditar.setVisible(true);
                break;
            case AGREGAR:
                Limpiar();
                ListTextId.setDisable(false);
            case EDITAR:
                ListTextDesc.setDisable(false);
            case BORRAR:
                ListbuttonAceptar.setVisible(true);
                ListbuttonCancelar.setVisible(true);
        }
    }
    private void Limpiar(){
        ListTextId.setText("");
        ListTextDesc.setText("");
    }

    public void clickListbuttonGoBack(ActionEvent actionEvent) {
        Stage stage = (Stage) ListbuttonGoBack.getScene().getWindow();
        stage.close();
    }
}
