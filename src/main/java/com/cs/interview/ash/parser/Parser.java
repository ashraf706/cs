package com.cs.interview.ash.parser;

import com.cs.interview.ash.dto.Event;
import com.cs.interview.ash.dto.Log;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface Parser {
    void parse(ConcurrentHashMap<String, List<Log>> container, List<Event> events, Path path);
}
