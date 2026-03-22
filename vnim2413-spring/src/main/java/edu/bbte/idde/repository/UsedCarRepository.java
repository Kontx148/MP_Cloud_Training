package edu.bbte.idde.repository;

import edu.bbte.idde.model.UsedCarListing;

import java.util.Collection;

public interface UsedCarRepository {
    long createUsedCarListing(UsedCarListing usedCarListing) throws RepositoryException;

    Collection<UsedCarListing> getUsedCars() throws RepositoryException;

    Collection<UsedCarListing> findByMake(String make) throws RepositoryException;

    UsedCarListing getUsedCar(long id) throws RepositoryException, EntityNotFoundException;

    void updateUsedCar(UsedCarListing usedCarListing) throws RepositoryException, EntityNotFoundException;

    void deleteUsedCar(long id) throws RepositoryException, EntityNotFoundException;

    boolean existsUsedCar(long id);
}
