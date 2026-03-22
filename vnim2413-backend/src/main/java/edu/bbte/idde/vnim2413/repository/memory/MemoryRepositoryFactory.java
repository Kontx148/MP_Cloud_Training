package edu.bbte.idde.vnim2413.repository.memory;

import edu.bbte.idde.vnim2413.repository.RepositoryFactory;
import edu.bbte.idde.vnim2413.repository.UsedCarRepository;

public class MemoryRepositoryFactory extends RepositoryFactory {
    private static final UsedCarRepository instance = new MemoryUsedCarRepository();

    @Override
    public UsedCarRepository getUsedCarRepository() {
        return instance;
    }
}
