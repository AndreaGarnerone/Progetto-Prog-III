package com.unito.Server;

import com.unito.ClientMain.Email;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.ServerSocket;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerModel {

    private int port = 27;
    ServerSocket serverSocket;
    ObjectInputStream inputStream = null;
    ObjectOutputStream outputStream = null;


    public ServerModel() {
        listen();
    }

    private void listen() {
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                receiveEmail();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void receiveEmail() {
        try {
            Socket socket = serverSocket.accept();

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
    private void storeEmail(Email email) {
        try {
            String filePath = "MailBank.json";

            // Read existing JSON array from file or create a new one if file doesn't exist
            JSONParser parser = new JSONParser();
            JSONArray userList = new JSONArray();

            File file = new File(filePath);
            if (file.exists()) {
                Object obj = parser.parse(new FileReader(filePath));
                userList = (JSONArray) ((JSONObject) obj).get("UserList");
            }

            // Find the user in the list based on the email address
            for (Object userObj : userList) {
                JSONObject user = (JSONObject) userObj;
                String userEmail = (String) user.get("name");
                if (userEmail.equals(email.getToAll())) { // Assuming email.getToAll() returns the email address
                    // Add the email to the user's email list
                    JSONArray emailList = (JSONArray) user.get("emailList");
                    JSONObject jsonEmail = new JSONObject();
                    jsonEmail.put("from", email.getFromAll());
                    jsonEmail.put("to", email.getToAll());
                    jsonEmail.put("subject", email.getSubject());
                    jsonEmail.put("content", email.getContent());
                    jsonEmail.put("timestamp", email.getTimestamp());
                    emailList.add(jsonEmail);
                    break; // No need to continue searching
                }
            }

            // Write the updated JSON back to the file
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("UserList", userList);

            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(jsonObject.toJSONString());
            fileWriter.flush();
            fileWriter.close();

            //System.out.println("Email saved successfully.");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

}
