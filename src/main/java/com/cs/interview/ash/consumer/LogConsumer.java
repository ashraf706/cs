package com.cs.interview.ash.consumer;

import com.cs.interview.ash.dto.Event;
import com.cs.interview.ash.dto.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LogConsumer implements Consumer {

    private static final Logger logger = LoggerFactory.getLogger(LogConsumer.class);
    private boolean producerIsProducing = true;
    private final Object mutex;

    public LogConsumer(Object mutex) {
        this.mutex = mutex;
    }

    @Override
    public void consume(ConcurrentHashMap<String, List<Log>> container, List<Event> events) {
        logger.info("Consumer started");
        while (producerIsProducing) {
            waitIfContainerIsEmpty(container.isEmpty());

            createAndAddEvent(container, events);

        }

        logger.info("<<<<<<<<<<<<<< Consumer Completed >>>>>>>>>>>>>>>>>");
    }

    @Override
    public void producerStatus(boolean complete) {
        producerIsProducing = !complete;
    }

    /**
     * Create Event object for each entry in the container and add the Event object
     * in events list. Also remove the container entry from the hash map.
     */
    private void createAndAddEvent(ConcurrentHashMap<String, List<Log>> container, List<Event> events) {
        int counter = 0;
        for (Iterator<Map.Entry<String, List<Log>>> iter = container.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry<String, List<Log>> entry = iter.next();
            events.add(eventFromLogs(entry.getValue()));
            counter++;
            iter.remove();
        }
        logger.debug("{} Event object created", counter);
    }

    /**
     * Conditionally call wait() method if the boolean flag is true
     *
     * @param isEmpty true if the container is empty
     */
    private void waitIfContainerIsEmpty(boolean isEmpty) {
        if (isEmpty) {
            try {
                synchronized (mutex) {
                    logger.debug("Consumer is Waiting");
                    mutex.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Event eventFromLogs(List<Log> logs) {
        final Log log1 = logs.get(0);
        final Log log2 = logs.get(1);

        final long duration = log1.getState().equals("STARTED") ?
                log2.getTimeStamp() - log1.getTimeStamp() :
                log1.getTimeStamp() - log2.getTimeStamp();

        return new Event(log1.getId(), (int) duration, log1.getType(), log1.getHost(), duration > 4);
    }
}
