package com.example.collegenotify.Model;

public class EventModel {
    private int id;
    private String name;
    private String description;
    private String eventTime;

    public EventModel(int id, String name, String description, String eventTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.eventTime = eventTime;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getEventTime() {
        return eventTime;
    }
}
