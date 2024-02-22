package com.unito.prog3.Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class ClientApplication extends Application {

    ClientController ccl;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("ClientMail.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Mail Client");
        stage.setScene(scene);
        stage.show();

        ccl = fxmlLoader.getController();
        ccl.initialize();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
