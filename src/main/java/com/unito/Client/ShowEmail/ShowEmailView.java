package com.unito.Client.ShowEmail;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ShowEmailView extends Application {

    public ShowEmailView(Stage stage) {
    }

    /**
     * Starter method that load the fxml page
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MailServer.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Show Email");
        stage.setScene(scene);
        stage.show();
    }

}
