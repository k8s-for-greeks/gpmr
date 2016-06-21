package chrislovecnm.k8s.gpmr.repository;

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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Cassandra repository for the RaceParticipant entity.
 */
@Repository
public class RaceParticipantRepository extends CassandraPaging {

    private Mapper<RaceParticipant> mapper;

    @PostConstruct
    public void init() {
        mapper = new MappingManager(session).mapper(RaceParticipant.class);
        createPaging("gpmr","race_participant");
    }


    private RaceParticipant rowCall(Row row) {
        RaceParticipant raceParticipant = new RaceParticipant();
        raceParticipant.setRaceParticipantId(row.getUUID("raceParticipantId"));
        raceParticipant.setPetId(row.getUUID("petId"));
        raceParticipant.setRaceId(row.getUUID("raceId"));
        raceParticipant.setPetName(row.getString("petName"));
        raceParticipant.setPetColor(row.getString("petColor"));
        raceParticipant.setPetCategoryName(row.getString("petCategoryName"));
        raceParticipant.setPetCategoryId(row.getUUID("petCategoryId"));
        raceParticipant.setStartTime(row.getTimestamp("startTime"));
        raceParticipant.setFinishTime(row.getDecimal("finishTime"));
        raceParticipant.setFinishPosition(row.getInt("finishPosition"));
        raceParticipant.setFinished(row.getBool("finished"));
        return raceParticipant;
    }

    public Page<RaceParticipant> findAll(Pageable pageable) {
        List<RaceParticipant> raceParticipants = new ArrayList<>();

        fetchRowsWithPage(pageable).stream().map(
            row -> rowCall(row)
        ).forEach(raceParticipants::add);
        Page<RaceParticipant> page = new PageImpl<>(raceParticipants,pageable,raceParticipants.size());
        return page;
    }

    public List<RaceParticipant> findAll() {
        List<RaceParticipant> raceParticipants = new ArrayList<>();
        BoundStatement stmt =  findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> rowCall(row)
        ).forEach(raceParticipants::add);
        return raceParticipants;
    }

    public RaceParticipant findOne(UUID id) {
        return mapper.get(id);
    }

    public RaceParticipant save(RaceParticipant raceParticipant) {
        if (raceParticipant.getRaceParticipantId() == null) {
            raceParticipant.setRaceParticipantId(UUID.randomUUID());
        }
        mapper.save(raceParticipant);
        return raceParticipant;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt =  truncateStmt.bind();
        session.execute(stmt);
    }
}
