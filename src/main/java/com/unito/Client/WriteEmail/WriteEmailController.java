package com.unito.Client.WriteEmail;

import com.unito.Client.ClientModel;
import com.unito.Client.Email;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
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

    /**
     * Check if all the fields are correct.
     * If all are valid, send the mail anc close the page
     * @param event
     */
    public void sendEmail(ActionEvent event) {
        String to = toField.getText();
        String subject = subjectField.getText();
        String content = messageField.getText();

        if (to.isEmpty() || subject.isEmpty() || content.isEmpty()) {
            showAlert("Please fill in all fields.", 0);
        } else {
            String[] recipients = to.split(";");
            boolean allValid = true;

            for (String recipient : recipients) {
                recipient = recipient.trim();
                if (!isValidEmail(recipient)) {
                    allValid = false;
                    showAlert("Wrong mail domain", 0);
                    break;
                }
                if (!isValidUsername(recipient)) {
                    allValid = false;
                    showAlert("Wrong mail name", 0);
                    break;
                }

                if (!checkValidEmail(recipient)) {
                    allValid = false;
                    break;
                }
            }

            if (allValid) {
                String timestamp = new SimpleDateFormat("dd/MM/yyyy:HH.mm.ss").format(Calendar.getInstance().getTime());
                clientModel.addEmail(new Email(from, to, subject, content, timestamp));

                Node source = (Node) event.getSource();
                Stage stage = (Stage) source.getScene().getWindow();

                stage.close();
            }
        }
    }

    /**
     * Perform the check for the correctness of the email address
     * @param recipient The account name
     * @return true if the email is valid, false otherwise
     */
    private boolean checkValidEmail(String recipient) {
        String filePath = "MailStorage/" + recipient + ".json";
        File file = new File(filePath);
        if (!file.exists()) {
            showAlert("Incorrect user mail address", 1);
            return false;
        }
        return true;
    }

    /**
     * Perform the check for the correctness of the email address
     * @param email The email written
     * @return true if the email is valid, false otherwise
     */
    public boolean isValidEmail(String email) {
        String[] parts = email.split("@");
        if (parts.length != 2) {
            return false;
        }
        String domain = parts[1].trim();
        return domain.equals("gormail.com");
    }

    /**
     * Perform the check for the correctness of the email address
     * @param email The email written
     * @return true if the email is valid, false otherwise
     */
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

    /**
     * Show an alert element type if there were some errors compiling all the fields.
     * @param message The error message to be shown
     * @param typeError The error type (ERROR or WARNING)
     */
    public void showAlert(String message, int typeError) {
        if (typeError == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
    }

    /**
     * Remove the written text from all the fields
     */
    public void clearFields() {
        toField.clear();
        subjectField.clear();
        messageField.clear();
    }

    /**
     * Setter method
     * @param sender
     */
    public void setSender(String sender) {
        this.from = sender;
    }

}
