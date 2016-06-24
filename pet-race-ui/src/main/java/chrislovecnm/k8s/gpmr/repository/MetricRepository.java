package chrislovecnm.k8s.gpmr.repository;

import chrislovecnm.k8s.gpmr.domain.Metric;

import com.datastax.driver.core.*;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Cassandra repository for the Metric entity.
 */
@Repository
public class MetricRepository extends CassandraPaging {


    private Mapper<Metric> mapper;

    private Integer connected;

    @PostConstruct
    public void init() {
        mapper = new MappingManager(session).mapper(Metric.class);
        createPaging("gpmr","metric");
    }

    public Page<Metric> findAll(Pageable pageable) {
        List<Metric> metrics = new ArrayList<>();
        fetchRowsWithPage(pageable).stream().map(
            row -> rowCall(row)
        ).forEach(metrics::add);
        Page<Metric> page = new PageImpl<>(metrics,pageable,metrics.size());
        return page;
    }

    public List<Metric> findAll() {
        connected = session.getCluster().getMetrics().getKnownHosts().getValue();
        List<Metric> metrics = new ArrayList<>();
        BoundStatement stmt = findAllStmt.bind();

        session.execute(stmt).all().stream().map(
            row -> rowCall(row)
        ).forEach(metrics::add);
        return metrics;
    }

    public Metric getJava() {
        Metrics metrics = session.getCluster().getMetrics();
        Metric metric = new Metric();
        metric.setKnownHosts(metrics.getConnectedToHosts().getValue());
        return metric;
    }

    private Metric rowCall(Row row) {
        Metric metric = new Metric();
        metric.setMetricId(row.getUUID("metricId"));
        metric.setConnectionErrors(row.getInt("connectionErrors"));
        metric.setWriteTimeouts(row.getInt("writeTimeouts"));
        metric.setReadTimeouts(row.getInt("readTimeouts"));
        metric.setUnavailables(row.getInt("unavailables"));
        metric.setOtherErrors(row.getInt("otherErrors"));
        metric.setRetries(row.getInt("retries"));
        metric.setIgnores(row.getInt("ignores"));
        metric.setKnownHosts(row.getInt("knownHosts"));
        metric.setConnectedTo(row.getInt("connectedTo"));
        metric.setOpenConnections(row.getInt("openConnections"));
        metric.setReqCount(row.getInt("reqCount"));
        metric.setReqMinLatency(row.getDouble("reqMinLatency"));
        metric.setReqMaxLatency(row.getDouble("reqMaxLatency"));
        metric.setReqMeanLatency(row.getDouble("reqMeanLatency"));
        metric.setReqStdev(row.getDouble("reqStdev"));
        metric.setReqMedian(row.getDouble("reqMedian"));
        metric.setReq75percentile(row.getDouble("req75percentile"));
        metric.setReq97percentile(row.getDouble("req97percentile"));
        metric.setReq98percentile(row.getDouble("req98percentile"));
        metric.setReq99percentile(row.getDouble("req99percentile"));
        metric.setReq999percentile(row.getDouble("req999percentile"));
        metric.setDateCreated(row.getTimestamp("dateCreated"));
        return metric;
    }

    public Metric findOne(UUID id) {
        return mapper.get(id);
    }

    public Metric save(Metric metric) {
        if (metric.getMetricId() == null) {
            metric.setMetricId(UUID.randomUUID());
        }
        mapper.save(metric);
        return metric;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt =  truncateStmt.bind();
        session.execute(stmt);
    }
}
