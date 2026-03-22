package edu.bbte.idde.controller;

import edu.bbte.idde.controller.dto.UsedCarRequestDto;
import edu.bbte.idde.controller.dto.UsedCarResponseDto;
import edu.bbte.idde.controller.mapper.UsedCarMapper;
import edu.bbte.idde.model.UsedCarListing;
import edu.bbte.idde.service.CarListingServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("api/usedCars")
public class UsedCarController {

    final CarListingServiceInterface usedCarService;
    final UsedCarMapper usedCarMapper;

    // Get all used cars or filter by make if provided
    @GetMapping
    public List<UsedCarResponseDto> getUsers(@RequestParam(required = false) String make) {
        if (make != null) {
            return usedCarService.getByMake(make).stream().map(usedCarMapper::toDto).toList();
        }
        return usedCarService.getAllCarListings().stream().map(usedCarMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public UsedCarResponseDto getUsedCarById(@PathVariable long id) {
        var usedCar = usedCarService.getById(id);
        return usedCarMapper.toDto(usedCar);
    }


    @PostMapping
    public UsedCarResponseDto createUsedCar(@RequestBody @Valid UsedCarRequestDto request) {
        UsedCarListing usedCarListing = usedCarMapper.toEntity(request);
        long createdId = usedCarService.register(usedCarListing);
        usedCarListing.setId(createdId);
        return usedCarMapper.toDto(usedCarListing);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUsedCar(
            @PathVariable long id,
            @Valid @RequestBody UsedCarRequestDto request
    ) {
        UsedCarListing usedCarListing = usedCarMapper.toEntity(request);
        usedCarListing.setId(id);
        usedCarService.update(usedCarListing);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUsedCar(@PathVariable long id) {
        usedCarService.delete(id);
    }

}
