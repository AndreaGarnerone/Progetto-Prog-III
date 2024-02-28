package com.unito.prog3.ShowEmail;

import com.unito.prog3.Client.ClientController;
import javafx.application.Application;
import com.unito.prog3.Client.ClientApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ShowEmail extends Application {
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("ClientMail.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Show Email");
        stage.setScene(scene);
        stage.show();
    }
}
