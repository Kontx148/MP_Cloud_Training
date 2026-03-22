package gyak.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "db_cars_jpa")
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@NamedQueries({
        @NamedQuery(name = Car.FIND_BY_YEAR, query = "from Car where year >= :min and year <= :max"),
        @NamedQuery(name = Car.FIND_ALL, query = "from Car"),
})
@Slf4j
public class Car extends BaseEntity {

    public static final String FIND_ALL = "Car.findAll";
    public static final String FIND_BY_YEAR = "Car.findByYear";

    private String model;

    @Column(length = 23)
    private String make;
    @Temporal(TemporalType.DATE)
    private int year;
    private LocalDateTime createdAt = null;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @PrePersist
    protected void beforePersists() {
        createdAt = LocalDateTime.now();
        log.info("Created car created at, wtf is this log xd");
    }

    @Override
    public String toString() {
        return "Car{" +
                "id='" + getId() + '\'' +
                "model='" + model + '\'' +
                ", make='" + make + '\'' +
                ", year=" + year + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    //    private Motor motor;
//
//    private Collection<Tire> tires;
}
