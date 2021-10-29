package com.cs.interview.ash.consumer;

import com.cs.interview.ash.dto.Event;
import com.cs.interview.ash.dto.Log;

import java.util.HashMap;
import java.util.List;

public interface Consumer {

    void consume(HashMap<String, List<Log>> container, List<Event> events);
}
