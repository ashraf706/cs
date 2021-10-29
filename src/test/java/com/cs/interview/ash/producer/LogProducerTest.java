package com.cs.interview.ash.producer;

import com.cs.interview.ash.dto.Log;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class LogProducerTest {
    @Test
    public void producerShouldAddLogInMap() throws IOException {
        Path path = Paths.get(this.getClass().getClassLoader().getResource("small-log.txt").getPath());
        final HashMap<String, List<Log>> container = new HashMap<>();
        final Producer producer = new LogProducer();
        final String sampleLogKey = "scsmbstgra";

        producer.produce(container, path);

        assertThat("container contains data", container.size(), equalTo(3));
        assertThat("scsmbstgra key exists", container.get(sampleLogKey), notNullValue());
        assertThat("scsmbstgra key contains value of size 2", container.get(sampleLogKey).size(), equalTo(2));
    }
}