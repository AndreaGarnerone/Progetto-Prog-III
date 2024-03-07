package com.unito.ClientMain;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import com.unito.WriteEmail.WriteEmailController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.Socket;

public class ClientModel {
    private ObservableList<Email> mailList;

    private String hostName = "127.0.0.1";
    private int port = 27;
    Socket socket = null;
    ObjectOutputStream outputStream = null;
    ObjectInputStream inputStream = null;

    WriteEmailController writeEmailController = new WriteEmailController();

    public ClientModel(String account) {
        mailList = FXCollections.observableArrayList();
        loadEmailsFromFile(account);
    }

    public ObservableList<Email> getMailList() {
        return mailList;
    }


    //Add an email to the list
    public void addEmail(Email email) throws IOException, ClassNotFoundException {
        String filePath = "MailStorage/" + email.getToFirst() + ".json";
        String emailJson = null;

        File file = new File(filePath);
        if (!file.exists()) {
            writeEmailController.setErrorText();
        } else {
            sendToServer(email);
        }
    }

    private void sendToServer(Email email) {
        try {
            connectToServer();
            sendEmail(email);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    // Create a socket connection with the server
    private void connectToServer() throws IOException {
        socket = new Socket(hostName, port);

        outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.flush();

        inputStream = new ObjectInputStream(socket.getInputStream());
    }

    // Send the email via the socket connection
    private void sendEmail(Email email) throws IOException {
        outputStream.writeObject(email);
        outputStream.flush();
    }

    private void closeConnection() {
        if (socket != null) {
            try {
                inputStream.close();
                outputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeEmail(Email email) {
        String filePath = "MailStorage/" + email.getToFirst() + ".json";

    }

    private void loadEmailsFromFile(String account) {
        String filePath = "MailStorage/" + account + ".json";
        try {
            // Read JSON file
            FileReader fileReader = new FileReader(filePath);
            JSONTokener jsonTokener = new JSONTokener(fileReader);
            JSONArray mainArray = new JSONArray(jsonTokener);

            // Loop through the main array
            for (int i = 0; i < mainArray.length(); i++) {
                JSONObject mainJson = mainArray.getJSONObject(i);

                // Process "emailSent" array
                JSONArray emailSentArray = mainJson.getJSONArray("emailSent");
                addEmailsToList(emailSentArray);

                // Process "emailReceived" array
                JSONArray emailReceivedArray = mainJson.getJSONArray("emailReceived");
                addEmailsToList(emailReceivedArray);
            }

            System.out.println("Emails loaded successfully.");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void addEmailsToList(JSONArray jsonArray) {
        // Iterate over JSON array and convert each element to Email object
        for (Object obj : jsonArray) {
            String jsonString = (String) obj;
            JSONObject jsonEmail = new JSONObject(jsonString);
            String from = jsonEmail.getString("from");
            String to = jsonEmail.getString("to");
            String subject = jsonEmail.getString("subject");
            String content = jsonEmail.getString("content");
            String timestamp = jsonEmail.getString("timestamp");
            mailList.add(0, new Email(from, to, subject, content, timestamp));
        }
    }



    //---------------------------- Send the email on refresh ----------------------------//

    // Send the request to the server for obtaining the emails still not read
    public void refresh() {
        String request = "Refresh";
        try {
            connectToServer();

            outputStream.writeObject(request);
            outputStream.flush();

            receiveNewEmail();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private void receiveNewEmail() throws IOException {
        try {
            // Read the string representation of JsonArray
            String jsonString = (String) inputStream.readObject();

            // Convert the string to JsonArray
            JsonArray emailList = JsonParser.parseString(jsonString).getAsJsonArray();

            System.out.println("Adesso salvo le mail");
            saveEmail(emailList);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void saveEmail(JsonArray emailList) {
        if (emailList.isEmpty()) {
            System.out.println("Email list is empty. No emails to save.");
            return;
        }
        try {
            String pathName = "MailStorage/carrapax@gormail.com.json";

            // Parse the existing JSON file
            JsonParser parser = new JsonParser();
            JsonArray jsonArray = parser.parse(new FileReader(pathName)).getAsJsonArray();
            JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();

            // Get the "emailToRead" array from the JSON object
            JsonArray emailToReadArray = jsonObject.getAsJsonArray("emailReceived");

            // Add each email from emailList to emailToReadArray
            for (int i = 0; i < emailList.size(); i++) {
                emailToReadArray.add(emailList.get(i));
            }

            // Write the modified JSON object back to the file
            try (JsonWriter writer = new JsonWriter(new FileWriter(pathName))) {
                writer.setIndent("  ");
                writer.beginArray();
                writer.jsonValue(jsonObject.toString());
                writer.endArray();
            }

            System.out.println("Data added successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

