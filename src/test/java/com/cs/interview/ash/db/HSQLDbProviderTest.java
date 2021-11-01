package com.cs.interview.ash.db;

import com.cs.interview.ash.dto.Event;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class HSQLDbProviderTest {
    @Test
    public void dbInsertionTest() throws SQLException, ClassNotFoundException {
        final List<Event> events = Arrays.asList(
                new Event("scsmbstgraaa", 4, "server", "host1", true),
                new Event("scsmbstgrbbbb", 4, null, null, true)
        );

        final Provider provider = new HSQLDbProvider();
        final boolean status = provider.consume(events);

        assertThat("Db operation executed successfully", status, is(true));
    }
}