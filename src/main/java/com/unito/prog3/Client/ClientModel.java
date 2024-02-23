package com.unito.prog3.Client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClientModel {
    private final ObservableList<Email> mailList;

    public ClientModel() {
        mailList = FXCollections.observableArrayList(
        );
    }

    public ObservableList<Email> getMailList() {
        return mailList;
    }

    public void addEmail(Email email) {
        mailList.add(0, email);
    }



}

