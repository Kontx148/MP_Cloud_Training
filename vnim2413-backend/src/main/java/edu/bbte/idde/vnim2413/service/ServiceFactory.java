package edu.bbte.idde.vnim2413.service;

import edu.bbte.idde.vnim2413.service.impl.CarListingService;

public class ServiceFactory {
    private static final CarListingServiceInterface instance = new CarListingService();

    public static CarListingServiceInterface getUserService() {
        return instance;
    }
}
