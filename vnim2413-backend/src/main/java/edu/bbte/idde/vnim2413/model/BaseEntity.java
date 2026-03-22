package edu.bbte.idde.vnim2413.model;

import java.io.Serial;
import java.io.Serializable;

public abstract class BaseEntity extends AbstractModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
