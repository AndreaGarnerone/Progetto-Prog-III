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

    public void initialize(String emailJson) {
        Email email = Email.fromString(emailJson);

        if (email != null) {
            fromField.setText(email.getFrom());
            subjectField.setText(email.getSubject());
            messageField.setText(email.getContent());
        } else {
            System.err.println("Failed to parse email JSON.");
        }
    }

    public void reply(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Reply reply = new Reply();

        reply.start(stage);
    }

    public void replyAll(ActionEvent event) {
    }
}
