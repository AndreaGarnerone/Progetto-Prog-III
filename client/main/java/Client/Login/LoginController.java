package Client.Login;

import Client.ClientMain.ClientApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    public ComboBox<String> accountSelector;
    public Label Feedback;
    private Stage stage; // Reference to the stage of login page

    // Setter method to set the stage
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void openClient(ActionEvent event) throws IOException {
        if (accountSelector.getValue() == null) {
            // Show a warning dialog
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please select an account.");
            alert.showAndWait();
        } else {
            // Close the login stage
            stage.close();

            // Open the client application
            ClientApplication clientApplication = new ClientApplication();
            clientApplication.setSelectedAccount(accountSelector.getValue()); // Set the selected account
            Stage clientStage = new Stage();
            clientApplication.start(clientStage);
        }
    }
}
