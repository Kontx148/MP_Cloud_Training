package edu.bbte.idde.vnim2413.repository;

import edu.bbte.idde.vnim2413.model.UsedCarListing;

import java.util.Collection;

public interface UsedCarRepository {
    long createUsedCarListing(UsedCarListing usedCarListing) throws RepositoryException;

    Collection<UsedCarListing> getUsedCars();

    UsedCarListing getUsedCar(long id) throws RepositoryException;

    void updateUsedCar(UsedCarListing usedCarListing) throws RepositoryException;

    void deleteUsedCar(long id) throws RepositoryException;

    boolean existsUsedCar(long id);
}
