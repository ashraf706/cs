package com.cs.interview.ash.consumer;

import com.cs.interview.ash.dto.Event;
import com.cs.interview.ash.dto.Log;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class LogConsumer implements Consumer{


    @Override
    public void consume(HashMap<String, List<Log>> container, List<Event> events) {
        final List<Event> eventsFromLogs = container.values().stream()
                .map(this::eventFromLogs)
                .collect(Collectors.toList());

        events.addAll(eventsFromLogs);
    }

    private Event eventFromLogs(List<Log> logs){
        final Log log1 = logs.get(0);
        final Log log2 = logs.get(1);

        final long duration =  log1.getState().equals("STARTED") ?
                log2.getTimeStamp() - log1.getTimeStamp():
                log1.getTimeStamp() - log2.getTimeStamp();

        return new Event(log1.getId(),(int) duration, log1.getType(), log1.getHost(), duration > 4);
    }
}
