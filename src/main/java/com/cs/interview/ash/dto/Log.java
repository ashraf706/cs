package com.cs.interview.ash.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Log {
    private final String id;
    private final String state;
    private final String type;
    private final String host;
    private final long timeStamp;

    @JsonCreator
    public Log(
            @JsonProperty("id") String id,
            @JsonProperty("state") String state,
            @JsonProperty("type") String type,
            @JsonProperty("host") String host,
            @JsonProperty("timestamp") long timeStamp) {
        this.id = id;
        this.state = state;
        this.type = type;
        this.host = host;
        this.timeStamp = timeStamp;
    }

    public String getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public String getType() {
        return type;
    }

    public String getHost() {
        return host;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String toString() {
        return "Log{" +
                "id='" + id + '\'' +
                ", state='" + state + '\'' +
                ", type='" + type + '\'' +
                ", host='" + host + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
