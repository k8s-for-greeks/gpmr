package chrislovecnm.k8s.gpmr.domain;

import com.datastax.driver.mapping.annotations.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A Pet.
 */

@Table(name = "pet")
public class Pet implements Serializable {

    private static final long serialVersionUID = 1L;

    @PartitionKey
    private UUID id;

    private String name;

    private String petCategory;

    private UUID petCategoryId;

    private BigDecimal petSpeed;

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

    public String getPetCategory() {
        return petCategory;
    }

    public void setPetCategory(String petCategory) {
        this.petCategory = petCategory;
    }

    public UUID getPetCategoryId() {
        return petCategoryId;
    }

    public void setPetCategoryId(UUID petCategoryId) {
        this.petCategoryId = petCategoryId;
    }

    public BigDecimal getPetSpeed() {
        return petSpeed;
    }

    public void setPetSpeed(BigDecimal petSpeed) {
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
        if(pet.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, pet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Pet{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", petCategory='" + petCategory + "'" +
            ", petCategoryId='" + petCategoryId + "'" +
            ", petSpeed='" + petSpeed + "'" +
            '}';
    }
}
