package com.unito.ClientMain;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
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

    public String getSelectedAccount() {
        return selectedAccount;
    }

    public void setSelectedAccount(String selectedAccount) {
        this.selectedAccount = selectedAccount;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("ClientMail.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Gormail Client");
        stage.setScene(scene);

        stage.setOnCloseRequest(event -> {
            shutdown();
        });

        stage.show();

        ClientController ccl = fxmlLoader.getController();
        ccl.initialize(selectedAccount);

        // Start a ScheduledExecutorService to call refresh() every two seconds
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            // Call refresh() method here
            ccl.refreshEmail(new ActionEvent());
        }, 0, 2, TimeUnit.SECONDS);
    }

    // Method to properly shut down the application
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
