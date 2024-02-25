package com.unito.prog3.WriteEmail;

import com.unito.prog3.Client.ClientModel;
import com.unito.prog3.Client.Email;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;

public class WriteEmailController {
    @FXML
    public TextField toField;
    @FXML
    public TextField subjectField;
    @FXML
    public TextArea messageField;

    LocalDateTime currentDateTime = LocalDateTime.now();
    private ClientModel clientModel;

    public void setClientModel(ClientModel clientModel) {
        this.clientModel = clientModel;
    }

    public void sendEmail(ActionEvent event) {
        String from = "you"; // Assuming "you" is the sender
        String to = toField.getText();
        String subject = subjectField.getText();
        String content = messageField.getText();
        String timestamp = String.valueOf(currentDateTime);

        clientModel.addEmail(new Email(from, to, subject, content, timestamp));
    }

    public void clearFields(ActionEvent event) {
        toField.clear();
        subjectField.clear();
        messageField.clear();
    }
}
