module org.example.progiii {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    requires com.google.gson;
    requires org.json;

    exports com.unito.Client;
    opens com.unito.Client to javafx.fxml;

    exports com.unito.Client.Login;
    opens com.unito.Client.Login to javafx.fxml;

    exports com.unito.Client.ShowEmail;
    opens com.unito.Client.ShowEmail to javafx.fxml;

    exports com.unito.Client.WriteEmail;
    opens com.unito.Client.WriteEmail to javafx.fxml;

    exports com.unito.Server;
    opens com.unito.Server to javafx.fxml;
}
