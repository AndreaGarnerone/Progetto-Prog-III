package com.unito.Client.ShowEmail;

import com.unito.Client.Email;
import com.unito.Client.WriteEmail.Reply;
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

    public Email emailV = null;
    String accountName = null;

    public void initialize(String emailJson, String selectedAccount) {
        Email email = Email.fromString(emailJson);

        emailV = email;
        accountName = selectedAccount;

        if (email != null) {
            fromField.setText(email.getFrom());
            subjectField.setText(email.getSubject());
            messageField.setText(email.getContent());
        } else {
            System.err.println("Failed to parse email JSON.");
        }
    }

    public void reply() throws Exception {
        Stage stage = new Stage();
        Reply reply = new Reply(emailV);

        reply.start(stage, accountName);
    }

    public void replyAll() throws IOException {
        Stage stage = new Stage();
        Reply reply = new Reply(emailV);

        reply.startAll(stage, accountName);
    }

    public void forward() throws IOException {
        Stage stage = new Stage();
        Reply reply = new Reply(emailV);

        reply.startLoaded(stage, accountName);
    }
}
