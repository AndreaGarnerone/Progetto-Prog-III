package com.unito.ClientMain;

import java.io.Serializable;
import java.util.Objects;

public class Email implements Serializable {
    private String from;
    private String to;
    private String subject;
    private String content;
    private String timestamp;

    public String getFromAll() {
        return from;
    }

    public String getFromFirst() {
        if (from != null && from.contains(";")) {
            return from.split(";")[0];
        }
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getToAll() {
        return to;
    }

    public String getToFirst() {
        if (to != null && to.contains(";")) {
            return to.split(";")[0];
        }
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Email(String from, String to, String subject, String content, String timestamp) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.content = content;
        this.timestamp = timestamp;
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
        return "{\"from\":\"" + from + "\",\"to\":\"" + to + "\",\"subject\":\"" + subject +
                "\",\"content\":\"" + content + "\",\"timestamp\":\"" + timestamp + "\"}";
    }

    public static Email fromString(String json) {
        try {
            String[] parts = json.substring(1, json.length() - 1).split(",");
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
            return new Email(from, to, subject, content, timestamp);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
