package com.unito.prog3.Client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;

public class ClientModel {
    private final ObservableList<Email> mailList;

    public ClientModel(String account) {
        mailList = FXCollections.observableArrayList();
        loadEmailsFromFile(account);
    }

    public ObservableList<Email> getMailList() {
        return mailList;
    }

    public void addEmail(Email email) {
        mailList.add(0, email);
        saveEmailsToFile(email); // Save email to file when added
    }

    private void loadEmailsFromFile(String account) {
        try (BufferedReader reader = new BufferedReader(new FileReader("MailStorage/"+account))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Email email = Email.fromString(line); // Use Email.fromString method to create email from JSON
                mailList.add(email);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveEmailsToFile(Email emailName) {
        String fileName = emailName.getToFirst();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("MailStorage/"+fileName))) {
            for (Email email : mailList) {
                writer.write(email.toJson()); // Convert email to JSON format and write to file
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

