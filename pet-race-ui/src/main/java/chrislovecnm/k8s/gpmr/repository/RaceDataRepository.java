package chrislovecnm.k8s.gpmr.repository;

import chrislovecnm.k8s.gpmr.domain.RaceData;

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
        BoundStatement stmt =  findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                RaceData raceData = new RaceData();
                raceData.setId(row.getUUID("id"));
                raceData.setPetId(row.getUUID("petId"));
                raceData.setPetName(row.getString("petName"));
                raceData.setPetCategory(row.getString("petCategory"));
                raceData.setPetCategoryId(row.getUUID("petCategoryId"));
                raceData.setRunnerPostion(row.getInt("runnerPostion"));
                raceData.setRunnerSashColor(row.getString("runnerSashColor"));
                return raceData;
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
        BoundStatement stmt =  truncateStmt.bind();
        session.execute(stmt);
    }
}
