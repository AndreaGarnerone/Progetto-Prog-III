package com.unito.prog3.Client;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

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
