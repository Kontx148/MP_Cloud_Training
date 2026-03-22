package edu.bbte.idde.service;

import edu.bbte.idde.model.CarFeature;
import edu.bbte.idde.model.UsedCarListing;

import java.util.Collection;

public interface CarFeatureServiceInterface {

    long register(CarFeature carFeature) throws ServiceException;

    void update(CarFeature carFeature) throws ServiceException;

    Collection<CarFeature> getFeaturesForCar(long carId) throws ServiceException;

    Collection<UsedCarListing> getCarsForFeature(long featureId) throws ServiceException;

    void addFeatureToCar(long carId, long featureId) throws ServiceException;

    void removeFeatureFromCar(long carId, long featureId) throws ServiceException;

    long createFeatureAndAddToCar(long carId, CarFeature feature) throws ServiceException;
}