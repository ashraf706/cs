package com.cs.interview.ash.consumer;

import com.cs.interview.ash.dto.Event;
import com.cs.interview.ash.dto.Log;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface Consumer {

    void consume(ConcurrentHashMap<String, List<Log>> container, List<Event> events);
    void producerStatus(boolean complete);
}
