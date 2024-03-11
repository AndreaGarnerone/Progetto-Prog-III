package com.unito.ShowEmail;

import com.unito.ClientMain.Email;
import com.unito.WriteEmail.Reply;
import javafx.event.ActionEvent;
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

    public void initialize(String emailJson) {
        Email email = Email.fromString(emailJson);
        emailV = email;

        if (email != null) {
            fromField.setText(email.getFrom());
            subjectField.setText(email.getSubject());
            messageField.setText(email.getContent());
        } else {
            System.err.println("Failed to parse email JSON.");
        }
    }

    public void reply(ActionEvent event) throws Exception {
        Stage stage = new Stage();
        Reply reply = new Reply(emailV);

        reply.start(stage);
    }

    public void replyAll() {
    }
}
