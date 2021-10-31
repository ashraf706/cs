package com.cs.interview.ash.db;

import com.cs.interview.ash.dto.Event;

import java.sql.SQLException;
import java.util.List;

public interface Provider {
    boolean consume(List<Event> events) throws SQLException, ClassNotFoundException;
    void producerStatus(boolean complete);
}
