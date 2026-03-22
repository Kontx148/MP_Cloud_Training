package gyak.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyProvider {
    private static Properties properties = new Properties();
    private static Logger Log = LoggerFactory.getLogger(PropertyProvider.class);

    public PropertyProvider() {
        try (InputStream is = PropertyProvider.class.getResourceAsStream("/application.properties")){
            properties.load(is);
        } catch (IOException e) {
            Log.error("Could not load properties");
            throw new RuntimeException(e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

}
