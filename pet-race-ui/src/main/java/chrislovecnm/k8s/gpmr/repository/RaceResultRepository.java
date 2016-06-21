package chrislovecnm.k8s.gpmr.repository;

import chrislovecnm.k8s.gpmr.domain.Race;
import chrislovecnm.k8s.gpmr.domain.RaceResult;

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
 * Cassandra repository for the RaceResult entity.
 */
@Repository
public class RaceResultRepository extends  CassandraPaging {

    private Mapper<RaceResult> mapper;

    @PostConstruct
    public void init() {
        mapper = new MappingManager(session).mapper(RaceResult.class);
        createPaging("gpmr","race_result");
    }

    public Page<RaceResult> findAll(Pageable pageable) {
        List<RaceResult> races = new ArrayList<>();

        fetchRowsWithPage(pageable).stream().map(
            row -> rowCall(row)
        ).forEach(races::add);
        Page<RaceResult> page = new PageImpl<>(races,pageable,races.size());
        return page;
    }

    private RaceResult rowCall(Row row) {
        RaceResult raceResult = new RaceResult();
        raceResult.setRaceResultId(row.getUUID("raceResultId"));
        raceResult.setRaceId(row.getUUID("raceId"));
        raceResult.setPetCategoryId(row.getUUID("petCategoryId"));
        raceResult.setRaceParticipantId(row.getUUID("raceParticipantId"));
        raceResult.setPetName(row.getString("petName"));
        raceResult.setPetType(row.getString("petType"));
        raceResult.setPetColor(row.getString("petColor"));
        raceResult.setPetCategoryName(row.getString("petCategoryName"));
        raceResult.setFinishPosition(row.getInt("finishPosition"));
        raceResult.setFinishTime(row.getDecimal("finishTime"));
        raceResult.setStartTime(row.getTimestamp("startTime"));
        return raceResult;
    }

    public List<RaceResult> findAll() {
        List<RaceResult> raceResults = new ArrayList<>();
        BoundStatement stmt =  findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> rowCall(row)
        ).forEach(raceResults::add);
        return raceResults;
    }

    public RaceResult findOne(UUID id) {
        return mapper.get(id);
    }

    public RaceResult save(RaceResult raceResult) {
        if (raceResult.getRaceResultId() == null) {
            raceResult.setRaceResultId(UUID.randomUUID());
        }
        mapper.save(raceResult);
        return raceResult;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt =  truncateStmt.bind();
        session.execute(stmt);
    }
}
