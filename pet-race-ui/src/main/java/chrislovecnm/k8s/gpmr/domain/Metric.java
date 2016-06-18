package chrislovecnm.k8s.gpmr.domain;

import com.datastax.driver.mapping.annotations.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * A Metric.
 */

@Table(name = "metric")
public class Metric implements Serializable {

    private static final long serialVersionUID = 1L;

    @PartitionKey
    @Column(caseSensitive = true,name = "metricId")
    private UUID metricId;

    @Column(caseSensitive = true,name = "connectionErrors")
    private Integer connectionErrors;

    @Column(caseSensitive = true,name = "writeTimeouts")
    private Integer writeTimeouts;

    @Column(caseSensitive = true,name = "readTimeouts")
    private Integer readTimeouts;

    @Column(caseSensitive = true,name = "unavailables")
    private Integer unavailables;

    @Column(caseSensitive = true,name = "otherErrors")
    private Integer otherErrors;

    @Column(caseSensitive = true,name = "retries")
    private Integer retries;

    @Column(caseSensitive = true,name = "ignores")
    private Integer ignores;

    @Column(caseSensitive = true,name = "knownHosts")
    private Integer knownHosts;

    @Column(caseSensitive = true,name = "connectedTo")
    private Integer connectedTo;

    @Column(caseSensitive = true,name = "openConnections")
    private Integer openConnections;

    @Column(caseSensitive = true,name = "reqCount")
    private Integer reqCount;

    @Column(caseSensitive = true,name = "reqMinLatency")
    private Double reqMinLatency;

    @Column(caseSensitive = true,name = "reqMaxLatency")
    private Double reqMaxLatency;

    @Column(caseSensitive = true,name = "reqMeanLatency")
    private Double reqMeanLatency;

    @Column(caseSensitive = true,name = "reqStdev")
    private Double reqStdev;

    @Column(caseSensitive = true,name = "reqMedian")
    private Double reqMedian;

    @Column(caseSensitive = true,name = "req75percentile")
    private Double req75percentile;

    @Column(caseSensitive = true,name = "req97percentile")
    private Double req97percentile;

    @Column(caseSensitive = true,name = "req98percentile")
    private Double req98percentile;

    @Column(caseSensitive = true,name = "req99percentile")
    private Double req99percentile;

    @Column(caseSensitive = true,name = "req999percentile")
    private Double req999percentile;

    @Column(caseSensitive = true,name = "dateCreated")
    private Date dateCreated;

    public UUID getMetricId() {
        return metricId;
    }

    public void setMetricId(UUID metricId) {
        this.metricId = metricId;
    }

    public Integer getConnectionErrors() {
        return connectionErrors;
    }

    public void setConnectionErrors(Integer connectionErrors) {
        this.connectionErrors = connectionErrors;
    }

    public Integer getWriteTimeouts() {
        return writeTimeouts;
    }

    public void setWriteTimeouts(Integer writeTimeouts) {
        this.writeTimeouts = writeTimeouts;
    }

    public Integer getReadTimeouts() {
        return readTimeouts;
    }

    public void setReadTimeouts(Integer readTimeouts) {
        this.readTimeouts = readTimeouts;
    }

    public Integer getUnavailables() {
        return unavailables;
    }

    public void setUnavailables(Integer unavailables) {
        this.unavailables = unavailables;
    }

    public Integer getOtherErrors() {
        return otherErrors;
    }

    public void setOtherErrors(Integer otherErrors) {
        this.otherErrors = otherErrors;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public Integer getIgnores() {
        return ignores;
    }

    public void setIgnores(Integer ignores) {
        this.ignores = ignores;
    }

    public Integer getKnownHosts() {
        return knownHosts;
    }

    public void setKnownHosts(Integer knownHosts) {
        this.knownHosts = knownHosts;
    }

    public Integer getConnectedTo() {
        return connectedTo;
    }

    public void setConnectedTo(Integer connectedTo) {
        this.connectedTo = connectedTo;
    }

    public Integer getOpenConnections() {
        return openConnections;
    }

    public void setOpenConnections(Integer openConnections) {
        this.openConnections = openConnections;
    }

    public Integer getReqCount() {
        return reqCount;
    }

    public void setReqCount(Integer reqCount) {
        this.reqCount = reqCount;
    }

    public Double getReqMinLatency() {
        return reqMinLatency;
    }

    public void setReqMinLatency(Double reqMinLatency) {
        this.reqMinLatency = reqMinLatency;
    }

    public Double getReqMaxLatency() {
        return reqMaxLatency;
    }

    public void setReqMaxLatency(Double reqMaxLatency) {
        this.reqMaxLatency = reqMaxLatency;
    }

    public Double getReqMeanLatency() {
        return reqMeanLatency;
    }

    public void setReqMeanLatency(Double reqMeanLatency) {
        this.reqMeanLatency = reqMeanLatency;
    }

    public Double getReqStdev() {
        return reqStdev;
    }

    public void setReqStdev(Double reqStdev) {
        this.reqStdev = reqStdev;
    }

    public Double getReqMedian() {
        return reqMedian;
    }

    public void setReqMedian(Double reqMedian) {
        this.reqMedian = reqMedian;
    }

    public Double getReq75percentile() {
        return req75percentile;
    }

    public void setReq75percentile(Double req75percentile) {
        this.req75percentile = req75percentile;
    }

    public Double getReq97percentile() {
        return req97percentile;
    }

    public void setReq97percentile(Double req97percentile) {
        this.req97percentile = req97percentile;
    }

    public Double getReq98percentile() {
        return req98percentile;
    }

    public void setReq98percentile(Double req98percentile) {
        this.req98percentile = req98percentile;
    }

    public Double getReq99percentile() {
        return req99percentile;
    }

    public void setReq99percentile(Double req99percentile) {
        this.req99percentile = req99percentile;
    }

    public Double getReq999percentile() {
        return req999percentile;
    }

    public void setReq999percentile(Double req999percentile) {
        this.req999percentile = req999percentile;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Metric metric = (Metric) o;
        if(metric.metricId == null || metricId == null) {
            return false;
        }
        return Objects.equals(metricId, metric.metricId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(metricId);
    }

    @Override
    public String toString() {
        return "Metric{" +
            " metricId='" + metricId + "'" +
            ", connectionErrors='" + connectionErrors + "'" +
            ", writeTimeouts='" + writeTimeouts + "'" +
            ", readTimeouts='" + readTimeouts + "'" +
            ", unavailables='" + unavailables + "'" +
            ", otherErrors='" + otherErrors + "'" +
            ", retries='" + retries + "'" +
            ", ignores='" + ignores + "'" +
            ", knownHosts='" + knownHosts + "'" +
            ", connectedTo='" + connectedTo + "'" +
            ", openConnections='" + openConnections + "'" +
            ", reqCount='" + reqCount + "'" +
            ", reqMinLatency='" + reqMinLatency + "'" +
            ", reqMaxLatency='" + reqMaxLatency + "'" +
            ", reqMeanLatency='" + reqMeanLatency + "'" +
            ", reqStdev='" + reqStdev + "'" +
            ", reqMedian='" + reqMedian + "'" +
            ", req75percentile='" + req75percentile + "'" +
            ", req97percentile='" + req97percentile + "'" +
            ", req98percentile='" + req98percentile + "'" +
            ", req99percentile='" + req99percentile + "'" +
            ", req999percentile='" + req999percentile + "'" +
            ", dateCreated='" + dateCreated + "'" +
            '}';
    }

}
