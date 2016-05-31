package chrislovecnm.k8s.gpmr.domain;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * A RaceParticipants.
 */

@Table(name = "raceParticipants")
public class RaceParticipants implements Serializable {

    private static final long serialVersionUID = 1L;

    @PartitionKey
    private UUID id;

    private UUID raceParticipantsId;

    private UUID petId;

    private UUID raceId;

    private String petName;

    private UUID petColor;

    private String petCategoryName;

    private UUID petCategoryId;

    private Date startTime;

    private Double finishTime;

    private Integer finishPosition;

    private Boolean finished;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getRaceParticipantsId() {
        return raceParticipantsId;
    }

    public void setRaceParticipantsId(UUID raceParticipantsId) {
        this.raceParticipantsId = raceParticipantsId;
    }

    public UUID getPetId() {
        return petId;
    }

    public void setPetId(UUID petId) {
        this.petId = petId;
    }

    public UUID getRaceId() {
        return raceId;
    }

    public void setRaceId(UUID raceId) {
        this.raceId = raceId;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
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

    public UUID getPetCategoryId() {
        return petCategoryId;
    }

    public void setPetCategoryId(UUID petCategoryId) {
        this.petCategoryId = petCategoryId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Double getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Double finishTime) {
        this.finishTime = finishTime;
    }

    public Integer getFinishPosition() {
        return finishPosition;
    }

    public void setFinishPosition(Integer finishPosition) {
        this.finishPosition = finishPosition;
    }

    public Boolean isFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RaceParticipants raceParticipants = (RaceParticipants) o;
        if (raceParticipants.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, raceParticipants.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RaceParticipants{" +
            "id=" + id +
            ", raceParticipantsId='" + raceParticipantsId + "'" +
            ", petId='" + petId + "'" +
            ", raceId='" + raceId + "'" +
            ", petName='" + petName + "'" +
            ", petColor='" + petColor + "'" +
            ", petCategoryName='" + petCategoryName + "'" +
            ", petCategoryId='" + petCategoryId + "'" +
            ", startTime='" + startTime + "'" +
            ", finishTime='" + finishTime + "'" +
            ", finishPosition='" + finishPosition + "'" +
            ", finished='" + finished + "'" +
            '}';
    }
}
