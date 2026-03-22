package edu.bbte.idde.controller.mapper;

import edu.bbte.idde.controller.dto.UsedCarRequestDto;
import edu.bbte.idde.controller.dto.UsedCarResponseDto;
import edu.bbte.idde.model.UsedCarListing;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsedCarMapper {
    UsedCarResponseDto toDto(UsedCarListing usedCarListing);

    @Mapping(target = "uuid", ignore = true)
    UsedCarListing toEntity(UsedCarRequestDto usedCarRequestDto);
}
