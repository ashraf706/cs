package com.cs.interview.ash.dto;

public class Event {
    private final String eventId;
    private final int duration;
    private final String type;
    private final String host;
    private final boolean alert;

    public Event(String eventId, int duration, String type, String host, boolean alert) {
        this.eventId = eventId;
        this.duration = duration;
        this.type = type;
        this.host = host;
        this.alert = alert;
    }
}
