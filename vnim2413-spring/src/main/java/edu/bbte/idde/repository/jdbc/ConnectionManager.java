package edu.bbte.idde.repository.jdbc;

import edu.bbte.idde.repository.RepositoryException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@Repository
@Slf4j
@Lazy
public class ConnectionManager {
    private final List<Connection> connectionList = new LinkedList<>();

    @Value("${jdbc.url}")
    private String jdbcUrl;

    @Value("${jdbc.driver}")
    private String jdbcDriver;

    @Value("${jdbc.username}")
    private String jdbcUser;

    @Value("${jdbc.password}")
    private String jdbcPassword;

    @Value("${jdbc.poolSize}")
    private int poolSize;

    @PostConstruct
    public void initConnections() {
        try {
            Class.forName(jdbcDriver);
            log.info("Connection succeeded");
        } catch (ClassNotFoundException e) {
            log.error("Connection failed", e);
            throw new RepositoryException("Connection failed", e);
        }
        for (int i = 0; i < poolSize; i++) {
            try {
                connectionList.add(DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword));
            } catch (SQLException e) {
                log.error("Connection failed", e);
                throw new RepositoryException("Connection failed", e);
            }
        }
        log.info("Connection pool initialized with {} connections", poolSize);
    }

    public Connection getConnection() {
        if (connectionList.isEmpty()) {
            log.warn("No available connections in the pool");
            return null;
        } else {
            return connectionList.removeFirst();
        }
    }

    public void returnConnection(Connection connection) {
        connectionList.add(connection);
    }
}
