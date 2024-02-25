package com.unito.prog3.Client;

import com.unito.prog3.WriteEmail.WriteEmailController;
import com.unito.prog3.WriteEmail.WriteEmailView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    @FXML
    public RadioButton AllMail, toRead, Sent;
    @FXML
    public ToggleGroup selector;


    // No-argument constructor
    public ClientController() {
    }

    @FXML
    public void initialize(String selectedAccount) {
        clientModel = new ClientModel();

        mailListView.setCellFactory((Callback<ListView<Email>, ListCell<Email>>) param -> new EmailVisualizer());
        mailListView.setItems(clientModel.getMailList());

        setAccountName.setText(selectedAccount);

        //clientModel.addEmail(new Email("me", "you", "prova", "uiregnf", "12"));
    }

    @FXML
    private void writeNewEmail() throws IOException {
        WriteEmailView writeEmailView = new WriteEmailView();
        Stage stage = new Stage();

        // Pass the client model to the openWriter method
        WriteEmailController writeEmailController = writeEmailView.openWriter(stage, clientModel);
    }

    public void addEmail() {
        clientModel.addEmail(new Email("you", "me", "prova", "uiregnf", "12"));
    }

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

    public void setAccountName(String text) {

    }

    public void openClient() throws IOException {
        ClientApplication clientApplication = new ClientApplication();
        Stage stage = new Stage();
        clientApplication.start(stage);
    }

}