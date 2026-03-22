package gyak.repository.jdbc;

import gyak.config.PropertiesManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPool {
    private final List<Connection> connectionList = new ArrayList<>();

    private static ConnectionPool instance;

    private static final PropertiesManager propertiesManager = new PropertiesManager();

    private ConnectionPool() {
        try {
            String url = propertiesManager.getJdbc().getUrl();
            String user = propertiesManager.getJdbc().getUser();
            String pass = propertiesManager.getJdbc().getPassword();

            Class.forName(propertiesManager.getJdbc().getDriverClass());
            for(int i=0;i<propertiesManager.getPool().getPoolSize();i++) {
                Connection connectionToAdd = DriverManager.getConnection(url, user, pass);
                connectionList.add(connectionToAdd);
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Connection could not be established: " + e.getMessage());
        }
    }

    public static synchronized ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    public Connection getConnection() {
        if(!connectionList.isEmpty()) {
            return connectionList.removeFirst();
        }

        return null;
    }

    public void returnConnection(Connection c) {
        connectionList.add(c);
    }
}
