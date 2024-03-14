package com.unito.ClientMain;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import com.unito.WriteEmail.WriteEmailController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.Socket;

public class ClientModel {
    private final ObservableList<Email> mailList;
    Socket socket = null;
    ObjectOutputStream outputStream;
    ObjectInputStream inputStream;

    WriteEmailController writeEmailController = new WriteEmailController();
    ClientController clientController = new ClientController();

    public ClientModel(String account) {
        mailList = FXCollections.observableArrayList();
    }

    public ObservableList<Email> getMailList() {
        return mailList;
    }


    // Add an email to the list
    public void addEmail(Email email) {
        boolean ok = true;
        if (!email.getToAll().equals(email.getToFirst())) { // Check if there are error in the receiver
            String[] recipients = email.getToAll().split(";");
            for (String recipient : recipients) {
                String filePath = "MailStorage/" + recipient + ".json";
                File file = new File(filePath);
                if (!file.exists()) {
                    writeEmailController.setErrorText();
                    ok = false;
                }
            }
        }

        if (ok) {
            saveToSender(email);
            sendToServer(email);
        }
    }

    // Save the email in the sender json file in the emailSent array
    private void saveToSender(Email email) {
        String emailJson = email.toJson();
        try {
            // Read JSON file
            FileReader reader = new FileReader("MailStorage/" + email.getFrom() + ".json");
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
            reader.close();

            // Add email to "emailSent" array
            JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
            JsonArray emailSentArray = jsonObject.getAsJsonArray("emailSent");
            emailSentArray.add(emailJson);

            // Write updated JSON back to the file
            FileWriter writer = new FileWriter("MailStorage/" + email.getFrom() + ".json");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(jsonArray, writer);
            writer.close();

            System.out.println("Email saved to sender successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save email to sender.");
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
        String hostName = "127.0.0.1";
        int port = 27;
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
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                //JSONArray emailSentArray = mainJson.getJSONArray("emailSent");
                //addEmailsToList(emailSentArray);

                // Process "emailReceived" array
                JSONArray emailReceivedArray = mainJson.getJSONArray("emailReceived");

                // Wrap UI update inside Platform.runLater()
                Platform.runLater(() -> {
                    addEmailsToList(emailReceivedArray);
                });
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void addEmailsToList(JSONArray jsonArray) {
        for (Object obj : jsonArray) {
            String jsonString = (String) obj;
            Email email = Email.fromString(jsonString);

            // Check if the email already exists in mailList. Add only the new emails
            boolean emailExists = false;
            for (Email existingEmail : mailList) {
                if (existingEmail.equals(email)) {
                    emailExists = true;
                    break;
                }
            }

            // Add the email to mailList only if it doesn't already exist
            if (!emailExists) {
                mailList.add(0, email);
            }
        }
    }


    //---------------------------- Send the email on refresh ----------------------------//

    // Send the request to the server for obtaining the emails still not read
    public void refresh(String selectedAccount) {
        try {
            connectToServer();

            outputStream.writeObject(selectedAccount);
            outputStream.flush();

            receiveNewEmail(selectedAccount);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

        loadEmailsFromFile(selectedAccount);
    }

    private void receiveNewEmail(String selectedAccount) throws IOException {
        try {
            // Read the string representation of JsonArray
            String jsonString = (String) inputStream.readObject();

            // Convert the string to JsonArray
            JsonArray emailList = JsonParser.parseString(jsonString).getAsJsonArray();

            if (!emailList.isEmpty()) {
                saveEmail(emailList, selectedAccount);
                clientController.showEmailNotification(selectedAccount);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveEmail(JsonArray emailList, String selectedAccount) {
        try {
            String pathName = "MailStorage/" + selectedAccount + ".json";

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

