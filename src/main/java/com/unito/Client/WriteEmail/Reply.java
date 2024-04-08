package com.unito.Client.WriteEmail;

import com.unito.Client.Email;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Reply extends Application {
    public Email email;
    public Reply() {
    }

    @Override
    public void start(Stage stage) throws Exception {

    }

    public Reply(Email email) {
        this.email = email;
    }

    public void start(Stage stage, String selectedAccount) throws Exception {
        loader(stage, 0, selectedAccount);
    }

    public void startAll(Stage stage, String selectedAccount) throws IOException {
        loader(stage, 1, selectedAccount);
    }

    public void startLoaded(Stage stage, String selectedAccount) throws IOException {
        forwardLoaded(stage, selectedAccount);
    }

    private void forwardLoaded(Stage stage, String selectedAccount) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Forward.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 200);
        stage.setTitle("Forward");
        stage.setScene(scene);
        stage.show();

        ReplyController replyController = fxmlLoader.getController();
        replyController.loadForward(email, selectedAccount);
    }

    private void loader(Stage stage, int option, String selectedAccount) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Reply.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Reply");
        stage.setScene(scene);
        stage.show();

        ReplyController replyController = fxmlLoader.getController();
        if (option == 0) {
            replyController.loadEmail(email);
        } else {
            replyController.loadEmailAll(email, selectedAccount);
        }
    }

}