package edu.bbte.idde.service.impl;

import edu.bbte.idde.model.UsedCarListing;
import edu.bbte.idde.repository.RepositoryException;
import edu.bbte.idde.repository.UsedCarRepository;
import edu.bbte.idde.service.CarListingServiceInterface;
import edu.bbte.idde.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Profile("!jpa")
public class CarListingService implements CarListingServiceInterface {
    @Autowired
    private UsedCarRepository usedCarRepository;

    @Override
    public long register(UsedCarListing usedCarListing) throws ServiceException {
        try {
            return usedCarRepository.createUsedCarListing(usedCarListing);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public UsedCarListing getById(long id) throws ServiceException {
        try {
            return usedCarRepository.getUsedCar(id);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void delete(long id) throws ServiceException {
        try {
            usedCarRepository.deleteUsedCar(id);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Collection<UsedCarListing> getAllCarListings() throws ServiceException {
        try {
            return usedCarRepository.getUsedCars();
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Collection<UsedCarListing> getByMake(String make) throws ServiceException {
        try {
            return usedCarRepository.findByMake(make);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void update(UsedCarListing usedCarListing) throws ServiceException {
        try {
            usedCarRepository.updateUsedCar(usedCarListing);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }

    }
}
