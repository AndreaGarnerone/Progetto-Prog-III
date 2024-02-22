package com.unito.prog3.Client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClientModel {
    private final ObservableList<String> mailList;

    public ClientModel() {
        mailList = FXCollections.observableArrayList(
                "Mail 1", "Mail 2", "Mail 3", "Mail 4", "Mail 5"
        );
    }

    public ObservableList<String> getMailList() {
        return mailList;
    }


}

