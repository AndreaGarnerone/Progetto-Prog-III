module org.example.progiii {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;

    opens com.unito.prog3.Client to javafx.fxml;
    exports com.unito.prog3.Client;
    opens com.unito.prog3.WriteEmail to javafx.fxml;
    exports com.unito.prog3.WriteEmail;
    opens com.unito.prog3.Login to javafx.fxml;
    exports com.unito.prog3.Login;
    exports com.unito.prog3.ShowEmail;
    opens com.unito.prog3.ShowEmail to javafx.fxml;
}