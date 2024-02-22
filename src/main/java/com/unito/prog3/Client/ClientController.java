package com.unito.prog3.Client;

import com.unito.prog3.WriteEmail.WriteEmailView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientController {
    private ClientModel clientModel;
    public ComboBox accountComboBox;
    public Button writeEmailButton;
    public ListView mailListView;
    public RadioButton AllMail, toRead, Sent;
    public ToggleGroup selector;


    // No-argument constructor
    public ClientController() {
    }

    @FXML
    public void initialize() {

    }

    @FXML
    private void handleWriteEmailButton(ActionEvent event) throws IOException {
        WriteEmailView newMail = new WriteEmailView();
        Stage stage = new Stage();

        newMail.openWriter(stage);
    }

    public void handleRadioButtonAction(ActionEvent event) {
        /* Esempio
        if (AllMail.isSelected()) {
            myLabel.setText(AllMail.getText());
        } else if (toRead.isSelected()) {
            myLabel.setText(toRead.getText());
        } else if (Sent.isSelected()) {
            myLabel.setText(Sent.getText());
        }
         */
    }
}
