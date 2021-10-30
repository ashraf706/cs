package com.cs.interview.ash.consumer;

import com.cs.interview.ash.dto.Event;
import com.cs.interview.ash.dto.Log;
import com.cs.interview.ash.producer.LogProducer;
import com.cs.interview.ash.producer.Producer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class LogConsumerTest {
    private static final HashMap<String, List<Log>> container = new HashMap<>();

    @Before
    public void setUp() throws Exception {
        updateContainerWithSampleLog();
    }

    @Test
    public void shouldBeAbleToProcessContainer() {
        final LogConsumer logConsumer = new LogConsumer();
        final ArrayList<Event> events = new ArrayList<>();

        logConsumer.consume(container,events);

        assertThat("events should contain 3 objects", events.size(), equalTo(3));
    }

    /**
     * Uses LogProducer to generate sample data
     * @throws IOException
     */
    private void updateContainerWithSampleLog() throws IOException {
        Path path = Paths.get(this.getClass().getClassLoader().getResource("small-log.txt").getPath());
        final Producer producer = new LogProducer();

        producer.produce(container, path);
    }
}