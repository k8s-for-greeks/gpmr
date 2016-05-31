package chrislovecnm.k8s.gpmr.domain;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A RaceParticipant.
 */

@Table(name = "raceParticipant")
public class RaceParticipant implements Serializable {

    private static final long serialVersionUID = 1L;

    @PartitionKey
    private UUID id;

    private UUID petId;

    private String petName;

    private String petType;

    private String petColor;

    private String petCategory;

    private String petCategoryId;

    private UUID raceId;

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

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public String getPetColor() {
        return petColor;
    }

    public void setPetColor(String petColor) {
        this.petColor = petColor;
    }

    public String getPetCategory() {
        return petCategory;
    }

    public void setPetCategory(String petCategory) {
        this.petCategory = petCategory;
    }

    public String getPetCategoryId() {
        return petCategoryId;
    }

    public void setPetCategoryId(String petCategoryId) {
        this.petCategoryId = petCategoryId;
    }

    public UUID getRaceId() {
        return raceId;
    }

    public void setRaceId(UUID raceId) {
        this.raceId = raceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RaceParticipant raceParticipant = (RaceParticipant) o;
        if (raceParticipant.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, raceParticipant.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RaceParticipant{" +
            "id=" + id +
            ", petId='" + petId + "'" +
            ", petName='" + petName + "'" +
            ", petType='" + petType + "'" +
            ", petColor='" + petColor + "'" +
            ", petCategory='" + petCategory + "'" +
            ", petCategoryId='" + petCategoryId + "'" +
            ", raceId='" + raceId + "'" +
            '}';
    }
}
