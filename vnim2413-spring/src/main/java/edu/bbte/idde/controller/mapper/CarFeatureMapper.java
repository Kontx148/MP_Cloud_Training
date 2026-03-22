package edu.bbte.idde.controller.mapper;

import edu.bbte.idde.controller.dto.CarFeatureRequestDto;
import edu.bbte.idde.controller.dto.CarFeatureResponseDto;
import edu.bbte.idde.model.CarFeature;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CarFeatureMapper {
    CarFeatureResponseDto toDto(CarFeature feature);

    @Mapping(target = "uuid", ignore = true)
    CarFeature toEntity(CarFeatureRequestDto request);
}