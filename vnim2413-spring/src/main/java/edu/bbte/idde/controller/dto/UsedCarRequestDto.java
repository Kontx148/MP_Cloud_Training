package edu.bbte.idde.controller.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UsedCarRequestDto {
    @NotNull
    @Size(min = 2, max = 50)
    @Pattern(regexp = "^[a-zA-Z ]+")
    private String make;

    @NotNull
    @Size(min = 1, max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9 \\-]+")
    private String model;

    @NotNull
    @Min(1886)
    private int fabricationYear;

    @NotNull
    private double price;

    @Pattern(
        regexp = "^\\d{4}-\\d{2}-\\d{2}",
        message = "Date must be in YYYY-MM-DD format"
    )
    private String uploadDate;
}
