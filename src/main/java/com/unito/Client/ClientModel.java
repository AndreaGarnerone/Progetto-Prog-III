package com.unito.Client;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import com.unito.Client.WriteEmail.WriteEmailController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
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
        loadEmailsFromFile(account, "emailReceived");
    }

    public ObservableList<Email> getMailList() {
        return mailList;
    }


    // Add an email to the list
    public void addEmail(Email email) {
        if (sendToServer(email)) {
            // Save the mail to the sender only if the connection was established correctly
            saveToSender(email);
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

    private boolean sendToServer(Email email) {
        boolean sent = true;
        try {
            connectToServer();
            sendEmail(email);
        } catch (NullPointerException | IOException e) {
            clientController.showAlert(Alert.AlertType.ERROR, "Connection Error", "Server Down", "Unable to connect to the server.");
            sent = false;
        } finally {
            closeConnection();
        }
        return sent;
    }

    // Create a socket connection with the server
    private void connectToServer() {
        String hostName = "127.0.0.1";
        int port = 27;
        try {
            socket = new Socket(hostName, port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            //clientController.showAlert(Alert.AlertType.ERROR, "Connection Error", "Server Down", "Unable to connect to the server.");
        }
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
        } catch (NullPointerException | IOException e) {
            //clientController.showAlert(Alert.AlertType.ERROR, "Connection Error", "Server Down", "Unable to connect to the server.");
        }
    }

    private void loadEmailsFromFile(String account, String selector) {
        String filePath = "src/main/java/com/unito/Client/MailStorage/" + account + ".json";
        try {
            // Read JSON file
            FileReader fileReader = new FileReader(filePath);
            JSONTokener jsonTokener = new JSONTokener(fileReader);
            JSONArray mainArray = new JSONArray(jsonTokener);

            // Loop through the main array
            for (int i = 0; i < mainArray.length(); i++) {
                JSONObject mainJson = mainArray.getJSONObject(i);

                // Process "emailReceived" array
                JSONArray emailReceivedArray = mainJson.getJSONArray(selector);

                // Wrap UI update inside Platform.runLater()
                Platform.runLater(() -> addEmailsToList(emailReceivedArray));
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void viewSent(String selectedAccount) {
        mailList.clear();
        loadEmailsFromFile(selectedAccount, "emailSent");
    }

    public void viewReceived(String selectedAccount) {
        mailList.clear();
        loadEmailsFromFile(selectedAccount, "emailReceived");
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
            boolean retry = true;
            while (retry) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    // Handle interruption if needed
                    ex.printStackTrace();
                    // Resume waiting if interrupted
                    // Interrupt the current thread again to maintain interrupted status
                    Thread.currentThread().interrupt();
                }
            }
        } finally {
            closeConnection();
        }

        loadEmailsFromFile(selectedAccount, "emailReceived");
    }

    public void sendString(String s) throws IOException {
        connectToServer();

        try {
            if (outputStream != null) { // Check if outputStream is not null
                outputStream.writeObject(s);
                outputStream.flush();
            }
        } catch (NullPointerException ignored) {
            // Handle any NullPointerException if needed
        } finally {
            closeConnection();
        }
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

