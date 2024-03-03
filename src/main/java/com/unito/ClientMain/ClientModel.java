package com.unito.ClientMain;

import com.unito.WriteEmail.WriteEmailController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ClientModel {
    private ObservableList<Email> mailList;

    private String hostName = "127.0.0.1";
    private int port = 27;
    Socket socket = null;
    ObjectOutputStream outputStream = null;
    InputStream inputStream = null;

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

        try {
            // Read existing JSON array from file
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(filePath));

            // Iterate over the JSON array to find the email to remove
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonEmail = (JSONObject) jsonArray.get(i);
                if (email.getFromAll().equals(jsonEmail.get("from"))
                        && email.getToAll().equals(jsonEmail.get("to"))
                        && email.getSubject().equals(jsonEmail.get("subject"))
                        && email.getContent().equals(jsonEmail.get("content"))
                        && email.getTimestamp().equals(jsonEmail.get("timestamp"))) {
                    jsonArray.remove(i); // Remove the email from the JSONArray
                    break; // No need to continue iterating once the email is found and removed
                }
            }

            // Write updated JSONArray back to the file
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(jsonArray.toJSONString());
            fileWriter.flush();
            fileWriter.close();

            //System.out.println("Email removed successfully.");
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
                mailList.add(0, new Email(from, to, subject, content, timestamp));
            }

            System.out.println("Emails loaded successfully.");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

}

