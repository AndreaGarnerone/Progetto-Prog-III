package com.unito.prog3.WriteEmail;

import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class WriteEmailController {
    public TextField toField;
    public TextField subjectField;
    public TextArea messageField;

    public void sendEmail(ActionEvent event) {
    }

    public void clearFields(ActionEvent event) {
        toField.clear();
        subjectField.clear();
        messageField.clear();
    }
}
