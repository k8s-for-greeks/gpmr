package chrislovecnm.k8s.gpmr.repository;

import chrislovecnm.k8s.gpmr.domain.RaceParticipants;

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
 * Cassandra repository for the RaceParticipants entity.
 */
@Repository
public class RaceParticipantsRepository {

    @Inject
    private Session session;

    private Mapper<RaceParticipants> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement truncateStmt;

    @PostConstruct
    public void init() {
        mapper = new MappingManager(session).mapper(RaceParticipants.class);
        findAllStmt = session.prepare("SELECT * FROM raceParticipants");
        truncateStmt = session.prepare("TRUNCATE raceParticipants");
    }

    public List<RaceParticipants> findAll() {
        List<RaceParticipants> raceParticipants = new ArrayList<>();
        BoundStatement stmt =  findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                RaceParticipants raceParticipants = new RaceParticipants();
                raceParticipants.setId(row.getUUID("id"));
                raceParticipants.setPetId(row.getUUID("petId"));
                raceParticipants.setPetName(row.getString("petName"));
                raceParticipants.setPetType(row.getString("petType"));
                raceParticipants.setPetColor(row.getString("petColor"));
                raceParticipants.setPetCategory(row.getString("petCategory"));
                raceParticipants.setPetCategoryId(row.getString("petCategoryId"));
                raceParticipants.setRaceId(row.getUUID("raceId"));
                return raceParticipants;
            }
        ).forEach(raceParticipants::add);
        return raceParticipants;
    }

    public RaceParticipants findOne(UUID id) {
        return mapper.get(id);
    }

    public RaceParticipants save(RaceParticipants raceParticipants) {
        if (raceParticipants.getId() == null) {
            raceParticipants.setId(UUID.randomUUID());
        }
        mapper.save(raceParticipants);
        return raceParticipants;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt =  truncateStmt.bind();
        session.execute(stmt);
    }
}
