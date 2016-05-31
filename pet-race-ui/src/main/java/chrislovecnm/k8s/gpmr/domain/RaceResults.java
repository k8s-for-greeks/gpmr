package chrislovecnm.k8s.gpmr.domain;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * A RaceResults.
 */

@Table(name = "raceResults")
public class RaceResults implements Serializable {

    private static final long serialVersionUID = 1L;

    @PartitionKey
    private UUID id;

    private UUID raceResultsId;

    private UUID raceId;

    private UUID petCategoryId;

    private UUID raceParticipantsId;

    private String petName;

    private String petType;

    private UUID petColor;

    private String petCategoryName;

    private Integer finishPosition;

    private Double finishTime;

    private Date startTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getRaceResultsId() {
        return raceResultsId;
    }

    public void setRaceResultsId(UUID raceResultsId) {
        this.raceResultsId = raceResultsId;
    }

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

    public UUID getRaceParticipantsId() {
        return raceParticipantsId;
    }

    public void setRaceParticipantsId(UUID raceParticipantsId) {
        this.raceParticipantsId = raceParticipantsId;
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

    public UUID getPetColor() {
        return petColor;
    }

    public void setPetColor(UUID petColor) {
        this.petColor = petColor;
    }

    public String getPetCategoryName() {
        return petCategoryName;
    }

    public void setPetCategoryName(String petCategoryName) {
        this.petCategoryName = petCategoryName;
    }

    public Integer getFinishPosition() {
        return finishPosition;
    }

    public void setFinishPosition(Integer finishPosition) {
        this.finishPosition = finishPosition;
    }

    public Double getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Double finishTime) {
        this.finishTime = finishTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RaceResults raceResults = (RaceResults) o;
        if (raceResults.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, raceResults.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RaceResults{" +
            "id=" + id +
            ", raceResultsId='" + raceResultsId + "'" +
            ", raceId='" + raceId + "'" +
            ", petCategoryId='" + petCategoryId + "'" +
            ", raceParticipantsId='" + raceParticipantsId + "'" +
            ", petName='" + petName + "'" +
            ", petType='" + petType + "'" +
            ", petColor='" + petColor + "'" +
            ", petCategoryName='" + petCategoryName + "'" +
            ", finishPosition='" + finishPosition + "'" +
            ", finishTime='" + finishTime + "'" +
            ", startTime='" + startTime + "'" +
            '}';
    }
}
