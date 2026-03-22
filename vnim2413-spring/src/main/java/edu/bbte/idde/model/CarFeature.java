package edu.bbte.idde.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "db_carfeatures")
public class CarFeature extends BaseEntity {
    private String featureName;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "usedcarlisting_carfeatures",
            joinColumns = @JoinColumn(name = "carfeature_id"),
            inverseJoinColumns = @JoinColumn(name = "usedcarlisting_id")
    )
    @EqualsAndHashCode.Exclude
    private Set<UsedCarListing> usedCarListings = new HashSet<>();
}
