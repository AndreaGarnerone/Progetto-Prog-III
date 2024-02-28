package com.unito.prog3.Client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.*;

public class ClientModel {
    private ObservableList<Email> mailList;

    public ClientModel(String account) {
        mailList = FXCollections.observableArrayList();
        loadEmailsFromFile(account);
    }

    public ObservableList<Email> getMailList() {
        return mailList;
    }

    public void addEmail(Email email) {
        saveEmailsToFile(email);
        //mailList.add(0, email);
    }

    public void saveEmailsToFile(Email email) {
        String filePath = "MailStorage/" + email.getToFirst() + ".json";

        try {
            // Read existing JSON array from file or create a new one if file doesn't exist
            JSONArray jsonArray;
            File file = new File(filePath);
            if (file.exists()) {
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(new FileReader(filePath));
                jsonArray = (JSONArray) obj;
            } else {
                jsonArray = new JSONArray();
            }

            // Convert Email object to JSONObject
            JSONObject emailJson = new JSONObject();
            emailJson.put("from", email.getFromAll());
            emailJson.put("to", email.getToAll());
            emailJson.put("subject", email.getSubject());
            emailJson.put("content", email.getContent());
            emailJson.put("timestamp", email.getTimestamp());

            // Add email JSONObject to the JSONArray
            jsonArray.add(emailJson);

            // Write updated JSONArray back to the file
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(jsonArray.toJSONString());
            fileWriter.flush();
            fileWriter.close();

            System.out.println("Email saved successfully.");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void loadEmailsFromFile(String account) {
        String filePath = "MailStorage/" + account + ".json";
        try {
            // Read JSON file
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(new BufferedReader(new FileReader(filePath)));

            // Iterate over JSON array and convert each element to Email object
            for (Object obj : jsonArray) {
                JSONObject jsonEmail = (JSONObject) obj;
                String from = (String) jsonEmail.get("from");
                String to = (String) jsonEmail.get("to");
                String subject = (String) jsonEmail.get("subject");
                String content = (String) jsonEmail.get("content");
                String timestamp = (String) jsonEmail.get("timestamp");
                mailList.add(new Email(from, to, subject, content, timestamp));
            }

            System.out.println("Emails loaded successfully.");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

}

