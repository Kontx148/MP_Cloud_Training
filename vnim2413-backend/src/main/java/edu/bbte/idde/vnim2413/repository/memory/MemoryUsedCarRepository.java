package edu.bbte.idde.vnim2413.repository.memory;

import edu.bbte.idde.vnim2413.model.UsedCarListing;
import edu.bbte.idde.vnim2413.repository.EntityNotFoundException;
import edu.bbte.idde.vnim2413.repository.UsedCarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MemoryUsedCarRepository implements UsedCarRepository {
    private final List<UsedCarListing> usedCarListings = new ArrayList<>();

    private static final Logger LOG = LoggerFactory.getLogger(MemoryUsedCarRepository.class);
    private long nextId = 1;

    @Override
    public long createUsedCarListing(UsedCarListing usedCarListing) {
        LOG.info("""
                            Requested car listing creation for
                            Make: {}
                            Model: {}
                            Prod Year: {}
                            Price: {}
                            Listing date: {}""",
                usedCarListing.getMake(), usedCarListing.getModel(), usedCarListing.getFabricationYear(),
                usedCarListing.getPrice(), usedCarListing.getUploadDate()
        );
        usedCarListing.setId(nextId++);
        usedCarListings.add(usedCarListing);
        LOG.info("Car listing created successfully!");
        return  usedCarListing.getId();
    }

    @Override
    public Collection<UsedCarListing> getUsedCars() {
        LOG.info("Requested car listings from database");
        return usedCarListings;
    }

    @Override
    public UsedCarListing getUsedCar(long id) {
        LOG.info("Requested used car listing from database, with ID: {}", id);
        return usedCarListings.stream()
                .filter(car -> car.getId() == id)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Used car with ID " + id + " not found in memory"));
    }

    @Override
    public void updateUsedCar(UsedCarListing updatedCar) {
        LOG.info("Requested update on car listing with ID: {}", updatedCar.getId());
        boolean updated = false;
        for (int i = 0; i < usedCarListings.size(); i++) {
            UsedCarListing current = usedCarListings.get(i);
            if (current.getId().equals(updatedCar.getId())) {
                usedCarListings.set(i, updatedCar);
                updated = true;
                break;
            }
        }
        if (!updated) {
            throw new EntityNotFoundException(
                    "Cannot update: used car with ID " + updatedCar.getId() + " not found in memory");
        }
    }

    @Override
    public void deleteUsedCar(long id) {
        LOG.info("Requested deletion on car listing with ID: {}", id);
        for (int i = 0; i < usedCarListings.size(); i++) {
            UsedCarListing current = usedCarListings.get(i);
            if (current.getId().equals(id)) {
                usedCarListings.remove(i);
                return;
            }
        }

        // Throw error if we didn't find it
        throw new EntityNotFoundException("Cannot delete: used car with ID " + id + " not found in memory");
    }

    @Override
    public boolean existsUsedCar(long id) {
        return usedCarListings.stream()
                .anyMatch(car -> car.getId() == id);
    }
}
