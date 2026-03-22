package edu.bbte.idde.vnim2413.service;

import edu.bbte.idde.vnim2413.model.UsedCarListing;

import java.util.Collection;

public interface CarListingServiceInterface {
    long register(UsedCarListing usedCarListing) throws ServiceException;

    UsedCarListing getById(long id) throws ServiceException;

    void delete(long id) throws ServiceException;

    Collection<UsedCarListing> getAllCarListings();

    void update(UsedCarListing usedCarListing) throws ServiceException;
}
