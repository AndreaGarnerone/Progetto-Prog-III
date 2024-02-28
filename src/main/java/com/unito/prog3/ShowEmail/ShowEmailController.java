package com.unito.prog3.ShowEmail;

import com.unito.prog3.Client.Email;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ShowEmailController {
    public TextField fromField;
    public TextField subjectField;
    public TextArea messageField;

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
