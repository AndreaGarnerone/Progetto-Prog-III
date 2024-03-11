package com.unito.WriteEmail;

import com.unito.ClientMain.ClientModel;
import com.unito.ClientMain.Email;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReplyController {
    @FXML
    public TextField messageField;
    @FXML
    public Label toField;
    @FXML
    public Label subjectField;
    @FXML
    public Label fromReField;
    @FXML
    public Label toReField;
    @FXML
    public Label dateReField;
    @FXML
    public Label subjectReField;
    @FXML
    public Label messageReField;

    public Email email = null;
    public String selectedAccount = null;

    public ReplyController() {
    }

    public ReplyController(Email email) {
        this.email = email;
    }

    public void loadEmail(Email email) {
        // Sopra
        toField.setText(email.getFrom());
        subjectField.setText("Re: " + email.getSubject());

        // Sotto
        fromReField.setText(email.getFrom());
        toReField.setText(email.getToFirst());
        dateReField.setText(email.getTimestamp());
        subjectReField.setText(email.getSubject());
        messageReField.setText(email.getContent());
    }

    public void loadEmailAll(Email email, String selectedAccount) {
        // Sopra
        this.selectedAccount = selectedAccount;
        String toAllCorrect = getToAll(email, selectedAccount);
        System.out.println(toAllCorrect);

        toField.setText(toAllCorrect);
        subjectField.setText("Re: " + email.getSubject());

        // Sotto
        fromReField.setText(email.getFrom());
        toReField.setText(email.getToAll());
        dateReField.setText(email.getTimestamp());
        subjectReField.setText(email.getSubject());
        messageReField.setText(email.getContent());
    }

    // Return the string with all the receivers of the new mail
    private static String getToAll(Email email, String selectedAccount) {
        boolean addedRecipient = false; // Flag to track if any recipients were added

        String[] toControlled = email.getToAll().split(";");
        String toAllCorrect = email.getFrom() + ";";

        for (String toSingle : toControlled) {
            if (!toSingle.equals(selectedAccount)) {
                if (addedRecipient) {
                    toAllCorrect += ";";
                }
                toAllCorrect += toSingle;
                addedRecipient = true;
            }
        }
        // If any recipient was added, remove ending ";"
        if (addedRecipient && toAllCorrect.endsWith(";")) {
            toAllCorrect = toAllCorrect.substring(0, toAllCorrect.length() - 1);
        }
        return toAllCorrect;
    }

    public void sendEmail(ActionEvent event) throws IOException, ClassNotFoundException {
        String to = toField.getText();
        String subject = subjectField.getText();
        String content = messageField.getText();
        String from =selectedAccount;

        if (messageField.getText().isEmpty()) {
            // Show a warning dialog
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please write a message");
            alert.showAndWait();
        } else {
            String timestamp = new SimpleDateFormat("dd/MM/yyyy:HH.mm.ss").format(Calendar.getInstance().getTime());

            ClientModel clientModel = new ClientModel(from);
            clientModel.addEmail(new Email(from, to, subject, content, timestamp));

            // Get the stage from the event source
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();

            stage.close();
        }
    }
}
