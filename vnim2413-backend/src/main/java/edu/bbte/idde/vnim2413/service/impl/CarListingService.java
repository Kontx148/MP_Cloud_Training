package edu.bbte.idde.vnim2413.service.impl;

import edu.bbte.idde.vnim2413.model.UsedCarListing;
import edu.bbte.idde.vnim2413.repository.EntityNotFoundException;
import edu.bbte.idde.vnim2413.repository.RepositoryException;
import edu.bbte.idde.vnim2413.repository.RepositoryFactory;
import edu.bbte.idde.vnim2413.service.CarListingServiceInterface;
import edu.bbte.idde.vnim2413.repository.UsedCarRepository;
import edu.bbte.idde.vnim2413.service.ServiceException;

import java.util.Collection;

public class CarListingService implements CarListingServiceInterface {
    private final UsedCarRepository usedCarRepository = RepositoryFactory.getInstance().getUsedCarRepository();

    @Override
    public long register(UsedCarListing usedCarListing) throws ServiceException {
        try {
            return usedCarRepository.createUsedCarListing(usedCarListing);
        } catch (EntityNotFoundException e) {
            throw new ServiceException(404, e.getMessage());
        } catch (RepositoryException e) {
            throw new ServiceException(500, e.getMessage());
        }
    }

    @Override
    public UsedCarListing getById(long id) throws ServiceException {
        try {
            return usedCarRepository.getUsedCar(id);
        } catch (EntityNotFoundException e) {
            throw new ServiceException(404, e.getMessage());
        } catch (RepositoryException e) {
            throw new ServiceException(500, e.getMessage());
        }
    }

    @Override
    public void delete(long id) throws ServiceException {
        try {
            usedCarRepository.deleteUsedCar(id);
        } catch (EntityNotFoundException e) {
            throw new ServiceException(404, e.getMessage());
        } catch (RepositoryException e) {
            throw new ServiceException(500, e.getMessage());
        }
    }

    @Override
    public Collection<UsedCarListing> getAllCarListings() {
        return usedCarRepository.getUsedCars();
    }

    @Override
    public void update(UsedCarListing usedCarListing) throws ServiceException {
        try {
            usedCarRepository.updateUsedCar(usedCarListing);
        } catch (EntityNotFoundException e) {
            throw new ServiceException(404, e.getMessage());
        } catch (RepositoryException e) {
            throw new ServiceException(500, e.getMessage());
        }

    }
}
