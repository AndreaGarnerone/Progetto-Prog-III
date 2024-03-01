package Client.ClientMain;

import Client.ShowEmail.ShowEmailController;
import Client.WriteEmail.WriteEmailView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.io.IOException;

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
                    showEmailController.initialize(selectedEmail.toJson());

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
            clientModel.removeEmail(selectedEmail);
            // Optionally, you can refresh the mailListView after deletion
            mailListView.getItems().remove(selectedEmail);
        }
    }


}