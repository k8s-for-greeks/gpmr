package chrislovecnm.k8s.gpmr.repository;

import chrislovecnm.k8s.gpmr.domain.RaceParticipant;

import com.datastax.driver.core.*;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Cassandra repository for the RaceParticipant entity.
 */
@Repository
public class RaceParticipantRepository {

    @Inject
    private Session session;

    private Mapper<RaceParticipant> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement truncateStmt;

    @PostConstruct
    public void init() {
        mapper = new MappingManager(session).mapper(RaceParticipant.class);
        findAllStmt = session.prepare("SELECT * FROM raceParticipant");
        truncateStmt = session.prepare("TRUNCATE raceParticipant");
    }

    public List<RaceParticipant> findAll() {
        List<RaceParticipant> raceParticipants = new ArrayList<>();
        BoundStatement stmt =  findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                RaceParticipant raceParticipant = new RaceParticipant();
                raceParticipant.setId(row.getUUID("id"));
                raceParticipant.setPetId(row.getUUID("petId"));
                raceParticipant.setPetName(row.getString("petName"));
                raceParticipant.setPetType(row.getString("petType"));
                raceParticipant.setPetColor(row.getString("petColor"));
                raceParticipant.setPetCategory(row.getString("petCategory"));
                raceParticipant.setPetCategoryId(row.getString("petCategoryId"));
                raceParticipant.setRaceId(row.getUUID("raceId"));
                return raceParticipant;
            }
        ).forEach(raceParticipants::add);
        return raceParticipants;
    }

    public RaceParticipant findOne(UUID id) {
        return mapper.get(id);
    }

    public RaceParticipant save(RaceParticipant raceParticipant) {
        if (raceParticipant.getId() == null) {
            raceParticipant.setId(UUID.randomUUID());
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
