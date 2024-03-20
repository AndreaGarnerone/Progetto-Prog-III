package com.unito.Server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.unito.ClientMain.Email;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ServerModel {
    private final ObservableList<String> eventLog = FXCollections.observableArrayList();
    private static final String filePath = "MailBank.json";
    ServerSocket serverSocket;
    Socket socket = null;
    ObjectInputStream inputStream = null;
    ObjectOutputStream outputStream = null;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock rl = lock.readLock();
    private final Lock wl = lock.writeLock();

    public ServerModel() {
    }

    /* Methods for receiving an email from a client */
    public void listen() {
        try {
            int port = 27;
            serverSocket = new ServerSocket(port);
            while (!serverSocket.isClosed()) { // Check if the socket is closed
                receiveEmail();
            }
        } catch (IOException e) {
            if (!serverSocket.isClosed()) { // Check if the socket is not closed
                e.printStackTrace();
            }
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

            // Handle client connection using ExecutorService
            executor.submit(() -> {
                try {
                    inputStream = new ObjectInputStream(socket.getInputStream());
                    outputStream = new ObjectOutputStream(socket.getOutputStream());
                    outputStream.flush();

                    try {
                        Object receivedObject = inputStream.readObject();
                        if (receivedObject instanceof String receivedString) {
                            if (receivedString.equals("new")) {
                                addEventLog("New client connected");
                            } else if (receivedString.equals("close")) {
                                addEventLog("Client closed");
                            } else {
                                receivedString = (String) receivedObject;
                                refresh(receivedString);
                            }
                        } else if (receivedObject instanceof Email receivedEmail) {
                            addEventLog("Email sent");
                            storeEmail(receivedEmail);
                        } else System.out.println("NO");
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("oh oh");
                        e.printStackTrace();
                    } finally {
                        closeConnection();
                    }
                } catch (IOException e) {
                    addEventLog("Failed to create a socket connection between Client and Server");
                    e.printStackTrace();
                }
            });
        } catch (SocketException e) {
            if (!serverSocket.isClosed()) {
                e.printStackTrace();
            }
        } catch (IOException e) {
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

    public void storeEmail(Email email) {
        wl.lock();
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
        } finally {
            wl.unlock();
        }

    }

    private JsonObject readJSON() throws IOException {
        rl.lock();
        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, JsonObject.class);
        } finally {
            rl.unlock();
        }
    }

    private void writeJSON(JsonObject jsonObject) throws IOException {
        wl.lock();
        try (FileWriter writer = new FileWriter(filePath)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(jsonObject, writer);
        } finally {
            wl.unlock();
        }
    }


    //---------------------------- Send the email on refresh ----------------------------//
    // Send the email on refresh
    public void refresh(String selectedAccount) {
        try {
            // Read existing JSON file
            JsonObject jsonObject = readJSON();

            // Retrieve the "UserList" array
            JsonArray userList = jsonObject.getAsJsonArray("UserList");

            for (int i = 0; i < userList.size(); i++) {
                JsonObject user = userList.get(i).getAsJsonObject();
                String userName = user.get("name").getAsString();
                if (userName.equals(selectedAccount)) {
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
        lock.readLock().lock();
        try {
            // Convert JsonArray to string
            String jsonString = emailArray.toString();

            // Send the string representation of JsonArray
            outputStream.writeObject(jsonString);
            outputStream.flush();
        } catch (SocketException e) {
            System.out.println("Client closed the connection");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }
    }

    //--------- Event Log ---------//
    public ObservableList<String> getEventLog() {
        return eventLog;
    }

    private void addEventLog(String event) {
        Platform.runLater(() -> eventLog.add(0, event));
    }

    public void stopServer() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (SocketException ignored) {
            // Socket is already closed, no need to handle this exception
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
