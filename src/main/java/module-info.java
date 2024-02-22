module org.example.progiii {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.unito.prog3.Client to javafx.fxml;
    exports com.unito.prog3.Client;
    opens com.unito.prog3.WriteEmail to javafx.fxml;
    exports com.unito.prog3.WriteEmail;
}