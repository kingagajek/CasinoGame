module com.gajek.casinogame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.gajek.casinogame to javafx.fxml;
    exports com.gajek.casinogame;
}