package com.cs.interview.ash;

import com.cs.interview.ash.consumer.Consumer;
import com.cs.interview.ash.consumer.LogConsumer;
import com.cs.interview.ash.db.HSQLDbProvider;
import com.cs.interview.ash.db.Provider;
import com.cs.interview.ash.dto.Event;
import com.cs.interview.ash.dto.Log;
import com.cs.interview.ash.parser.LogParser;
import com.cs.interview.ash.producer.LogProducer;
import com.cs.interview.ash.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Hello world!
 */
public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static String filePath;

    public static void main( String[] args )
    {
        if(!validInput(args)){
            return;
        }

        final Object mutex = new Object();
        final Producer producer = new LogProducer(mutex);
        final Consumer consumer = new LogConsumer(mutex);
        final Path path = Paths.get(filePath);
        final ConcurrentHashMap<String, List<Log>> container = new ConcurrentHashMap<>();
        final ArrayList<Event> events = new ArrayList<>();

        producer.register(consumer);
        final LogParser parser = new LogParser(producer, consumer);

        parser.parse(container, events, path);

        addEventToDatabase(events);

        logger.info("Application exists successfully.");
    }

    private static void addEventToDatabase(ArrayList<Event> events) {
        Provider provider = new HSQLDbProvider();
        try {
            provider.consume(events);
            logger.info("Event records added to the database.");
        } catch (SQLException e) {
            logger.error("Unexpected SQL exception occurred: ", e);
        } catch (ClassNotFoundException e) {
            logger.error("Unexpected exception occurred", e);
        }
    }

    /**
     * Simple file path input sanitiser.
     * Returns true for a valid file path
     */
    private static boolean validInput(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide a valid log file path.");
            logger.error("File path parameter not provided by the user.");
            return false;
        } else {
            filePath = args[0];
            final String msg = "File path: " + filePath;
            System.out.println(msg);
            logger.info(msg);

            File file = new File(filePath);
            if (file.exists() && !file.isDirectory()) {
                logger.info("File path is valid. Continue working..");
                return true;
            } else {
                System.out.println("Provided file path is not a valid file path. Please provide a valid log file path.");
                logger.error("User provided invalid file path: {}", args[0]);
                return false;
            }
        }
    }
}
