package chrislovecnm.k8s.gpmr.domain;

import com.datastax.driver.mapping.annotations.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * A RaceResult.
 */

@Table(name = "raceResult")
public class RaceResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @PartitionKey
    private UUID id;

    private UUID petId;

    private String petName;

    private String petCategory;

    private UUID petCategoryId;

    private Integer place;

    private Date time;

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

    public Integer getPlace() {
        return place;
    }

    public void setPlace(Integer place) {
        this.place = place;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RaceResult raceResult = (RaceResult) o;
        if(raceResult.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, raceResult.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RaceResult{" +
            "id=" + id +
            ", petId='" + petId + "'" +
            ", petName='" + petName + "'" +
            ", petCategory='" + petCategory + "'" +
            ", petCategoryId='" + petCategoryId + "'" +
            ", place='" + place + "'" +
            ", time='" + time + "'" +
            '}';
    }
}
