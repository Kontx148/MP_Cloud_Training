package gyak.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name = "db_motor_jpa")
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class Motor extends BaseEntity {
    private double displacement;
    private double hp;
}
