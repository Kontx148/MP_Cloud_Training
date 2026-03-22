package edu.bbte.idde.service.impl.jpa;

import edu.bbte.idde.model.UsedCarListing;
import edu.bbte.idde.repository.EntityNotFoundException;
import edu.bbte.idde.repository.jpa.JpaUsedCarRepository;
import edu.bbte.idde.service.CarListingServiceInterface;
import edu.bbte.idde.service.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Profile("jpa")
@Slf4j
public class CarListingJpaService implements CarListingServiceInterface {

    @Autowired
    private JpaUsedCarRepository usedCarRepository;


    @Override
    public long register(UsedCarListing usedCarListing) throws ServiceException {
        return usedCarRepository.save(usedCarListing).getId();
    }

    @Override
    public UsedCarListing getById(long id) throws ServiceException {
        return usedCarRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Used car listing not found"));

    }

    @Override
    public void delete(long id) throws ServiceException {
        if (!usedCarRepository.existsById(id)) {
            throw new EntityNotFoundException("Used car listing not found");
        }
        usedCarRepository.deleteById(id);
    }


    @Override
    public Collection<UsedCarListing> getAllCarListings() throws ServiceException {
        return usedCarRepository.findAll();
    }

    @Override
    public Collection<UsedCarListing> getByMake(String make) throws ServiceException {
        return usedCarRepository.findByMake(make);
    }

    @Override
    public void update(UsedCarListing usedCarListing) throws ServiceException {
        if (!usedCarRepository.existsById(usedCarListing.getId())) {
            throw new EntityNotFoundException("Used car listing not found");
        }
        usedCarRepository.save(usedCarListing);
    }
}