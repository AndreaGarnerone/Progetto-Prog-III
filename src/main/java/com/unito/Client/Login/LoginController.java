package com.unito.Client.Login;

import com.unito.Client.ClientApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginController {
    @FXML
    public ComboBox<String> accountSelector;
    @FXML
    public ImageView logo;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Let the user select the account and then open the client window
     * @throws IOException
     */
    public void openClient() throws IOException {
        if (accountSelector.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please select an account.");
            alert.showAndWait();
        } else {
            stage.close();

            ClientApplication clientApplication = new ClientApplication();
            clientApplication.setSelectedAccount(accountSelector.getValue());
            Stage clientStage = new Stage();
            clientApplication.start(clientStage);
        }
    }

    /**
     * Load the logo image
     */
    public void initialize() {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("Logo.png")));
        logo.setImage(image);
    }
}
