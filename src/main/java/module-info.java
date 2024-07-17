module com.example.mykoleccionista {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jasperreports;
    requires java.desktop;


    opens com.example.mykoleccionista to javafx.fxml;
    exports com.example.mykoleccionista;
    exports com.example.mykoleccionista.GUI;
    opens com.example.mykoleccionista.GUI to javafx.fxml;
    exports com.example.mykoleccionista.Negocios;
    exports com.example.mykoleccionista.Datos;
}