package com.unito.prog3.Client;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

public class Email {
    private final StringProperty from = new SimpleStringProperty();
    private final StringProperty to = new SimpleStringProperty();
    private final StringProperty subject = new SimpleStringProperty();
    private final StringProperty content = new SimpleStringProperty();
    private final StringProperty timestamp = new SimpleStringProperty();

    public String getFromAll() {
        return from.get();
    }

    public String getFromFirst() {
        return from.get().split(";")[0];
    }

    public StringProperty fromProperty() {
        return from;
    }

    public void setFrom(String from) {
        this.from.set(from);
    }

    public String getToAll() {
        return to.get();
    }

    public String getToFirst() {
        return to.get().split(";")[0];
    }

    public StringProperty toProperty() {
        return to;
    }

    public void setTo(String to) {
        this.to.set(to);
    }

    public String getSubject() {
        return subject.get();
    }

    public StringProperty subjectProperty() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject.set(subject);
    }

    public String getContent() {
        return content.get();
    }

    public StringProperty contentProperty() {
        return content;
    }

    public void setContent(String content) {
        this.content.set(content);
    }

    public String getTimestamp() {
        return timestamp.get();
    }

    public StringProperty timestampProperty() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp.set(timestamp);
    }


    public Email(String from, String to, String subject, String content, String time) {
        this.setFrom(from);
        this.setContent(content);
        this.setSubject(subject);
        this.setTo(to);
        this.setTimestamp(time);
    }

    @Override
    public String toString() {
        return "from=" + getFromAll() +
                ", to=" + getToAll() +
                ", subject=" + getSubject() +
                ", content=" + getContent() +
                ", date=" + getTimestamp();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return email.getFromAll().equals(this.getFromAll()) &&
                email.getToAll().equals(this.getToAll()) &&
                email.getSubject().equals(this.getSubject()) &&
                email.getContent().equals(this.getContent()) &&
                email.getTimestamp().equals(this.getTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, subject, content, timestamp);
    }

    public String toJson() {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");

        appendProperty(jsonBuilder, "from", from.get());
        appendProperty(jsonBuilder, "to", to.get());
        appendProperty(jsonBuilder, "subject", subject.get());
        appendProperty(jsonBuilder, "content", content.get());
        appendProperty(jsonBuilder, "timestamp", timestamp.get());

        // Remove the trailing comma if it exists
        if (jsonBuilder.length() > 1) {
            jsonBuilder.setLength(jsonBuilder.length() - 1);
        }

        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }

    // Helper method to append key-value pairs to the StringBuilder
    private void appendProperty(StringBuilder builder, String key, String value) {
        if (value != null) {
            builder.append("\"").append(key).append("\":\"").append(value).append("\",");
        }
    }

    public static Email fromString(String json) {
        try {
            // Split the JSON string into individual fields
            String[] parts = json.substring(1, json.length() - 1).split(",");

            // Extract values for each field
            String from = null, to = null, subject = null, content = null, timestamp = null;
            for (String part : parts) {
                String[] keyValue = part.split(":");
                String key = keyValue[0].trim().replaceAll("\"", "");
                String value = keyValue[1].trim().replaceAll("\"", "");
                switch (key) {
                    case "from":
                        from = value;
                        break;
                    case "to":
                        to = value;
                        break;
                    case "subject":
                        subject = value;
                        break;
                    case "content":
                        content = value;
                        break;
                    case "timestamp":
                        timestamp = value;
                        break;
                    default:
                        // Handle unknown fields if necessary
                        break;
                }
            }

            // Create and return an Email instance
            return new Email(from, to, subject, content, timestamp);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}