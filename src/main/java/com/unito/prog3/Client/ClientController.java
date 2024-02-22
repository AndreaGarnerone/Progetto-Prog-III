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
    public ComboBox accountComboBox;
    public Button writeEmailButton;
    public ListView mailListView;
    public ToggleGroup toggleGroup;
    public RadioButton AllMail, toRead, Sent;
    public ToggleGroup selector;
    private ClientModel model;
    private Label myLabel;
    private VBox radioButtonContainer;


    // No-argument constructor
    public ClientController() {
    }

    @FXML
    public void initialize() {

        // Set default value
        accountComboBox.setValue("Account 1");

        // Add items to the ComboBox
        accountComboBox.setItems(FXCollections.observableArrayList(
                "Account 1",
                "Account 2",
                "Account 3"
        ));
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
