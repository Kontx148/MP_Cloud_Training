package edu.bbte.idde.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "db_usedcarlistings")
public class UsedCarListing extends BaseEntity {

    private String make;
    private String model;
    private int fabricationYear;
    private double price;
    private String uploadDate;

    @ManyToMany(mappedBy = "usedCarListings", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<CarFeature> carFeatures = new HashSet<>();
}
