package chrislovecnm.k8s.gpmr.domain;

import com.datastax.driver.mapping.annotations.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A Pet.
 */

@Table(name = "pet")
public class Pet implements Serializable {

    private static final long serialVersionUID = 1L;

    @PartitionKey
    @Column(caseSensitive = true,name = "petId")
    private UUID petId;

    private String name;

    private String description;

    @Column(caseSensitive = true, name="petCategoryName")
    private String petCategoryName;

    @Column(caseSensitive = true, name = "petCategoryId")
    private UUID petCategoryId;

    @Column(caseSensitive = true, name="petSpeed")
    private Float petSpeed;

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
        Pet pet = (Pet) o;
        if(pet.petId == null || petId == null) {
            return false;
        }
        return Objects.equals(petId, pet.petId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(petId);
    }

    @Override
    public String toString() {
        return "Pet{" +
            ", petId='" + petId + "'" +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", petCategoryName='" + petCategoryName + "'" +
            ", petCategoryId='" + petCategoryId + "'" +
            ", petSpeed='" + petSpeed + "'" +
            '}';
    }
}
