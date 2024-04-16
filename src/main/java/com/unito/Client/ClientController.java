package com.unito.Client;

import com.google.gson.*;
import com.unito.Client.ShowEmail.ShowEmailController;
import com.unito.Client.WriteEmail.WriteEmailView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ClientController {
    private ClientModel clientModel;
    @FXML
    public Label setAccountName;
    @FXML
    public Button writeEmailButton;
    @FXML
    public ListView mailListView;
    @FXML
    public RadioButton Received, Sent;
    @FXML
    public ToggleGroup selector;
    private String accountName;
    private boolean isReceived = true;

    /**
     * Constructor
     */
    public ClientController() {
    }

    /**
     * Initialize the necessary elements for the application
     *
     * @param selectedAccount The user account
     */
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

    /**
     * Open the writer for writing a new email
     */
    @FXML
    private void writeNewEmail() throws IOException {
        WriteEmailView writeEmailView = new WriteEmailView();
        Stage stage = new Stage();

        writeEmailView.openWriter(stage, clientModel, accountName);
    }

    /**
     * Visualize the selected email
     */
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

    /**
     * Remove an email from the file
     */
    @FXML
    private void deleteEmail() throws IOException {
        Email selectedEmail = (Email) mailListView.getSelectionModel().getSelectedItem();
        JsonObject jsonObject = readJSON().get(0).getAsJsonObject(); // Accessing the first element of the array

        mailListView.getItems().remove(selectedEmail);
        if (isReceived) {
            JsonArray emailReceived = jsonObject.getAsJsonArray("emailReceived");
            removeEmail(selectedEmail.getContent(), emailReceived);
        } else {
            JsonArray emailSent = jsonObject.getAsJsonArray("emailSent");
            removeEmail(selectedEmail.getContent(), emailSent);
        }

        JsonArray jsonArray = new JsonArray();
        jsonArray.add(jsonObject);

        writeJSON(jsonArray);
    }

    /**
     * Find the email to remove in the json array
     *
     * @param keyForRemoval Key value for find the correct email to remove
     * @param emailArray    The array containing the mail
     */
    private void removeEmail(String keyForRemoval, JsonArray emailArray) {
        for (int i = 0; i < emailArray.size(); i++) {
            String emailJsonString = emailArray.get(i).getAsString();
            JsonObject emailObject = JsonParser.parseString(emailJsonString).getAsJsonObject();
            String timestamp = emailObject.get("content").getAsString();
            if (timestamp.equals(keyForRemoval)) {
                emailArray.remove(i);
                break;
            }
        }
    }

    /**
     * Read the json for sending the email received to the client
     *
     * @return the JSon mail file
     */
    private JsonArray readJSON() throws IOException {
        String filepath = "src/main/java/com/unito/Client/MailStorage/" + accountName + ".json";
        FileReader reader = new FileReader(filepath);
        JsonElement jsonElement = JsonParser.parseReader(reader);
        return jsonElement.getAsJsonArray();
    }

    /**
     * After receiving a mail it stores it in the JSon file
     */
    private void writeJSON(JsonArray jsonArray) throws IOException {
        String filepath = "src/main/java/com/unito/Client/MailStorage/" + accountName + ".json";
        FileWriter writer = new FileWriter(filepath);
        new Gson().toJson(jsonArray, writer);
        writer.close();
    }

    /**
     * Send a notification to the user if arrive a new email
     */
    public void showEmailNotification() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("New Email Notification");
            alert.setHeaderText("New Email Received");
            alert.showAndWait();
        });
    }

    /**
     * Actually show the alert
     */
    void showAlert() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection Error");
            alert.setHeaderText("Server Down");
            alert.setContentText("Unable to connect to the server.");
            alert.showAndWait();
        });
    }

    /**
     * Close the applications
     */
    void close() {
        try {
            clientModel.sendString("close");
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    /**
     * Refresh the email list
     */
    @FXML
    public void refreshEmail() {
        clientModel.refresh(accountName);
    }

    /**
     * Change from the email sent view to the email received view
     */
    public void changeListViewReceived() {
        clientModel.loadReceived(accountName);
        isReceived = true;
    }

    /**
     * Change from the email received view to the email sent view
     */
    public void changeListViewSent() {
        clientModel.loadSent(accountName);
        isReceived = false;
    }

}
