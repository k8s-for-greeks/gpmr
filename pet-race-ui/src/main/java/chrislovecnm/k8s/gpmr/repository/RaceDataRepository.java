package chrislovecnm.k8s.gpmr.repository;

import chrislovecnm.k8s.gpmr.domain.RaceData;

import chrislovecnm.k8s.gpmr.domain.RaceNormal;
import chrislovecnm.k8s.gpmr.domain.RaceParticipant;
import com.datastax.driver.core.*;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Cassandra repository for the RaceData entity.
 */
@Repository
public class RaceDataRepository extends CassandraPaging {

    private Mapper<RaceData> mapper;


    @PostConstruct
    public void init() {
        mapper = new MappingManager(session).mapper(RaceData.class);
        createPaging(mapper,"gpmr","race_data");
    }

   public Page<RaceData> findAll(Pageable pageable) {
        List<RaceData> raceData = new ArrayList<>();
        fetchRowsWithPage(pageable.getOffset(), pageable.getPageSize()).stream().map(
            row -> rowCall(row)
        ).forEach(raceData::add);
       Page<RaceData> page = new PageImpl<>(raceData,pageable,raceData.size());
       return page;
    }

    private RaceData rowCall(Row row) {
        RaceData rd = new RaceData();
        rd.setRaceDataId(row.getUUID("raceDataId"));
        rd.setPetId(row.getUUID("petId"));
        rd.setRaceId(row.getUUID("raceId"));
        rd.setPetName(row.getString("petName"));
        rd.setPetCategoryName(row.getString("petCategoryName"));
        rd.setPetCategoryId(row.getUUID("petCategoryId"));
        rd.setInterval(row.getInt("interval"));
        rd.setRunnerPosition(row.getInt("runnerPosition"));
        rd.setRunnerDistance(row.getDecimal("runnerDistance"));
        rd.setStartTime(row.getTimestamp("startTime"));
        rd.setFinished(row.getBool("finished"));
        rd.setRunnerPreviousDistance(row.getDecimal("runnerPreviousDistance"));
        return rd;
    }

    public List<RaceData> findAll() {
        List<RaceData> raceData = new ArrayList<>();
        BoundStatement stmt =  findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> rowCall(row)
        ).forEach(raceData::add);
        return raceData;
    }

    public RaceData findOne(UUID id) {
        return mapper.get(id);
    }

    public RaceData save(RaceData raceData) {
        if (raceData.getRaceDataId() == null) {
            raceData.setRaceDataId(UUID.randomUUID());
        }
        mapper.save(raceData);
        return raceData;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt =  truncateStmt.bind();
        session.execute(stmt);
    }
}
