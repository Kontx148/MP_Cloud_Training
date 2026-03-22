package edu.bbte.idde.service.impl.jpa;

import edu.bbte.idde.model.CarFeature;
import edu.bbte.idde.model.UsedCarListing;
import edu.bbte.idde.repository.EntityNotFoundException;
import edu.bbte.idde.repository.jpa.JpaCarFeatureRepository;
import edu.bbte.idde.repository.jpa.JpaUsedCarRepository;
import edu.bbte.idde.service.CarFeatureServiceInterface;
import edu.bbte.idde.service.ServiceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Profile("jpa")
@Slf4j
@RequiredArgsConstructor
public class CarFeatureJpaService implements CarFeatureServiceInterface {

    private final JpaUsedCarRepository usedCarRepository;
    private final JpaCarFeatureRepository carFeatureRepository;

    @Override
    @Transactional
    public long register(CarFeature carFeature) throws ServiceException {
        return carFeatureRepository.save(carFeature).getId();
    }

    @Override
    @Transactional
    public void update(CarFeature carFeature) throws ServiceException {
        CarFeature feature = carFeatureRepository.findById(carFeature.getId())
                .orElseThrow(() -> new EntityNotFoundException("Car feature not found"));

        feature.setFeatureName(carFeature.getFeatureName());
        carFeatureRepository.save(feature);
    }

    @Override
    @Transactional
    public Collection<CarFeature> getFeaturesForCar(long carId) throws ServiceException {
        UsedCarListing car = usedCarRepository.findById(carId)
                .orElseThrow(() -> new EntityNotFoundException("Used car listing not found"));
        return car.getCarFeatures();
    }

    @Override
    @Transactional
    public Collection<UsedCarListing> getCarsForFeature(long featureId) throws ServiceException {
        CarFeature feature = carFeatureRepository.findById(featureId)
                .orElseThrow(() -> new EntityNotFoundException("Car feature not found"));
        return feature.getUsedCarListings();
    }

    @Override
    @Transactional
    public void addFeatureToCar(long carId, long featureId) throws ServiceException {
        UsedCarListing car = usedCarRepository.findById(carId)
                .orElseThrow(() -> new EntityNotFoundException("Used car listing not found"));
        CarFeature feature = carFeatureRepository.findById(featureId)
                .orElseThrow(() -> new EntityNotFoundException("Car feature not found"));

        // bidirectional update
        car.getCarFeatures().add(feature);
        feature.getUsedCarListings().add(car);

        carFeatureRepository.save(feature);
    }

    @Override
    @Transactional
    public void removeFeatureFromCar(long carId, long featureId) throws ServiceException {
        UsedCarListing car = usedCarRepository.findById(carId)
                .orElseThrow(() -> new EntityNotFoundException("Used car listing not found"));
        CarFeature feature = carFeatureRepository.findById(featureId)
                .orElseThrow(() -> new EntityNotFoundException("Car feature not found"));

        car.getCarFeatures().remove(feature);
        feature.getUsedCarListings().remove(car);

        carFeatureRepository.save(feature);
    }

    @Override
    @Transactional
    public long createFeatureAndAddToCar(long carId, CarFeature feature) throws ServiceException {
        UsedCarListing car = usedCarRepository.findById(carId)
                .orElseThrow(() -> new EntityNotFoundException("Used car listing not found"));

        feature.getUsedCarListings().add(car);
        car.getCarFeatures().add(feature);

        return carFeatureRepository.save(feature).getId();
    }
}