package com.unito.Server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.unito.ClientMain.Email;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ServerModel {
    private static final String filePath = "MailBank.json";
    private int port = 27;
    ServerSocket serverSocket;
    Socket socket = null;
    ObjectInputStream inputStream = null;
    ObjectOutputStream outputStream = null;


    /* Methods for receiving an email from a client */
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

            try {
                Object receivedObject = inputStream.readObject();
                if (receivedObject instanceof String && receivedObject.equals("Refresh")) {
                    refresh();
                    System.out.println("Refresh");
                } else if (receivedObject instanceof Email receivedEmail) {
                    System.out.println("Received email");
                    storeEmail(receivedEmail);
                } else System.out.println("NO");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                closeConnection();
            }

        } catch (IOException e) {
            System.out.println("Failed to establish connection");
            e.printStackTrace();
        }
    }


    private void closeConnection() {
        try {
            if (inputStream != null) {
                inputStream.close();
            }

            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //---------------------------- Store the email sent by the client ----------------------------//
    public static void storeEmail(Email email) {
        try {
            // Read the JSON file
            JsonObject jsonObject = readJSON();

            // Get the UserList array
            JsonArray userList = jsonObject.getAsJsonArray("UserList");

            // Find the user and update emailSent and emailReceived arrays
            for (int i = 0; i < userList.size(); i++) {
                JsonObject user = userList.get(i).getAsJsonObject();
                String name = user.get("name").getAsString();

                if (email.getFrom().equals(name)) {
                    JsonArray emailSent = user.getAsJsonArray("emailSent");
                    emailSent.add(email.toJson());
                }

                // Check if there are multiple receivers
                if (!email.getToAll().equals(email.getToFirst())) {
                    // Split the 'to' field into multiple recipients
                    String[] recipients = email.getToAll().split(";");

                    for (String recipient : recipients) {
                        for (int j = 0; j < userList.size(); j++) {
                            if (recipient.trim().equals(name)) {
                                JsonArray emailReceived = user.getAsJsonArray("emailReceived");
                                emailReceived.add(email.toJson());
                                break;
                            }
                        }
                    }

                } else {
                    if (email.getToAll().equals(name)) {
                        JsonArray emailReceived = user.getAsJsonArray("emailReceived");
                        emailReceived.add(email.toJson());
                    }
                }
            }

            // Write the updated JSON back to the file
            writeJSON(jsonObject);

            System.out.println("Email added successfully to the email bank");
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


    //---------------------------- Send the email on refresh ----------------------------//
    // Send the email on refresh
    public void refresh() {
        try {
            // Read existing JSON file
            JsonObject jsonObject = readJSON();

            // Retrieve the "UserList" array
            JsonArray userList = jsonObject.getAsJsonArray("UserList");

            // Iterate over users and send emails for "carrapax@gormail.com"
            for (int i = 0; i < userList.size(); i++) {
                JsonObject user = userList.get(i).getAsJsonObject();
                String userName = user.get("name").getAsString();
                if (userName.equals("carrapax@gormail.com")) {
                    // Retrieve email list for the user
                    JsonArray emailList = user.getAsJsonArray("emailReceived");

                    // Send emails to the client via socket
                    sendEmails(emailList);

                    // Clear the emailReceived array
                    for (int j = emailList.size() - 1; j >= 0; j--) {
                        emailList.remove(j);
                    }

                    // Write back the modified JSON to file
                    writeJSON(jsonObject);

                    break; // No need to continue after sending emails for the specified user
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendEmails(JsonArray emailArray) {
        try {
            // Convert JsonArray to string
            String jsonString = emailArray.toString();

            // Send the string representation of JsonArray
            outputStream.writeObject(jsonString);
            outputStream.flush();
            System.out.println("ok");
        } catch (SocketException e) {
            System.out.println("Client closed the connection");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
