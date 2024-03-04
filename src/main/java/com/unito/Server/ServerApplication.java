package com.unito.Server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class ServerApplication extends Application {
    ServerModel serverModel = new ServerModel();
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MailServer.fxml")));

        primaryStage.setTitle("Mail Application");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();

        serverModel.listen();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
