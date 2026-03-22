package edu.bbte.idde.vnim2413.repository;

import edu.bbte.idde.vnim2413.repository.jdbc.JdbcRepositoryFactory;
import edu.bbte.idde.vnim2413.repository.memory.MemoryRepositoryFactory;
import edu.bbte.idde.vnim2413.utils.AppConfig;
import edu.bbte.idde.vnim2413.utils.ConfigLoader;

import java.io.IOException;
import java.util.Objects;

public abstract class RepositoryFactory {
    private static RepositoryFactory instance;

    public static synchronized RepositoryFactory getInstance() {
        AppConfig config;
        try {
            config = ConfigLoader.getConfig();
        } catch (IOException e) {
            throw new RepositoryException("Failed to load config");
        }

        if (instance == null) {
            instance = Objects.equals(config.getProfile(), "jdbc") ? new JdbcRepositoryFactory()
                    : new MemoryRepositoryFactory();
        }
        return instance;
    }

    public abstract UsedCarRepository getUsedCarRepository();
}