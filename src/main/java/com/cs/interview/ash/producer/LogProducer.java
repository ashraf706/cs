package com.cs.interview.ash.producer;

import com.cs.interview.ash.dto.Log;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(LogProducer.class);
    private final HashMap<String, List<Log>> localContainer = new HashMap<>();
    private final Object mutex;
    private static final int CONTAINER_BUFFER_SIZE = 5;

    public LogProducer(Object mutex) {
        this.mutex = mutex;
    }

    @Override
    public void produce(HashMap<String, List<Log>> container, Path path) throws IOException {
        logger.debug("Producer started");

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            for (String line; (line = reader.readLine()) != null; ) {
                Log log = mapper.readValue(line, Log.class);
                addToContainer(log, container);
                notifyConditionally(container.size());
            }
        }

        logger.debug("Container size " + container.size());
        logStatus();
        notifyUnconditionally(container.size());
    }

    /**
     * execute notifyAll if container is not empty and reach buffer size limit.
     */
    private void notifyConditionally(int containerSize) {
        if (containerSize > 0 && containerSize % CONTAINER_BUFFER_SIZE == 0) {
            notifyUnconditionally(containerSize);
        }
    }

    private void notifyUnconditionally(int containerSize) {
        synchronized (mutex) {
            logger.debug("Producer Notify. Container size is: {}", containerSize);
            mutex.notifyAll();
        }
    }

    /**
     * This function add a Log object initially in  localContainer hashmap if it is not present.
     * The key is the id and value is a list of Log objects. If the key is already in localContainer
     * then it is removed from the localContainer and added to the global Container hashmap.
     * In this way this function make sure the global Container always has list value with 2 Log object
     * for the same key.
     */
    private void addToContainer(Log log, HashMap<String, List<Log>> container) {
        final String id = log.getId();
        if (localContainer.containsKey(id)) {
            final List<Log> logs = localContainer.remove(id);
            logs.add(log);
            container.put(id, logs);
        } else {
            final ArrayList<Log> logs = new ArrayList<>();
            logs.add(log);
            localContainer.put(id, logs);
        }
    }

    /**
     * Log producer status after execution.
     * There is a possibility that the log file may contain an event
     * which does not have both start and finish state. This function
     * will help to identify the event.
     */
    private void logStatus() {
        if (localContainer.size() > 0) {
            logger.info("Producer could not process the following event(s):");
            localContainer.values()
                    .forEach(System.out::println);
        } else {
            logger.info("Log file process completed successfully.");
        }
    }
}
