package com.cs.interview.ash.parser;

import com.cs.interview.ash.consumer.Consumer;
import com.cs.interview.ash.consumer.LogConsumer;
import com.cs.interview.ash.dto.Event;
import com.cs.interview.ash.dto.Log;
import com.cs.interview.ash.producer.LogProducer;
import com.cs.interview.ash.producer.Producer;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class LogParserTest {

    @Test
    public void parserShouldCreateEvent() throws InterruptedException {
        final Object mutex = new Object();
        final Producer producer = new LogProducer(mutex);
        final Consumer consumer = new LogConsumer(mutex);
        final Path path = Paths.get(this.getClass().getClassLoader().getResource("medium-log.txt").getPath());
        final ConcurrentHashMap<String, List<Log>> container = new ConcurrentHashMap<>();
        final ArrayList<Event> events = new ArrayList<>();

        producer.register(consumer);
        final LogParser parser = new LogParser(producer, consumer);

        parser.parse(container,events, path);
        Thread.sleep(1000);  //todo: This sleep is a quick fix. Has to implement properly
        assertThat("events should contain 13 objects", events.size(), equalTo(13));
    }
}
