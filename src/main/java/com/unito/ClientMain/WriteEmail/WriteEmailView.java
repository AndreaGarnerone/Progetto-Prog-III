package com.unito.ClientMain.WriteEmail;

import com.unito.ClientMain.ClientModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WriteEmailView {

    public void openWriter(Stage stage, ClientModel clientModel, String sender) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("WriteEmail.fxml"));
        Parent root = fxmlLoader.load();

        WriteEmailController writeEmailController = fxmlLoader.getController();
        writeEmailController.setClientModel(clientModel); // Set the client model
        writeEmailController.setSender(sender);

        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Write Email");
        stage.setScene(scene);
        stage.show();

    }
}
