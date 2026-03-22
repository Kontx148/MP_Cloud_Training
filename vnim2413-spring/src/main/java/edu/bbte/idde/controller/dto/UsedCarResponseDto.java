package edu.bbte.idde.controller.dto;

import lombok.Data;

@Data
public class UsedCarResponseDto {
    private Long id;
    private String make;
    private String model;
    private int fabricationYear;
    private double price;
    private String uploadDate;
}
