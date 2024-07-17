package com.example.mykoleccionista.GUI;

import com.example.mykoleccionista.Negocios.TipoEstado;
import com.example.mykoleccionista.Negocios.TipoEstadoMgr;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

//CONTROLADOR DE ListTE.fxml
public class ListTEController {
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
    private TipoEstadoMgr tipoestados;

    public void initialize(){
        try{
            tipoestados = new TipoEstadoMgr();
            TableColumn<TipoEstado, Integer> tcId = new TableColumn<>("id");
            TableColumn<TipoEstado, String> tcDesc = new TableColumn<>("Descripcion");

            tcId.setCellValueFactory(new PropertyValueFactory<>("id"));
            tcDesc.setCellValueFactory(new PropertyValueFactory<>("Descripcion"));

            ListTable.getColumns().clear();
            ListTable.getColumns().addAll(tcId, tcDesc);
            ListTable.getItems().addAll(tipoestados.getTipoEstado());

        } catch (Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText("Error en el TipoEstado");
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
        TipoEstado tipoestado = (TipoEstado) object;
        ListTextId.setText(String.format("%d", tipoestado.getId()));
        ListTextDesc.setText(tipoestado.getDescripcion());
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
        TipoEstado tipoestado = new TipoEstado(Integer.valueOf(ListTextId.getText()), ListTextDesc.getText());
        try {
            switch (accion) {
                case AGREGAR:
                    tipoestados.insertarTipoEstado(tipoestado);
                    break;
                case EDITAR:
                    tipoestados.editarTipoEstado(tipoestado);
                    break;
                case BORRAR:
                    tipoestados.borrarTipoEstado(tipoestado);
                    break;
            }

            ListTable.getItems().clear();
            ListTable.getItems().addAll(tipoestados.getTipoEstado());
        } catch (Exception ex){
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setTitle("Error");
            msg.setHeaderText(String.format("Error al %s un TipoEstado", accion.toString().toLowerCase()));
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
