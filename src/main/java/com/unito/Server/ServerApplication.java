package com.unito.Server;

import com.unito.ClientMain.ClientApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("MailServer.fxml"));

        // Set up the primary stage
        primaryStage.setTitle("Mail Application");
        primaryStage.setScene(new Scene(root, 800, 600)); // Adjust width and height as needed
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
