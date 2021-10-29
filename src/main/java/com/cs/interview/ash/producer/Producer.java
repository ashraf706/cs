package com.cs.interview.ash.producer;

import com.cs.interview.ash.dto.Log;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

public interface Producer {
    void produce(HashMap<String, List<Log>> container, Path path) throws IOException;
}
