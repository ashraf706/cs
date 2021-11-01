package com.cs.interview.ash.db;

import com.cs.interview.ash.dto.Event;
import org.hsqldb.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
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
            stopServer();
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("Unexpected error occurred while preparing database.", e);
            throw e;
        }

        return true;
    }

    @Override
    public void producerStatus(boolean complete) {
        //todo: need to implement this method if Provider is treated as a consumer.
        throw new UnsupportedOperationException("Not yet implemented");
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
                con.commit();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void stopServer() throws SQLException {
        con.prepareStatement("SHUTDOWN").execute();
        server.stop();
    }

    private void prepareDatabase() throws SQLException, ClassNotFoundException {
        startServer();
        createConnection();
        createTable();

    }

    /**
     * Crete Event table if not present
     */
    private void createTable() throws SQLException {
        final ResultSet rs = con.getMetaData().getTables(null, null, "EVENT", null);
        if (!rs.next()) {
            logger.info("Database table 'Event' not found. Creating table");
            con.createStatement().executeUpdate(
                    "create table event(eventid varchar(100),duration int,type varchar(20),host varchar(60),alert boolean);"
            );
        }
    }

    private void createConnection() throws ClassNotFoundException, SQLException {
        Class.forName(HSQLDB_JDBC_DRIVER);
        con = DriverManager.getConnection(HOST, "SA", "");
    }
}
