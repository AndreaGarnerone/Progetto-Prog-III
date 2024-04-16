package com.unito.Client.WriteEmail;

import com.unito.Client.Email;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ReplyView extends Application {
    public Email email;

    public ReplyView() {
    }

    @Override
    public void start(Stage stage) throws Exception {
    }

    /**
     * Constructor
     *
     * @param email The email
     */
    public ReplyView(Email email) {
        this.email = email;
    }

    /**
     * Call the reply page loader
     *
     * @param selectedAccount The user account
     */
    public void start(Stage stage, String selectedAccount) throws IOException {
        loader(stage, 0, selectedAccount);
    }

    /**
     * Call the reply all page loader
     *
     * @param selectedAccount The user account
     */
    public void startAll(Stage stage, String selectedAccount) throws IOException {
        loader(stage, 1, selectedAccount);
    }

    /**
     * Call the forward page loader
     *
     * @param selectedAccount The user account
     */
    public void startLoaded(Stage stage, String selectedAccount) throws IOException {
        forwardLoaded(stage, selectedAccount);
    }

    /**
     * Load the reply or reply all page
     *
     * @param selectedAccount The user account
     */
    private void forwardLoaded(Stage stage, String selectedAccount) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Forward.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 200);
        stage.setTitle("Forward");
        stage.setScene(scene);
        stage.show();

        ReplyController replyController = fxmlLoader.getController();
        replyController.loadForward(email, selectedAccount);
    }

    /**
     * Load the email reply page
     *
     * @param option          0: reply, 1: reply all
     * @param selectedAccount The user account
     */
    private void loader(Stage stage, int option, String selectedAccount) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Reply.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Reply");
        stage.setScene(scene);
        stage.show();

        ReplyController replyController = fxmlLoader.getController();
        if (option == 0) {
            replyController.loadEmail(email, selectedAccount);
        } else {
            replyController.loadEmailAll(email, selectedAccount);
        }
    }

}