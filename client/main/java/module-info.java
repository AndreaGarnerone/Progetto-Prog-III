module org.example.progiii {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;

    opens Client.ClientMain to javafx.fxml;
    exports Client.ClientMain;
    opens Client.WriteEmail to javafx.fxml;
    exports Client.WriteEmail;
    opens Client.Login to javafx.fxml;
    exports Client.Login;
    exports Client.ShowEmail;
    opens Client.ShowEmail to javafx.fxml;
}