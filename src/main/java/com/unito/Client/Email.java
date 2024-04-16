package com.unito.Client;

import java.io.Serializable;
import java.util.Objects;

public class Email implements Serializable {
    private String from;
    private String to;
    private String subject;
    private final String content;
    private final String timestamp;

    /**
     * Getter method
     * @return the from field
     */
    public String getFrom() {
        return from;
    }

    /**
     * Setter method
     * @param from the from field
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Getter method
     */
    public String getToAll() {
        return to;
    }

    /**
     * Getter method
     */
    public String getToFirst() {
        if (to != null && to.contains(";")) {
            return to.split(";")[0];
        }
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Getter method
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Setter method
     * @param subject The subject field
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Getter method
     */
    public String getContent() {
        return content;
    }

    /**
     * Getter method
     */
    public String getTimestamp() {
        return timestamp;
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
        return "from=" + getFrom() +
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
        return email.getFrom().equals(this.getFrom()) &&
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
