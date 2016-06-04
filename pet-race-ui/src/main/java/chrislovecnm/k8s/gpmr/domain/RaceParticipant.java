package chrislovecnm.k8s.gpmr.domain;

import com.datastax.driver.mapping.annotations.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.math.BigDecimal;

/**
 * A RaceParticipant.
 */

@Table(name = "race_participant")
public class RaceParticipant implements Serializable {

    private static final long serialVersionUID = 1L;

    @PartitionKey
    @Column(caseSensitive = true, name = "raceParticipantId")
    private UUID raceParticipantId;

    @Column(caseSensitive = true, name="petId")
    private UUID petId;

    @Column(caseSensitive = true, name="raceId")
    private UUID raceId;

    @Column(caseSensitive = true, name="petName")
    private String petName;

    @Column(caseSensitive = true, name = "petColor")
    private String petColor;

    @Column(caseSensitive = true, name="petCategoryName")
    private String petCategoryName;

    @Column(caseSensitive = true, name="petCategoryId")
    private UUID petCategoryId;

    @Column(caseSensitive = true, name="startTime")
    private Date startTime;

    @Column(caseSensitive = true, name="finishTime")
    private BigDecimal finishTime;

    @Column(caseSensitive = true, name="finishPosition")
    private Integer finishPosition;

    private Boolean finished;

    public UUID getRaceParticipantId() {
        return raceParticipantId;
    }

    public void setRaceParticipantId(UUID raceParticipantId) {
        this.raceParticipantId = raceParticipantId;
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

    public String getPetColor() {
        return petColor;
    }

    public void setPetColor(String petColor) {
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

    public BigDecimal getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(BigDecimal finishTime) {
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
        RaceParticipant raceParticipant = (RaceParticipant) o;
        if(raceParticipant.raceParticipantId == null || raceParticipantId == null) {
            return false;
        }
        return Objects.equals(raceParticipantId, raceParticipant.raceParticipantId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(raceParticipantId);
    }

    @Override
    public String toString() {
        return "RaceParticipant{" +
            ", raceParticipantId='" + raceParticipantId + "'" +
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
