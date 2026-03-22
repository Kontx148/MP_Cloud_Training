package edu.bbte.idde.service;

import edu.bbte.idde.model.UsedCarListing;

import java.util.Collection;

public interface CarListingServiceInterface {
    long register(UsedCarListing usedCarListing) throws ServiceException;

    UsedCarListing getById(long id) throws ServiceException;

    void delete(long id) throws ServiceException;

    Collection<UsedCarListing> getAllCarListings() throws ServiceException;

    Collection<UsedCarListing> getByMake(String make) throws ServiceException;

    void update(UsedCarListing usedCarListing) throws ServiceException;
}
