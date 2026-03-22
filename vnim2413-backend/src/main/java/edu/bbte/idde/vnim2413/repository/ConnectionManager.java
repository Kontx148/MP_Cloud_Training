package edu.bbte.idde.vnim2413.repository;

import edu.bbte.idde.vnim2413.utils.AppConfig;
import edu.bbte.idde.vnim2413.utils.ConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public final class ConnectionManager {
    private final List<Connection> connectionList = new LinkedList<>();

    private static ConnectionManager manager;
    private static final Logger LOG = LoggerFactory.getLogger(ConnectionManager.class);


    private void initConnections() {
        AppConfig config;
        try {
            config = ConfigLoader.getConfig();
            Class.forName(config.getJdbcDriver());
            LOG.info("Connection succeeded");
        } catch (ClassNotFoundException | IOException e) {
            LOG.error("Connection failed", e);
            throw new RepositoryException("Connection failed", e);
        }
        int poolSize = config.getConnectionPoolSize();

        for (int i = 0; i < poolSize; i++) {
            try {
                String jdbcUrl = config.getJdbcUrl();
                String jdbcUser = config.getUsername();
                String jdbcPassword = config.getPassword();
                connectionList.add(DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword));
            } catch (SQLException e) {
                LOG.error("Connection failed", e);
                throw new RepositoryException("Connection failed", e);
            }
        }
        LOG.info("Connection pool initialized with {} connections", poolSize);
    }

    public static synchronized ConnectionManager createInstance() throws RepositoryException {
        if (manager == null) {
            ConnectionManager tmp = new ConnectionManager();
            tmp.initConnections();
            manager = tmp;
        }
        return manager;
    }

    public Connection getConnection() {
        if (connectionList.isEmpty()) {
            LOG.warn("No available connections in the pool");
            return null;
        } else {
            return connectionList.removeFirst();
        }
    }

    public void returnConnection(Connection connection) {
        connectionList.add(connection);
    }
}
