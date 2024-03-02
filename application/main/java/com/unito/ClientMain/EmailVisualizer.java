package com.unito.ClientMain;

import javafx.scene.control.ListCell;

public class EmailVisualizer extends ListCell<Email> {

    @Override
    protected void updateItem(Email email, boolean empty) {
        super.updateItem(email, empty);

        if (empty || email == null) {
            setText(null);
        } else {
            setText(email.getFromAll() + " : " + email.getSubject() + " " + email.getContent());
        }
    }
}
