package com.unito.prog3.Login;

import com.unito.prog3.Client.ClientController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class LoginController {
    @FXML
    public ComboBox accountSelector;
    private ClientController mainController; // Reference to the main controller

    public void openClient(ActionEvent event) {
        ClientController mainController = new ClientController();
        mainController.openClient();
        Platform.exit();
    }

    @FXML
    private void selectAccount1(ActionEvent event) {
        //mainController.showAccountPage("Account 1"); // Show the account page with account 1
    }

}
