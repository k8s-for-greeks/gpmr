package chrislovecnm.k8s.gpmr.domain;

import com.datastax.driver.mapping.annotations.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * A Race.
 */

@Table(name = "race")
public class Race implements Serializable {

    private static final long serialVersionUID = 1L;

    @PartitionKey
    private UUID id;

    private Integer numOfPets;

    private Integer length;

    private Integer numOfSamples;

    private UUID winnerId;

    private String winnerName;

    private String winnnerPetCategory;

    private Date startTime;

    private Date endTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public Integer getNumOfSamples() {
        return numOfSamples;
    }

    public void setNumOfSamples(Integer numOfSamples) {
        this.numOfSamples = numOfSamples;
    }

    public UUID getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(UUID winnerId) {
        this.winnerId = winnerId;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public String getWinnnerPetCategory() {
        return winnnerPetCategory;
    }

    public void setWinnnerPetCategory(String winnnerPetCategory) {
        this.winnnerPetCategory = winnnerPetCategory;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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
        if(race.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, race.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Race{" +
            "id=" + id +
            ", numOfPets='" + numOfPets + "'" +
            ", length='" + length + "'" +
            ", numOfSamples='" + numOfSamples + "'" +
            ", winnerId='" + winnerId + "'" +
            ", winnerName='" + winnerName + "'" +
            ", winnnerPetCategory='" + winnnerPetCategory + "'" +
            ", startTime='" + startTime + "'" +
            ", endTime='" + endTime + "'" +
            '}';
    }
}
