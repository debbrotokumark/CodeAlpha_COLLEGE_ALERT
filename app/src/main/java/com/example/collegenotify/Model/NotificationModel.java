package com.example.collegenotify.Model;

public class NotificationModel {
    private int id;
    private String title;
    private String body;
    private String link;
    private String timestamp;

    public NotificationModel(int id, String title, String body, String link, String timestamp) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.link = link;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getLink() {
        return link;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
