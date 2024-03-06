module org.example.progiii {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    requires com.google.gson;
    requires org.json;

    exports com.unito.ClientMain;
    opens com.unito.ClientMain to javafx.fxml;

    exports com.unito.Login;
    opens com.unito.Login to javafx.fxml;

    exports com.unito.ShowEmail;
    opens com.unito.ShowEmail to javafx.fxml;

    exports com.unito.WriteEmail;
    opens com.unito.WriteEmail to javafx.fxml;

    exports com.unito.Server;
    opens com.unito.Server to javafx.fxml;
}
