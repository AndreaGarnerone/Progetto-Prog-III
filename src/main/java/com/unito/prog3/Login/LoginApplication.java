package com.unito.prog3.Login;

import com.unito.prog3.Client.ClientApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginApplication extends Application {

    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginPage.fxml"));

        Scene scene = new Scene(loader.load(), 800, 600);
        stage.setTitle("Mail Client");
        stage.setScene(scene);
        stage.show();
    }

    public void showAccountPage(String accountName) {
        // Implement logic to show the account page based on the selected account
        // You can use FXMLLoader to load the FXML file for the account page and show it similarly to showInitialPage()
        // You can pass accountName to the account page controller if necessary
    }

    public static void main(String[] args) {
        launch(args);
    }
}
