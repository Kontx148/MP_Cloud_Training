package edu.bbte.idde.vnim2413.model;


import java.util.Objects;
import java.util.UUID;

public abstract class AbstractModel {
    private String uuid;


    public String getUuid() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID().toString();
        }

        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbstractModel that = (AbstractModel) o;
            return Objects.equals(this.getUuid(), that.getUuid());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getUuid());
    }
}
