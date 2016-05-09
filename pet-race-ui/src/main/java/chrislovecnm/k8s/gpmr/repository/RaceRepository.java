package chrislovecnm.k8s.gpmr.repository;

import chrislovecnm.k8s.gpmr.domain.Race;

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
 * Cassandra repository for the Race entity.
 */
@Repository
public class RaceRepository {

    @Inject
    private Session session;

    private Mapper<Race> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement truncateStmt;

    @PostConstruct
    public void init() {
        mapper = new MappingManager(session).mapper(Race.class);
        findAllStmt = session.prepare("SELECT * FROM race");
        truncateStmt = session.prepare("TRUNCATE race");
    }

    public List<Race> findAll() {
        List<Race> races = new ArrayList<>();
        BoundStatement stmt =  findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                Race race = new Race();
                race.setId(row.getUUID("id"));
                race.setNumOfPets(row.getInt("numOfPets"));
                race.setLength(row.getInt("length"));
                race.setNumOfSamples(row.getInt("numOfSamples"));
                race.setWinnerId(row.getUUID("winnerId"));
                race.setWinnerName(row.getString("winnerName"));
                race.setWinnnerPetCategory(row.getString("winnnerPetCategory"));
                race.setStartTime(row.getDate("startTime"));
                race.setEndTime(row.getDate("endTime"));
                return race;
            }
        ).forEach(races::add);
        return races;
    }

    public Race findOne(UUID id) {
        return mapper.get(id);
    }

    public Race save(Race race) {
        if (race.getId() == null) {
            race.setId(UUID.randomUUID());
        }
        mapper.save(race);
        return race;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt =  truncateStmt.bind();
        session.execute(stmt);
    }
}
