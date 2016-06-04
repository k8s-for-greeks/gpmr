package chrislovecnm.k8s.gpmr.domain;

import com.datastax.driver.mapping.annotations.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A PetCategory.
 */

@Table(name = "pet_category")
public class PetCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @PartitionKey
    @Column(caseSensitive = true, name="petCategoryId")
    private UUID petCategoryId;

    private String name;

    private Float speed;

    public UUID getPetCategoryId() {
        return petCategoryId;
    }

    public void setPetCategoryId(UUID petCategoryId) {
        this.petCategoryId = petCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
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
        if(petCategory.petCategoryId == null || petCategoryId == null) {
            return false;
        }
        return Objects.equals(petCategoryId, petCategory.petCategoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(petCategoryId);
    }

    @Override
    public String toString() {
        return "PetCategory{" +
            "petCategoryId='" + petCategoryId + "'" +
            ", name='" + name + "'" +
            ", speed='" + speed + "'" +
            '}';
    }
}
