package gyak.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "db_comment_jpa")
public class Comment extends BaseEntity {

    @ManyToOne(optional = false)
    private Car car;

    private String content;

    public Comment() {
        super();
    }

    public Comment(String content) {
        super();
        this.content = content;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Comment{");
        sb.append("id=").append(getId());
        sb.append(", blogPost=").append(car);
        sb.append(", content='").append(content).append('\'');
        sb.append('}');
        return sb.toString();
    }
}