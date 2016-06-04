package chrislovecnm.k8s.gpmr.repository;

import chrislovecnm.k8s.gpmr.domain.Race;

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
 * Cassandra repository for the Race entity.
 */
@Repository
public class RaceRepository extends CassandraPaging {

    private Mapper<Race> mapper;

    @PostConstruct
    public void init() {
        mapper = new MappingManager(session).mapper(Race.class);
        createPaging(mapper,"gpmr","race");
    }

    public Page<Race> findAll(Pageable pageable) {
        List<Race> races = new ArrayList<>();

        fetchRowsWithPage(pageable.getOffset(), pageable.getPageSize()).stream().map(
            row -> rowCall(row)
        ).forEach(races::add);
        Page<Race> page = new PageImpl<>(races,pageable,races.size());
        return page;
    }

    private Race rowCall(Row row) {
        Race race = new Race();
        race.setRaceId(row.getUUID("raceId"));
        race.setPetCategoryId(row.getUUID("petCategoryId"));
        race.setPetCategoryName(row.getString("petCategoryName"));
        race.setNumOfPets(row.getInt("numOfPets"));
        race.setLength(row.getInt("length"));
        race.setDescription(row.getString("description"));
        race.setWinnerId(row.getUUID("winnerId"));
        race.setStartTime(row.getTimestamp("startTime"));
        race.setBaseSpeed(row.getFloat("baseSpeed"));
        race.setRacersIds(row.getList("racersIds", UUID.class));
        return race;
    }

    public List<Race> findAll() {
        ResultSet resultSet = session.execute(findAllStmtPaging);
        return mapper.map(resultSet).all();
    }

    public Race findOne(UUID id) {
        return mapper.get(id);
    }

    public Race save(Race race) {
        if (race.getRaceId() == null) {
            race.setRaceId(UUID.randomUUID());
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
