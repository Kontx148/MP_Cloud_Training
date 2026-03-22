package edu.bbte.idde.model;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.Objects;
import java.util.UUID;

@Data
@MappedSuperclass
public abstract class AbstractModel {

    @Column(unique = true, nullable = false, updatable = false)
    @UuidGenerator
    private String uuid;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractModel that = (AbstractModel) o;
        return Objects.equals(getUuid(), that.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUuid());
    }

    public String getUuid() {
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
        }
        return uuid;
    }
}
