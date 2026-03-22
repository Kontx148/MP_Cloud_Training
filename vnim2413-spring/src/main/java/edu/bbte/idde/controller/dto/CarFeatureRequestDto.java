package edu.bbte.idde.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CarFeatureRequestDto {
    @NotBlank
    @Size(min = 2, max = 50)
    private String featureName;
}