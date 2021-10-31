package com.cs.interview.ash.db;

import com.cs.interview.ash.dto.Event;
import org.hsqldb.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class HSQLDbProvider implements Provider {
    private static final Logger logger = LoggerFactory.getLogger(HSQLDbProvider.class);

    public static final String HSQLDB_JDBC_DRIVER = "org.hsqldb.jdbc.JDBCDriver";
    public static final String HOST = "jdbc:hsqldb:hsql://localhost/eventdb";
    public static final String DB_NAME = "eventdb";
    public static final String DB_LOCATION = "file:database/eventdb";
    private Server server;
    Connection con;

    @Override
    public boolean consume(List<Event> events) throws SQLException, ClassNotFoundException {
        try {
            prepareDatabase();
            insertRecord(events);
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("Unexpected error occurred while preparing database.", e);
            throw e;
        }

        return true;
    }

    @Override
    public void producerStatus(boolean complete) {

    }

    private void startServer() {
        server = new Server();
        server.setLogWriter(null);
        server.setSilent(true);
        server.setDatabaseName(0, DB_NAME);
        server.setDatabasePath(0, DB_LOCATION);
        server.start();
    }

    private void insertRecord(List<Event> events) throws SQLException {
        final String sql = "Insert into event(eventid, duration, type, host, alert) values (?,?,?,?,?)";
        final PreparedStatement statement = con.prepareStatement(sql);

        events.forEach(e -> {
            try {
                statement.setString(1, e.getEventId());
                statement.setInt(2, e.getDuration());
                statement.setString(3, e.getType());
                statement.setString(4, e.getHost());
                statement.setBoolean(5, e.isAlert());

                statement.execute();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        stopServer();
    }

    private void stopServer() {
        server.stop();
    }

    private void prepareDatabase() throws SQLException, ClassNotFoundException {
        startServer();
        createConnection();
    }

    private void createConnection() throws ClassNotFoundException, SQLException {
        Class.forName(HSQLDB_JDBC_DRIVER);
        con = DriverManager.getConnection(HOST, "SA", "");
    }
}
