package chrislovecnm.k8s.gpmr.domain;

import com.datastax.driver.mapping.annotations.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A PetCategory.
 */

@Table(name = "petCategory")
public class PetCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @PartitionKey
    private UUID id;

    private String name;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PetCategory petCategory = (PetCategory) o;
        if(petCategory.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, petCategory.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PetCategory{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
