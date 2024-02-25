package com.unito.prog3.Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApplication extends Application {
    private String selectedAccount;

    // Getter and setter for selectedAccount
    public String getSelectedAccount() {
        return selectedAccount;
    }

    public void setSelectedAccount(String selectedAccount) {
        this.selectedAccount = selectedAccount;
    }

    // Your existing start method remains unchanged
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("ClientMail.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Mail Client - " + selectedAccount); // Append the selected account to the title
        stage.setScene(scene);
        stage.show();

        ClientController ccl = fxmlLoader.getController();
        ccl.initialize(selectedAccount); // Pass the selected account to the controller
    }
}
