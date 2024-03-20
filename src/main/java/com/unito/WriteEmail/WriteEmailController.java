package com.unito.WriteEmail;

import com.unito.ClientMain.ClientModel;
import com.unito.ClientMain.Email;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WriteEmailController {
    @FXML
    public TextField toField;
    @FXML
    public TextField subjectField;
    @FXML
    public TextArea messageField;
    @FXML
    private String from;
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
            showAlert("Please fill in all fields.");
        } else {
            String[] recipients = to.split(";");
            boolean allValidDomain = true;
            boolean allValidName = true;

            for (String recipient : recipients) {
                recipient = recipient.trim();
                if (!isValidEmail(recipient)) {
                    allValidDomain = false;
                    break;
                }
                if (!isValidUsername(recipient)) {
                    allValidName = false;
                    break;
                }
            }

            if (allValidDomain && allValidName) {
                String timestamp = new SimpleDateFormat("dd/MM/yyyy:HH.mm.ss").format(Calendar.getInstance().getTime());
                // Assuming 'from' is declared somewhere else in your code
                clientModel.addEmail(new Email(from, to, subject, content, timestamp));

                // Get the stage from the event source
                Node source = (Node) event.getSource();
                Stage stage = (Stage) source.getScene().getWindow();

                stage.close();
            } else {
                if (!allValidName) {
                    showAlert("Wrong mail name");
                } else {
                    showAlert("Wrong mail domain");
                }
            }
        }
    }

    public boolean isValidEmail(String email) {
        String[] parts = email.split("@");
        if (parts.length != 2) {
            return false;
        }
        String domain = parts[1].trim();
        return domain.equals("gormail.com");
    }

    public boolean isValidUsername(String email) {
        String[] parts = email.split("@");
        if (parts.length != 2) {
            return false;
        }
        String username = parts[0].trim();
        for (char c : username.toCharArray()) {
            if (!(Character.isLetterOrDigit(c) || c == '.' || c == '_')) {
                return false;
            }
        }
        return true;
    }

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void clearFields() {
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
        alert.setContentText("Incorrect user mail address");
        alert.showAndWait();
    }

}
