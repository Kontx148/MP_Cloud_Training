package edu.bbte.idde.vnim2413.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.bbte.idde.vnim2413.repository.ConnectionManager;
import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class ConfigLoader {

    private static AppConfig config;

    private static final Logger LOG = LoggerFactory.getLogger(ConnectionManager.class);

    public static synchronized AppConfig getConfig() throws IOException {
        if (config == null) {
            // Get the profile from .env file
            // Fallback to memory if the .env file is missing
            // .env file structure :
            // APP_PROFILE=jdbc/memory
            Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
            String profile = dotenv.get("APP_PROFILE");

            if (profile == null) {
                profile = "memory";
            }

            LOG.info("Currently running profile : " + profile);

            String resource = "/config/" + profile + ".json";
            InputStream is = ConfigLoader.class.getResourceAsStream(resource);
            if (is == null) {
                throw new IOException("Config file not found: " + resource);
            }

            ObjectMapper mapper = new ObjectMapper();
            config = mapper.readValue(is, AppConfig.class);
        }
        return config;
    }
}