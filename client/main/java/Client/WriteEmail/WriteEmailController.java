package Client.WriteEmail;

import Client.ClientMain.ClientModel;
import Client.ClientMain.Email;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class WriteEmailController {
    @FXML
    public TextField toField;
    @FXML
    public TextField subjectField;
    @FXML
    public TextArea messageField;
    public Label ErrorLabel;

    private String from;

    LocalDateTime currentDateTime = LocalDateTime.now();
    private ClientModel clientModel;

    public void setClientModel(ClientModel clientModel) {
        this.clientModel = clientModel;
    }

    public void sendEmail(ActionEvent event) {
        String to = toField.getText();
        String subject = subjectField.getText();
        String content = messageField.getText();

        // Check if any of the fields are empty
        if (to.isEmpty() || subject.isEmpty() || content.isEmpty()) {
            // Show a warning dialog
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields.");
            alert.showAndWait();
        } else {
            String timestamp = String.valueOf(currentDateTime);

            clientModel.addEmail(new Email(from, to, subject, content, timestamp));

            // Get the stage from the event source
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();

            // Close the stage
            stage.close();
        }
    }

    public void clearFields(ActionEvent event) {
        toField.clear();
        subjectField.clear();
        messageField.clear();
    }

    public void setSender(String sender) {
        this.from = sender;
    }


    public void setErrorText() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText("Please fill in all fields.");
        alert.showAndWait();
    }
}
