package com.unito.Server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ServerApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MailServer.fxml"));
        Parent root = loader.load();


        ServerModel serverModel = new ServerModel();

        primaryStage.setTitle("Mail Application");
        // Set the scene dimensions based on the preferred height
        primaryStage.setScene(new Scene(root, 600, 400));

        ServerController serverController = loader.getController();
        serverController.setServerModel(serverModel);
        serverController.initialize();

        // 1. Create the server thread and set it as daemon:
        Thread serverThread = new Thread(serverModel::listen);
        serverThread.setDaemon(true); // Set the thread as daemon

        // 2. Start the server thread:
        serverThread.start();

        // 3. Handle window close event for graceful shutdown:
        primaryStage.setOnCloseRequest(event -> {
            // 3.1 Stop the server thread (interrupt the listening loop):
            serverThread.interrupt();

            // 3.2 Perform additional cleanup in the ServerModel (if needed):
            serverModel.stopServer();
        });

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
