package gyak.repository;

import gyak.model.Car;

import java.util.Collection;

public interface CarRepository {
    long createCar(Car car);

    Collection<Car> getAllCars();

    Collection<Car> filterByYear(Integer min, Integer max);

    Car update(Car update);

    void delete(Integer year);

}
