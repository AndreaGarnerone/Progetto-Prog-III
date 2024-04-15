package com.unito.Client;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
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
    boolean isSent = false;
    private final ObservableList<Email> mailList;
    Socket socket = null;
    ObjectOutputStream outputStream;
    ObjectInputStream inputStream;

    ClientController clientController = new ClientController();

    public ClientModel(String account) {
        mailList = FXCollections.observableArrayList();
        loadEmailsFromFile(account, "emailReceived");
    }

    public ObservableList<Email> getMailList() {
        return mailList;
    }


    /**
     * Add an email to the list
     * @param email The email to be added
     */
    public void addEmail(Email email) {
        if (sendToServer(email)) {
            saveToSender(email);
        }
    }

    /**
     * Save the email in the sender json file in the emailSent array
     * @param email The email to be saved
     */
    private void saveToSender(Email email) {
        String emailJson = email.toJson();
        try {
            // Read JSON file
            FileReader reader = new FileReader("src/main/java/com/unito/Client/MailStorage/" + email.getFrom() + ".json");
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
            reader.close();

            // Add email to "emailSent" array
            JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
            JsonArray emailSentArray = jsonObject.getAsJsonArray("emailSent");
            emailSentArray.add(emailJson);

            // Write updated JSON back to the file
            FileWriter writer = new FileWriter("src/main/java/com/unito/Client/MailStorage/" + email.getFrom() + ".json");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(jsonArray, writer);
            writer.close();

            System.out.println("Email saved to sender successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save email to sender.");
        }
    }

    /**
     * Send the email to the server to store it
     * @param email The email to be saved
     * @return true if the email is sent correctly, false otherwise
     */
    private boolean sendToServer(Email email) {
        boolean sent = true;
        try {
            connectToServer();
            sendEmail(email);
        } catch (NullPointerException | IOException e) {
            clientController.showAlert();
            sent = false;
        } finally {
            closeConnection();
        }
        return sent;
    }

    /**
     * Create a socket connection with the server
     */
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

    /**
     * Send the email via the socket connection
     * @param email The email to be sent
     */
    private void sendEmail(Email email) throws IOException {
        outputStream.writeObject(email);
        outputStream.flush();
    }

    /**
     * Close the socket after the email is sent
     */
    private void closeConnection() {
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (NullPointerException | IOException e) {
            //clientController.showAlert(Alert.AlertType.ERROR, "Connection Error", "Server Down", "Unable to connect to the server.");
        }
    }

    /**
     * It selects the received or sent mail list
     * @param account The user account
     * @param selector The selector value. Possible values: emailReceived, emailSent
     */
    private void loadEmailsFromFile(String account, String selector) {
        String filePath = "src/main/java/com/unito/Client/MailStorage/" + account + ".json";
        try {
            FileReader fileReader = new FileReader(filePath);
            JSONTokener jsonTokener = new JSONTokener(fileReader);
            JSONArray mainArray = new JSONArray(jsonTokener);

            for (int i = 0; i < mainArray.length(); i++) {
                JSONObject mainJson = mainArray.getJSONObject(i);

                JSONArray emailReceivedArray = mainJson.getJSONArray(selector);

                Platform.runLater(() -> addEmailsToList(emailReceivedArray));
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load the email sent list
     * @param selectedAccount The user account
     */
    public void viewSent(String selectedAccount) {
        mailList.clear();
        loadEmailsFromFile(selectedAccount, "emailSent");
        isSent = true;
    }
    /**
     * Load the received email list
     * @param selectedAccount The user account
     */
    public void viewReceived(String selectedAccount) {
        mailList.clear();
        loadEmailsFromFile(selectedAccount, "emailReceived");
        isSent = false;
    }

    /**
     * Save the email to the list
     * @param jsonArray The list off the mails
     */
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

    /**
     * Send the request to the server for obtaining the emails still not read
     * @param selectedAccount The user account
     */
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
                    ex.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        } finally {
            closeConnection();
        }

        if (!isSent) loadEmailsFromFile(selectedAccount, "emailReceived");
    }

    /**
     * Send a message to the server for the server log
     * @param s The parameter. Values: new or close
     */
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

    /**
     * Receive a new email and store it
     * @param selectedAccount The account of the user
     */
    private void receiveNewEmail(String selectedAccount) throws IOException {
        try {
            String jsonString = (String) inputStream.readObject();

            JsonArray emailList = JsonParser.parseString(jsonString).getAsJsonArray();

            if (!emailList.isEmpty()) {
                saveEmail(emailList, selectedAccount);
                clientController.showEmailNotification();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Actually save the email to the correct file
     * @param emailList The JSon rappresentation of the email list
     * @param selectedAccount The account of the user
     */
    private void saveEmail(JsonArray emailList, String selectedAccount) {
        try {
            String pathName = "src/main/java/com/unito/Client/MailStorage/" + selectedAccount + ".json";

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

