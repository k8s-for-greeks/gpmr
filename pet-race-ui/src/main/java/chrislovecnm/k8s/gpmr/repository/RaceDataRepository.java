package chrislovecnm.k8s.gpmr.repository;

import chrislovecnm.k8s.gpmr.domain.RaceData;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
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
public class RaceDataRepository {

    @Inject
    private Session session;

    private Mapper<RaceData> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement truncateStmt;

    @PostConstruct
    public void init() {
        mapper = new MappingManager(session).mapper(RaceData.class);
        findAllStmt = session.prepare("SELECT * FROM raceData");
        truncateStmt = session.prepare("TRUNCATE raceData");
    }

    public List<RaceData> findAll() {
        List<RaceData> raceData = new ArrayList<>();
        BoundStatement stmt = findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                RaceData rd = new RaceData();
                rd.setId(row.getUUID("id"));
                rd.setPetId(row.getUUID("petId"));
                rd.setPetName(row.getString("petName"));
                rd.setPetCategory(row.getString("petCategory"));
                rd.setPetCategoryId(row.getUUID("petCategoryId"));
                rd.setRunnerPostion(row.getInt("runnerPostion"));
                rd.setRunnerSashColor(row.getString("runnerSashColor"));
                return rd;
            }
        ).forEach(raceData::add);
        return raceData;
    }

    public RaceData findOne(UUID id) {
        return mapper.get(id);
    }

    public RaceData save(RaceData raceData) {
        if (raceData.getId() == null) {
            raceData.setId(UUID.randomUUID());
        }
        mapper.save(raceData);
        return raceData;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt = truncateStmt.bind();
        session.execute(stmt);
    }
}
