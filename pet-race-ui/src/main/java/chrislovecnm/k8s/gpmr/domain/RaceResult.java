package chrislovecnm.k8s.gpmr.domain;

import com.datastax.driver.mapping.annotations.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * A RaceResult.
 */

@Table(name = "race_result", caseSensitiveTable = true)
public class RaceResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @PartitionKey
    @Column(caseSensitive = true, name = "raceResultId")
    private UUID raceResultId;

    @Column(caseSensitive = true, name = "raceId")
    private UUID raceId;

    @Column(caseSensitive = true, name = "petCategoryId")
    private UUID petCategoryId;

    @Column(caseSensitive = true, name = "raceParticipantId")
    private UUID raceParticipantId;

    @Column(caseSensitive = true, name = "petName")
    private String petName;

    @Column(caseSensitive = true, name = "petType")
    private String petType;

    @Column(caseSensitive = true, name = "petColor")
    private String petColor;

    @Column(caseSensitive = true, name = "petCategoryName")
    private String petCategoryName;

    @Column(caseSensitive = true, name = "finishPosition")
    private Integer finishPosition;

    @Column(caseSensitive = true, name = "finishTime")
    private BigDecimal finishTime;

    @Column(caseSensitive = true, name = "startTime")
    private Date startTime;

    public UUID getRaceResultId() {
        return raceResultId;
    }

    public void setRaceResultId(UUID raceResultId) {
        this.raceResultId = raceResultId;
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

    public UUID getRaceParticipantId() {
        return raceParticipantId;
    }

    public void setRaceParticipantId(UUID raceParticipantId) {
        this.raceParticipantId = raceParticipantId;
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

    public BigDecimal getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(BigDecimal finishTime) {
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
        RaceResult raceResult = (RaceResult) o;
        if(raceResult.raceResultId == null || raceResultId == null) {
            return false;
        }
        return Objects.equals(raceResultId, raceResult.raceResultId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(raceResultId);
    }

    @Override
    public String toString() {
        return "RaceResult{" +
            ", raceResultId='" + raceResultId + "'" +
            ", raceId='" + raceId + "'" +
            ", petCategoryId='" + petCategoryId + "'" +
            ", raceParticipantId='" + raceParticipantId + "'" +
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
