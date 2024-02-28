package com.unito.prog3.Client;

import com.unito.prog3.ShowEmail.ShowEmail;
import com.unito.prog3.ShowEmail.ShowEmailController;
import com.unito.prog3.WriteEmail.WriteEmailController;
import com.unito.prog3.WriteEmail.WriteEmailView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

public class ClientController {
    @FXML
    public Label setAccountName;
    @FXML
    private ClientModel clientModel;
    @FXML
    public ComboBox accountSelector;
    @FXML
    public Button writeEmailButton;
    @FXML
    public ListView mailListView;
    private ShowEmailController showEmailController;
    @FXML
    public RadioButton AllMail, toRead, Sent;
    @FXML
    public ToggleGroup selector;

    private String accountName;


    // No-argument constructor
    public ClientController() {
    }

    @FXML
    public void initialize(String selectedAccount) {
        clientModel = new ClientModel(selectedAccount);
        accountName = selectedAccount;

        mailListView.setCellFactory(param -> new EmailVisualizer());
        mailListView.setItems(clientModel.getMailList());

        setAccountName.setText(selectedAccount);

        mailListView.setOnMouseClicked(this::handleEmailDoubleClick);
    }

    @FXML
    private void writeNewEmail() throws IOException {
        WriteEmailView writeEmailView = new WriteEmailView();
        Stage stage = new Stage();

        writeEmailView.openWriter(stage, clientModel, accountName);
    }

    // Change from "All Mail" to other viewer
    public void changeListView(ActionEvent event) {
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

    private void handleEmailDoubleClick(javafx.scene.input.MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
            Email selectedEmail = (Email) mailListView.getSelectionModel().getSelectedItem();
            if (selectedEmail != null && showEmailController != null) {
                String email = selectedEmail.toJson();
                showEmailController.initialize(email); // Pass selected email to ShowEmailController
            }
        }
    }

}