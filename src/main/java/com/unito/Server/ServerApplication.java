package com.unito.Server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServerApplication extends Application {
    /**
     * Starter method that load the server application
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MailServer.fxml"));
        Parent root = loader.load();

        ServerModel serverModel = new ServerModel();

        primaryStage.setTitle("Mail Application");
        primaryStage.setScene(new Scene(root, 600, 400));

        ServerController serverController = loader.getController();
        serverController.setServerModel(serverModel);
        serverController.initialize();

        Thread serverThread = new Thread(serverModel::listen);
        serverThread.setDaemon(true);

        serverThread.start();

        primaryStage.setOnCloseRequest(event -> {
            serverThread.interrupt();

            serverModel.stopServer();
        });

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
