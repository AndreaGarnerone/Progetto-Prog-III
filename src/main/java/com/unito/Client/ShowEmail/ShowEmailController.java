package com.unito.Client.ShowEmail;

import com.unito.Client.Email;
import com.unito.Client.WriteEmail.ReplyView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class ShowEmailController {
    @FXML
    public Label fromField;
    @FXML
    public Label subjectField;
    @FXML
    public Label messageField;
    @FXML
    public Label timestampField = null;
    public Email emailV = null;
    String accountName = null;

    /**
     * Convert the JSon to Email  and set the parameters
     *
     * @param emailJson       The email selected to show
     * @param selectedAccount The user's account
     */
    public void initialize(String emailJson, String selectedAccount) {
        Email email = Email.fromString(emailJson);

        emailV = email;
        accountName = selectedAccount;

        if (email != null) {
            fromField.setText(email.getFrom());
            subjectField.setText(email.getSubject());
            messageField.setText(email.getContent());
            timestampField.setText(email.getTimestamp());
        } else {
            System.err.println("Failed to parse email JSON.");
        }
    }

    /**
     * Start the reply fxml page
     */
    public void reply() throws Exception {
        Stage stage = new Stage();
        ReplyView reply = new ReplyView(emailV);

        reply.start(stage, accountName);
    }

    /**
     * Start the reply all fxml page
     */
    public void replyAll() throws IOException {
        Stage stage = new Stage();
        ReplyView reply = new ReplyView(emailV);

        reply.startAll(stage, accountName);
    }

    /**
     * Start the forward fxml page
     */
    public void forward() throws IOException {
        Stage stage = new Stage();
        ReplyView reply = new ReplyView(emailV);

        reply.startLoaded(stage, accountName);
    }
}
