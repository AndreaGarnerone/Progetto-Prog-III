package com.unito.Server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServerApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MailServer.fxml"));
        Parent root = loader.load();

        ServerModel serverModel = new ServerModel();

        primaryStage.setTitle("Mail Application");
        primaryStage.setScene(new Scene(root, 800, 600));

        ServerController serverController = loader.getController();
        serverController.setServerModel(serverModel);
        serverController.initialize();

        // 1. Create the server thread and store it for proper interruption:
        Thread serverThread = new Thread(serverModel::listen);

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
