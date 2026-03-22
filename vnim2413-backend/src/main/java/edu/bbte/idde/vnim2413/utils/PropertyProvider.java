package edu.bbte.idde.vnim2413.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyProvider {
    private static final Properties properties = new Properties();
    private static final Logger LOG = LoggerFactory.getLogger(PropertyProvider.class);

    static {
        try (InputStream is = PropertyProvider.class.getResourceAsStream("/jdbc.properties")) {
            properties.load(is);
        } catch (IOException e) {
            LOG.error("Couldn't load properties", e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }
}
