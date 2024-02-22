package com.unito.prog3.WriteEmail;

import com.unito.prog3.Client.ClientApplication;
import com.unito.prog3.Client.ClientModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WriteEmailView {

    public void openWriter(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(WriteEmailView.class.getResource("WriteEmail.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Mail Client");
        stage.setScene(scene);
        stage.show();
    }
}
