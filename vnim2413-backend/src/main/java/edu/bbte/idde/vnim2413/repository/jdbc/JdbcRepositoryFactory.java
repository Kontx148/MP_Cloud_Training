package edu.bbte.idde.vnim2413.repository.jdbc;

import edu.bbte.idde.vnim2413.repository.RepositoryFactory;
import edu.bbte.idde.vnim2413.repository.UsedCarRepository;

public class JdbcRepositoryFactory extends RepositoryFactory {
    private static final UsedCarRepository instance = new JdbcUsedCarRepository();

    @Override
    public UsedCarRepository getUsedCarRepository() {
        return instance;
    }
}
