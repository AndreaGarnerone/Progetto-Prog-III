package Client.ClientMain;

import Client.WriteEmail.WriteEmailController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class ClientModel {
    private ObservableList<Email> mailList;

    WriteEmailController writeEmailController = new WriteEmailController();

    public ClientModel(String account) {
        mailList = FXCollections.observableArrayList();
        loadEmailsFromFile(account);
    }

    public ObservableList<Email> getMailList() {
        return mailList;
    }


    //Add an email to the list
    public void addEmail(Email email) {
        String filePath = "MailStorage/" + email.getToFirst() + ".json";

        try {
            // Read existing JSON array from file or create a new one if file doesn't exist
            File file = new File(filePath);
            JSONArray jsonArray = null;
            if (!file.exists()) {
                writeEmailController.setErrorText();
            } else {
                jsonArray = new JSONArray();
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(new FileReader(filePath));
                jsonArray = (JSONArray) obj;


                // Convert Email object to JSONObject
                JSONObject emailJson = new JSONObject();
                emailJson.put("from", email.getFromAll());
                emailJson.put("to", email.getToAll());
                emailJson.put("subject", email.getSubject());
                emailJson.put("content", email.getContent());
                emailJson.put("timestamp", email.getTimestamp());

                jsonArray.add(emailJson);

                FileWriter fileWriter = new FileWriter(filePath);
                fileWriter.write(jsonArray.toJSONString());
                fileWriter.flush();
                fileWriter.close();
            }

            //System.out.println("Email saved successfully.");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
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

