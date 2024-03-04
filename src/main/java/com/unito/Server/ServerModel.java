package com.unito.Server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.unito.ClientMain.Email;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.format.DateTimeFormatter;

public class ServerModel {
    private static final String filePath = "MailBank.json";
    private int port = 27;
    ServerSocket serverSocket;
    Socket socket = null;
    ObjectInputStream inputStream = null;
    ObjectOutputStream outputStream = null;


    public void listen() {
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                receiveEmail();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void receiveEmail() {
        try {
            socket = serverSocket.accept();

            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();

            Email receivedEmail = (Email) inputStream.readObject();

            storeEmail(receivedEmail);

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("------chiusura forzata server-----");
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private void closeConnection() {
        try {
            if(inputStream != null) {
                inputStream.close();
            }

            if(outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Save email to json file */
    public static void storeEmail(Email email) {
        try {
            // Read existing JSON file
            JsonObject jsonObject = readJSON();

            // Retrieve the "UserList" array
            JsonArray userList = jsonObject.getAsJsonArray("UserList");

            // Find the corresponding user and add email to their emailList
            for (int i = 0; i < userList.size(); i++) {
                JsonObject user = userList.get(i).getAsJsonObject();
                if (user.get("name").getAsString().equals(email.getToAll())) {
                    // Create JSON object for the new email
                    JsonObject newEmail = new JsonObject();
                    newEmail.addProperty("subject", email.getSubject());
                    newEmail.addProperty("from", email.getFromAll());
                    newEmail.addProperty("to", email.getToAll());
                    newEmail.addProperty("content", email.getContent());
                    newEmail.addProperty("timestamp", String.format(String.valueOf(DateTimeFormatter.ISO_INSTANT)));

                    // Add the new email to the emailList of the user
                    user.getAsJsonArray("emailList").add(newEmail);

                    // Write the updated JSON back to file
                    writeJSON(jsonObject);

                    return; // Exit method after storing email
                }
            }

            // If no corresponding user found, handle accordingly (maybe throw exception or log)
            System.out.println("No user found with email: " + email.getToAll());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JsonObject readJSON() throws IOException {
        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, JsonObject.class);
        }
    }

    private static void writeJSON(JsonObject jsonObject) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(jsonObject, writer);
        }
    }

}
