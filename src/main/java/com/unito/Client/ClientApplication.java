package com.unito.Client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClientApplication extends Application {
    private String selectedAccount;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void setSelectedAccount(String selectedAccount) {
        this.selectedAccount = selectedAccount;
    }

    /**
     * Start the application
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("ClientMail.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Gimail Client");
        stage.setScene(scene);

        stage.show();

        ClientController clientController = fxmlLoader.getController();
        clientController.initialize(selectedAccount);

        stage.setOnCloseRequest(event -> {
            clientController.close();
            shutdown();
        });

        // Start a ScheduledExecutorService to call refresh() every two seconds
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(clientController::refreshEmail, 0, 2, TimeUnit.SECONDS);
    }

    /**
     * Shut down the application and all the threads
     */
    private void shutdown() {
        if (scheduler != null) {
            scheduler.shutdown();
        }

        Platform.exit();
        System.exit(0);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
