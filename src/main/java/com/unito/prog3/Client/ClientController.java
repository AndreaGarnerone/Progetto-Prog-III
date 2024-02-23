package com.unito.prog3.Client;

import com.unito.prog3.WriteEmail.WriteEmailView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

public class ClientController {
    private ClientModel clientModel;
    @FXML
    public ComboBox accountComboBox;
    public Button writeEmailButton;
    @FXML
    public ListView mailListView;
    public RadioButton AllMail, toRead, Sent;
    public ToggleGroup selector;


    // No-argument constructor
    public ClientController() {
    }

    @FXML
    public void initialize() {
        clientModel = new ClientModel();

        mailListView.setCellFactory(new Callback<ListView<Email>, ListCell<Email>>() {
            @Override
            public ListCell<Email> call(ListView<Email> param) {
                return new EmailVisualizer();
         }
});
        mailListView.setItems(clientModel.getMailList());

        clientModel.addEmail(new Email("me", "you", "prova", "uiregnf", "12"));
    }

    @FXML
    private void handleWriteEmailButton(ActionEvent event) throws IOException {
        WriteEmailView newMail = new WriteEmailView();
        Stage stage = new Stage();

        newMail.openWriter(stage);
    }

    public void prova () {
        clientModel.addEmail(new Email("you", "me", "prova", "uiregnf", "12"));
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
