package gyak.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;

public class PropertiesManager {
    private static final String CONFIG_FILE = "/jdbc/application.json";

    private static MainConfiguration mainConfiguration = new MainConfiguration();

    public PropertiesManager() {
        ObjectMapper objectMapper = new ObjectMapper();

        try (InputStream is = PropertiesManager.class.getResourceAsStream(CONFIG_FILE)) {
            mainConfiguration = objectMapper.readValue(is, MainConfiguration.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void toConfigFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File("asd.json");
        try {
            objectMapper.writeValue(file, mainConfiguration);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JdbcConfig getJdbc() {
        return mainConfiguration.getJdbcConfig();
    }

    public PoolConfig getPool() {
        return mainConfiguration.getPoolConfig();
    }
}

