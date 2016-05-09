package chrislovecnm.k8s.gpmr.repository;

import chrislovecnm.k8s.gpmr.domain.RaceResults;

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
 * Cassandra repository for the RaceResults entity.
 */
@Repository
public class RaceResultsRepository {

    @Inject
    private Session session;

    private Mapper<RaceResults> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement truncateStmt;

    @PostConstruct
    public void init() {
        mapper = new MappingManager(session).mapper(RaceResults.class);
        findAllStmt = session.prepare("SELECT * FROM raceResults");
        truncateStmt = session.prepare("TRUNCATE raceResults");
    }

    public List<RaceResults> findAll() {
        List<RaceResults> raceResults = new ArrayList<>();
        BoundStatement stmt =  findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                RaceResults raceResults = new RaceResults();
                raceResults.setId(row.getUUID("id"));
                raceResults.setPetId(row.getUUID("petId"));
                raceResults.setPetName(row.getString("petName"));
                raceResults.setPetCategory(row.getString("petCategory"));
                raceResults.setPetCategoryId(row.getUUID("petCategoryId"));
                raceResults.setPlace(row.getInt("place"));
                raceResults.setTime(row.getDate("time"));
                return raceResults;
            }
        ).forEach(raceResults::add);
        return raceResults;
    }

    public RaceResults findOne(UUID id) {
        return mapper.get(id);
    }

    public RaceResults save(RaceResults raceResults) {
        if (raceResults.getId() == null) {
            raceResults.setId(UUID.randomUUID());
        }
        mapper.save(raceResults);
        return raceResults;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt =  truncateStmt.bind();
        session.execute(stmt);
    }
}
