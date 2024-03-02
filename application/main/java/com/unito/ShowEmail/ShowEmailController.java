package com.unito.ShowEmail;

import com.unito.ClientMain.Email;
import javafx.scene.control.Label;

public class ShowEmailController {
    public Label fromField;
    public Label subjectField;
    public Label messageField;

    public void initialize(String emailJson) {
        Email email = Email.fromString(emailJson);

        if (email != null) {
            fromField.setText(email.getFromAll());
            subjectField.setText(email.getSubject());
            messageField.setText(email.getContent());
        } else {
            System.err.println("Failed to parse email JSON.");
        }
    }

}
