package com.cs.interview.ash.producer;

import com.cs.interview.ash.dto.Log;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LogProducer implements Producer {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void produce(HashMap<String, List<Log>> container, Path path) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            for (String line; (line = reader.readLine()) != null; ) {

                Log log = mapper.readValue(line, Log.class);
                addToContainer(log, container);
            }
        }
    }

    private void addToContainer(Log log, HashMap<String, List<Log>> container) {
        final String id = log.getId();
        if (container.containsKey(id)) {
            final List<Log> logs = container.get(id);
            logs.add(log);
            container.put(id, logs);
        } else {
            final ArrayList<Log> logs = new ArrayList<>();
            logs.add(log);
            container.put(id, logs);
        }
    }
}
