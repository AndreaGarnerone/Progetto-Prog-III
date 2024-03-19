package com.unito.ClientMain;

import com.unito.ShowEmail.ShowEmailController;
import com.unito.WriteEmail.WriteEmailView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ClientController {
    @FXML
    public Label setAccountName;
    private ClientModel clientModel;
    @FXML
    public ComboBox accountSelector;
    @FXML
    public Button writeEmailButton;
    @FXML
    public ListView mailListView;
    @FXML
    public RadioButton Received, Sent;
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

        try {
            clientModel.sendString("new");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void close() {
        try {
            clientModel.sendString("close");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void writeNewEmail() throws IOException {
        WriteEmailView writeEmailView = new WriteEmailView();
        Stage stage = new Stage();

        writeEmailView.openWriter(stage, clientModel, accountName);
    }

    //Visualize an email
    private void handleEmailDoubleClick(javafx.scene.input.MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
            Email selectedEmail = (Email) mailListView.getSelectionModel().getSelectedItem();
            if (selectedEmail != null) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ShowEmail.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                    Stage primaryStage = new Stage();
                    primaryStage.setTitle("Show Email");
                    primaryStage.setScene(scene);
                    primaryStage.show();

                    ShowEmailController showEmailController = fxmlLoader.getController();
                    showEmailController.initialize(selectedEmail.toJson(), accountName);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Remove an email from the file
    @FXML
    private void deleteEmail() {
        Email selectedEmail = (Email) mailListView.getSelectionModel().getSelectedItem();
        if (selectedEmail != null) {
            mailListView.getItems().remove(selectedEmail);
        }
    }

    public void showEmailNotification(String sender) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("New Email Notification");
            alert.setHeaderText("New Email Received");
            alert.setContentText("From: " + sender);
            alert.showAndWait();
        });
    }

    void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }


    // Refresh the email list
    @FXML
    public void refreshEmail(ActionEvent event) {
        clientModel.refresh(accountName);
    }

    public void changeListViewReceived(ActionEvent event) {
        clientModel.viewReceived(accountName);
    }

    public void changeListViewSent(ActionEvent event) {
        clientModel.viewSent(accountName);
    }

}
