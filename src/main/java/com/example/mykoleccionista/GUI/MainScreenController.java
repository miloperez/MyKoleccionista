package com.example.mykoleccionista.GUI;

import com.example.mykoleccionista.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

//CONTROLADOR DE MainScreen.fxml
public class MainScreenController {
    @FXML
    public void clickMSButtonAlmacen(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(HelloApplication.class.getResource("Almacen.fxml"));
            //fxmlLoader.setController(this);
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Almacen");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void clickMSButtonClient(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(HelloApplication.class.getResource("CABC.fxml"));
            //fxmlLoader.setController(this);
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Gestionar Clientes");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clickMSButtonVentas(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(HelloApplication.class.getResource("Ventas.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Ventas");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clickMSButtonPreventas(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(HelloApplication.class.getResource("Preventas.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Preventas");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
