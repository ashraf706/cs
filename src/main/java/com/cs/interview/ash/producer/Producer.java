package com.cs.interview.ash.producer;

import com.cs.interview.ash.consumer.Consumer;
import com.cs.interview.ash.dto.Log;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface Producer {
    void produce(ConcurrentHashMap<String, List<Log>> container, Path path) throws IOException;
    boolean register(Consumer consumer);
}
