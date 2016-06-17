package chrislovecnm.k8s.gpmr.repository;

import chrislovecnm.k8s.gpmr.domain.DataCounter;

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

/**
 * Cassandra repository for the DataCounter entity.
 */
@Repository
public class DataCounterRepository extends CassandraPaging {

    private Mapper<DataCounter> mapper;

    @PostConstruct
    public void init() {
        mapper = new MappingManager(session).mapper(DataCounter.class);
        createPaging(mapper,"gpmr","data_counter");
    }
    public Page<DataCounter> findAll(Pageable pageable) {
       List<DataCounter> dataCounters = new ArrayList<>();
        fetchRowsWithPage(pageable.getOffset(), pageable.getPageSize()).stream().map(
            row -> rowCall(row)
        ).forEach(dataCounters::add);
        return new PageImpl<>(dataCounters,pageable,dataCounters.size());
    }

    public List<DataCounter> findAll() {
        List<DataCounter> dataCounters = new ArrayList<>();
        BoundStatement stmt =  findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> rowCall(row)
        ).forEach(dataCounters::add);
        return dataCounters;
    }

    private DataCounter rowCall(Row row) {
        DataCounter dataCounter = new DataCounter();
        dataCounter.setVtype(row.getString("vtype"));
        dataCounter.setValue(row.getLong("value"));
        return dataCounter;
    }

    public DataCounter findOne(String id) {
        return mapper.get(id);
    }

    public DataCounter save(DataCounter dataCounter) {
        mapper.save(dataCounter);
        return dataCounter;
    }

    public void delete(String vType) { mapper.delete(vType); }

    public void deleteAll() {
        BoundStatement stmt =  truncateStmt.bind();
        session.execute(stmt);
    }
}
