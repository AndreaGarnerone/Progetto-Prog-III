package com.unito.Client.WriteEmail;

import com.unito.Client.ClientModel;
import com.unito.Client.Email;
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

    public Email email = null;
    public String selectedAccount;

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
    @FXML
    public TextField toFieldForward;
    @FXML
    public Label subjectFieldForward;
    @FXML
    public Label messageFieldForward;
    @FXML
    public Label fromFieldForward;

    /**
     * Constructor
     */
    public ReplyController() {
    }

    /**
     * Set the texts of the fields of the mail to forward on the forward page
     * @param email The email to be showed
     * @param selectedAccount The user account
     */
    public void loadForward(Email email, String selectedAccount) {
        this.selectedAccount = selectedAccount;
        fromFieldForward.setText(email.getFrom());
        subjectFieldForward.setText(email.getSubject());
        messageFieldForward.setText(email.getContent());
    }

    /**
     * Set the text of the mail to be showed
     * @param email The email to be showed
     */
    public void loadEmail(Email email) {
        toField.setText(email.getFrom());
        subjectField.setText("Re: " + email.getSubject());

        fromReField.setText(email.getFrom());
        toReField.setText(email.getToFirst());
        dateReField.setText(email.getTimestamp());
        subjectReField.setText(email.getSubject());
        messageReField.setText(email.getContent());
    }

    /**
     * Set the text of the mail to be showed
     * @param email The email to be showed
     * @param selectedAccount The user account
     */
    public void loadEmailAll(Email email, String selectedAccount) {
        this.selectedAccount = selectedAccount;
        String toAllCorrect = getToAll(email, selectedAccount);
        System.out.println(toAllCorrect);

        toField.setText(toAllCorrect);
        subjectField.setText("Re: " + email.getSubject());

        fromReField.setText(email.getFrom());
        toReField.setText(email.getToAll());
        dateReField.setText(email.getTimestamp());
        subjectReField.setText(email.getSubject());
        messageReField.setText(email.getContent());
    }

    /**
     * Return the string with all the receivers of the new mail
     * @param email The email to be showed
     * @param selectedAccount The user account
     * @return The list of all the email receivers
     */
    private static String getToAll(Email email, String selectedAccount) {
        boolean addedRecipient = false; // Flag to track if any recipients were added

        String[] toControlled = email.getToAll().split(";");
        StringBuilder toAllCorrect = new StringBuilder(email.getFrom() + ";");

        for (String toSingle : toControlled) {
            if (!toSingle.equals(selectedAccount)) {
                if (addedRecipient) {
                    toAllCorrect.append(";");
                }
                toAllCorrect.append(toSingle);
                addedRecipient = true;
            }
        }
        // If any recipient was added, remove ending ";"
        if (addedRecipient && toAllCorrect.toString().endsWith(";")) {
            toAllCorrect = new StringBuilder(toAllCorrect.substring(0, toAllCorrect.length() - 1));
        }
        return toAllCorrect.toString();
    }

    /**
     * Set up the fields and call the sender
     */
    public void sendEmail(ActionEvent event) throws IOException, ClassNotFoundException {
        String to = toField.getText();
        String subject = subjectField.getText();
        String content = messageField.getText();
        String from = selectedAccount;

        String[] fields = new String[4];
        fields[0] = from;
        fields[1] = to;
        fields[2] = subject;
        fields[3] = content;

        sendEmailAction(fields, event);
    }

    /**
     * Set up the fields and call the sender
     */
    public void sendEmailForward(ActionEvent event) {
        String from = selectedAccount;
        String to = toFieldForward.getText();
        String subject = subjectFieldForward.getText();
        String content = messageFieldForward.getText();
        System.out.println(from);

        String[] fields = new String[4];
        fields[0] = from;
        fields[1] = to;
        fields[2] = subject;
        fields[3] = content;

        sendEmailAction(fields, event);
    }

    /**
     * Check for the correctness of the fields and send the email. Then close the window
     * @param fields The fields of the mail, saved in an array for simplicity
     */
    private void sendEmailAction(String[] fields, ActionEvent event) {
        if (fields[3].isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please write a message");
            alert.showAndWait();
        } else {
            String timestamp = new SimpleDateFormat("dd/MM/yyyy:HH.mm.ss").format(Calendar.getInstance().getTime());

            ClientModel clientModel = new ClientModel(fields[0]);
            clientModel.addEmail(new Email(fields[0], fields[1], fields[2], fields[3], timestamp));

            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();

            stage.close();
        }
    }

}
