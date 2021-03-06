package com.cs.interview.ash.producer;

import com.cs.interview.ash.dto.Log;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class LogProducerTest {

    public static final String SMALL_LOG = "small-log.txt";
    public static final String MEDIUM_LOG = "medium-log.txt";
    private final static String SAMPLE_LOG_KEY = "scsmbstgra";
    private ConcurrentHashMap<String, List<Log>> container;
    private Object mutex;

    @Before
    public void setUp() {
        container = new ConcurrentHashMap<>();
        mutex = new Object();
    }

    @Test
    public void producerShouldAddLogInMap() throws IOException {
        Path path = Paths.get(this.getClass().getClassLoader().getResource(SMALL_LOG).getPath());
        final Producer producer = new LogProducer(mutex);

        producer.produce(container, path);

        assertThat("container contains data", container.size(), equalTo(3));
        assertThat("scsmbstgra key exists", container.get(SAMPLE_LOG_KEY), notNullValue());
        assertThat("scsmbstgra key contains value of size 2", container.get(SAMPLE_LOG_KEY).size(), equalTo(2));
    }

    @Test
    public void producerShouldAddLogInMapForMediumSizeFile() throws IOException {
        Path path = Paths.get(this.getClass().getClassLoader().getResource(MEDIUM_LOG).getPath());
        final Producer producer = new LogProducer(mutex);

        producer.produce(container, path);

        assertThat("container contains data", container.size(), equalTo(13));
    }
}
