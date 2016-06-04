package chrislovecnm.k8s.gpmr.domain;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.Frozen;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A RaceNormal.
 */

@Table(name = "race_normal")
public class RaceNormal implements Serializable {

    private static final long serialVersionUID = 1L;

    @PartitionKey
    @Column(caseSensitive = true, name = "raceNormalId")
    private UUID raceNormalId;

    @Column(caseSensitive = true, name = "raceId")
    private UUID raceId;

    @Column(caseSensitive = true, name = "petCategoryId")
    private UUID petCategoryId;

    @Column(caseSensitive = true, name = "petCategoryName")
    private String petCategoryName;

    @Column(caseSensitive = true, name = "currentTime")
    private Date currentTime;

    @Column(caseSensitive = true, name = "normalLoc")
    private Float normalLoc;

    @Column(caseSensitive = true, name = "normalScale")
    private Float normalScale;

    @Column(caseSensitive = true, name = "normalSize")
    private Integer normalSize;

    @Frozen("list<decimal>")
    private List<BigDecimal> normals;

    public UUID getRaceNormalId() {
        return raceNormalId;
    }

    public void setRaceNormalId(UUID raceNormalId) {
        this.raceNormalId = raceNormalId;
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

    public String getPetCategoryName() {
        return petCategoryName;
    }

    public void setPetCategoryName(String petCategoryName) {
        this.petCategoryName = petCategoryName;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }

    public Float getNormalLoc() {
        return normalLoc;
    }

    public void setNormalLoc(Float normalLoc) {
        this.normalLoc = normalLoc;
    }

    public Float getNormalScale() {
        return normalScale;
    }

    public void setNormalScale(Float normalScale) {
        this.normalScale = normalScale;
    }

    public Integer getNormalSize() {
        return normalSize;
    }

    public void setNormalSize(Integer normalSize) {
        this.normalSize = normalSize;
    }

    public List<BigDecimal> getNormals() {
        return this.normals;
    }

    public void setNormals(List<BigDecimal> normals) {
        this.normals = normals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RaceNormal raceNormal = (RaceNormal) o;
        if (raceNormal.raceNormalId == null || raceNormalId == null) {
            return false;
        }
        return Objects.equals(raceNormalId, raceNormal.raceNormalId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(raceNormalId);
    }

    @Override
    public String toString() {
        return "RaceNormal{" +
            ", raceNormalId='" + raceNormalId + "'" +
            ", raceId='" + raceId + "'" +
            ", petCategoryId='" + petCategoryId + "'" +
            ", petCategoryName='" + petCategoryName + "'" +
            ", currentTime='" + currentTime + "'" +
            ", normalLoc='" + normalLoc + "'" +
            ", normalScale='" + normalScale + "'" +
            ", normalSize='" + normalSize + "'" +
            '}';
    }
}
