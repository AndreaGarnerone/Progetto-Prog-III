package com.unito.Server;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class ServerController {
    public ServerApplication serverApplication;
    @FXML
    public ListView<String> listView;
    private ServerModel serverModel; // declare ServerModel here

    public void setServerModel(ServerModel serverModel) {
        this.serverModel = serverModel;
    }

    public void initialize() {
        listView.setCellFactory(param -> new LogVisualizer());
        if (serverModel != null) {
            listView.setItems(serverModel.getEventLog());
        }
    }
}

