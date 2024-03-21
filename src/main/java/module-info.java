module org.example.progiii {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    requires com.google.gson;
    requires org.json;

    exports com.unito.ClientMain;
    opens com.unito.ClientMain to javafx.fxml;

    exports com.unito.ClientMain.Login;
    opens com.unito.ClientMain.Login to javafx.fxml;

    exports com.unito.ClientMain.ShowEmail;
    opens com.unito.ClientMain.ShowEmail to javafx.fxml;

    exports com.unito.ClientMain.WriteEmail;
    opens com.unito.ClientMain.WriteEmail to javafx.fxml;

    exports com.unito.Server;
    opens com.unito.Server to javafx.fxml;
}
