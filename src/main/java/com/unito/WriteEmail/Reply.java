package com.unito.WriteEmail;

import com.unito.ClientMain.Email;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Reply extends Application {
    public Email email;
    public Reply() {
    }

    public Reply(Email email) {
        this.email = email;
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Reply.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Reply");
        stage.setScene(scene);
        stage.show();

        ReplyController replyController = fxmlLoader.getController();
        replyController.loadEmail(email);
    }
    
}