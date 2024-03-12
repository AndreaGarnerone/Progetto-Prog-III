package com.unito.Server;

import javafx.scene.control.ListCell;

public class LogVisualizer extends ListCell<String> {
    @Override
    protected void updateItem(String log, boolean empty) {
        super.updateItem(log, empty);

        if (empty || log == null) {
            setText(null);
        } else {
            setText(log);
        }
    }
}
