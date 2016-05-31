package chrislovecnm.k8s.gpmr.domain;

import com.datastax.driver.mapping.annotations.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A Pets.
 */

@Table(name = "pets")
public class Pets implements Serializable {

    private static final long serialVersionUID = 1L;

    @PartitionKey
    private UUID id;

    private UUID petId;

    private String name;

    private String description;

    private String petCategoryName;

    private UUID petCategoryId;

    private Float petSpeed;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPetId() {
        return petId;
    }

    public void setPetId(UUID petId) {
        this.petId = petId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPetCategoryName() {
        return petCategoryName;
    }

    public void setPetCategoryName(String petCategoryName) {
        this.petCategoryName = petCategoryName;
    }

    public UUID getPetCategoryId() {
        return petCategoryId;
    }

    public void setPetCategoryId(UUID petCategoryId) {
        this.petCategoryId = petCategoryId;
    }

    public Float getPetSpeed() {
        return petSpeed;
    }

    public void setPetSpeed(Float petSpeed) {
        this.petSpeed = petSpeed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pets pets = (Pets) o;
        if(pets.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, pets.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Pets{" +
            "id=" + id +
            ", petId='" + petId + "'" +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", petCategoryName='" + petCategoryName + "'" +
            ", petCategoryId='" + petCategoryId + "'" +
            ", petSpeed='" + petSpeed + "'" +
            '}';
    }
}
