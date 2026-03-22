package edu.bbte.idde.controller;

import edu.bbte.idde.controller.dto.CarFeatureRequestDto;
import edu.bbte.idde.controller.dto.CarFeatureResponseDto;
import edu.bbte.idde.controller.dto.UsedCarResponseDto;
import edu.bbte.idde.controller.mapper.CarFeatureMapper;
import edu.bbte.idde.controller.mapper.UsedCarMapper;
import edu.bbte.idde.model.CarFeature;
import edu.bbte.idde.service.CarFeatureServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("api")
@Profile("jpa")
public class CarFeatureController {

    private final CarFeatureServiceInterface carFeatureService;
    private final CarFeatureMapper carFeatureMapper;
    private final UsedCarMapper usedCarMapper;

    // List features for a given car
    @GetMapping("/usedCars/{carId}/features")
    public List<CarFeatureResponseDto> getFeaturesForCar(@PathVariable long carId) {
        return carFeatureService.getFeaturesForCar(carId).stream()
                .map(carFeatureMapper::toDto)
                .toList();
    }

    // List cars for a given feature
    @GetMapping("/features/{featureId}/usedCars")
    public List<UsedCarResponseDto> getCarsForFeature(@PathVariable long featureId) {
        return carFeatureService.getCarsForFeature(featureId).stream()
                .map(usedCarMapper::toDto)
                .toList();
    }

    // Add an existing feature to an existing car
    @PutMapping("/usedCars/{carId}/features/{featureId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addExistingFeatureToCar(@PathVariable long carId, @PathVariable long featureId) {
        carFeatureService.addFeatureToCar(carId, featureId);
    }

    // Add a new feature to an existing car
    @PostMapping("/usedCars/{carId}/features")
    @ResponseStatus(HttpStatus.CREATED)
    public CarFeatureResponseDto createFeatureAndAddToCar(
            @PathVariable long carId,
            @Valid @RequestBody CarFeatureRequestDto request
    ) {
        CarFeature feature = carFeatureMapper.toEntity(request);
        long id = carFeatureService.createFeatureAndAddToCar(carId, feature);
        feature.setId(id);
        return carFeatureMapper.toDto(feature);
    }

    // Remove a feature from a car
    @DeleteMapping("/usedCars/{carId}/features/{featureId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFeatureFromCar(@PathVariable long carId, @PathVariable long featureId) {
        carFeatureService.removeFeatureFromCar(carId, featureId);
    }

    // Add a new feature
    @PostMapping("/features")
    public CarFeatureResponseDto createFeature(@RequestBody @Valid CarFeatureRequestDto request) {
        CarFeature feature = carFeatureMapper.toEntity(request);
        long id = carFeatureService.register(feature);
        feature.setId(id);
        return carFeatureMapper.toDto(feature);
    }

    @PutMapping("/features/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUsedCar(
            @PathVariable long id,
            @Valid @RequestBody CarFeatureRequestDto request
    ) {
        CarFeature feature = carFeatureMapper.toEntity(request);
        feature.setId(id);
        carFeatureService.update(feature);
    }
}