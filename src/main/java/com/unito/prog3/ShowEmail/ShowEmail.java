package com.unito.prog3.ShowEmail;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ShowEmail extends Application {

    private Stage stage;

    public ShowEmail(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ShowEmail.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        primaryStage.setTitle("Show Email");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void show() {
        try {
            start(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
