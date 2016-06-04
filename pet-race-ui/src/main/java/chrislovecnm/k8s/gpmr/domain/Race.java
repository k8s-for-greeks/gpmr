package chrislovecnm.k8s.gpmr.domain;

import com.datastax.driver.mapping.annotations.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.List;

/**
 * A Race.
 */

@Table(name = "race")
public class Race implements Serializable {

    private static final long serialVersionUID = 1L;

    @PartitionKey
    @Column(caseSensitive = true, name="raceId")
    private UUID raceId;

    @Column(caseSensitive = true, name="petCategoryId")
    private UUID petCategoryId;

    @Column(caseSensitive = true, name="petCategoryName")
    private String petCategoryName;

    @Column(caseSensitive = true, name="numOfPets")
    private Integer numOfPets;

    private Integer length;

    private String description;

    @Column(caseSensitive = true, name = "winnerId")
    private UUID winnerId;

    @Column(caseSensitive = true, name = "startTime")
    private Date startTime;

    @Column(caseSensitive = true, name = "baseSpeed")
    private Float baseSpeed;

    @Frozen("list<uuid>")
    @Column(caseSensitive = true, name = "racersIds")
    private List<UUID> racersIds;

    public UUID getRaceId() {
        return raceId;
    }

    public void setRaceId(UUID raceId) {
        this.raceId = raceId;
    }

    public UUID getPetCategoryId() {
        return petCategoryId;
    }

    public void setPetCategoryId(UUID petCategoryId) {
        this.petCategoryId = petCategoryId;
    }

    public String getPetCategoryName() {
        return petCategoryName;
    }

    public void setPetCategoryName(String petCategoryName) {
        this.petCategoryName = petCategoryName;
    }

    public Integer getNumOfPets() {
        return numOfPets;
    }

    public void setNumOfPets(Integer numOfPets) {
        this.numOfPets = numOfPets;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(UUID winnerId) {
        this.winnerId = winnerId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Float getBaseSpeed() {
        return baseSpeed;
    }

    public void setBaseSpeed(Float baseSpeed) {
        this.baseSpeed = baseSpeed;
    }

    public List<UUID> getRacersIds() {
        return racersIds;
    }
    public void setRacersIds(List<UUID> racersIds) {
        this.racersIds = racersIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Race race = (Race) o;
        if(race.raceId == null || raceId == null) {
            return false;
        }
        return Objects.equals(raceId, race.raceId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(raceId);
    }

    @Override
    public String toString() {
        return "Race{" +
            "raceId='" + raceId + "'" +
            ", petCategoryId='" + petCategoryId + "'" +
            ", petCategoryName='" + petCategoryName + "'" +
            ", numOfPets='" + numOfPets + "'" +
            ", length='" + length + "'" +
            ", description='" + description + "'" +
            ", winnerId='" + winnerId + "'" +
            ", startTime='" + startTime + "'" +
            ", baseSpeed='" + baseSpeed + "'" +
            '}';
    }
}
