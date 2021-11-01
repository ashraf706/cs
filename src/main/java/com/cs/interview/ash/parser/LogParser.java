package com.cs.interview.ash.parser;

import com.cs.interview.ash.consumer.Consumer;
import com.cs.interview.ash.dto.Event;
import com.cs.interview.ash.dto.Log;
import com.cs.interview.ash.producer.Producer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class create couple of threads to run producer and consumer in a
 * separate thread.
 */
public class LogParser implements Parser {
    private final Producer producer;
    private final Consumer consumer;

    public LogParser(Producer producer, Consumer consumer) {
        this.producer = producer;
        this.consumer = consumer;
    }

    @Override
    public void parse(ConcurrentHashMap<String, List<Log>> container, List<Event> events, Path path) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.execute(producerRunnable(producer, container, path));
        executor.execute(consumerRunnable(consumer, container, events));

        executor.shutdown();
    }

    private Runnable producerRunnable(Producer producer, ConcurrentHashMap<String, List<Log>> container, Path path) {
        return () -> {
            try {
                producer.produce(container, path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    private Runnable consumerRunnable(Consumer consumer,ConcurrentHashMap<String, List<Log>> container,
                                            List<Event> events){
        return () -> consumer.consume(container, events);
    }
}
