package com.unito.Server;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ServerController {
    @FXML
    public TextArea logTextArea;

    @FXML
    public void log(String message) {
        logTextArea.appendText(message + "\n");
    }
}
