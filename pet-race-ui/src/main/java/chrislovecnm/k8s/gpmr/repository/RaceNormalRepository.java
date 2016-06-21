package chrislovecnm.k8s.gpmr.repository;

import chrislovecnm.k8s.gpmr.domain.RaceNormal;

import com.datastax.driver.core.*;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Cassandra repository for the RaceNormal entity.
 */
@Repository
public class RaceNormalRepository extends CassandraPaging {

    private Mapper<RaceNormal> mapper;

    @PostConstruct
    public void init() {
        mapper = new MappingManager(session).mapper(RaceNormal.class);
        createPaging("gpmr","race_normal");
    }

    public Page<RaceNormal> findAll(Pageable pageable) {
        List<RaceNormal> raceNormals = new ArrayList<>();
        fetchRowsWithPage(pageable).stream().map(
            row -> rowCall(row)
        ).forEach(raceNormals::add);
        Page<RaceNormal> page = new PageImpl<>(raceNormals,pageable,raceNormals.size());
        return page;
    }

    private RaceNormal rowCall(Row row) {
        RaceNormal raceNormal = new RaceNormal();
        raceNormal.setRaceNormalId(row.getUUID("raceNormalId"));
        raceNormal.setRaceId(row.getUUID("raceId"));
        raceNormal.setPetCategoryId(row.getUUID("petCategoryId"));
        raceNormal.setPetCategoryName(row.getString("petCategoryName"));
        raceNormal.setCurrentTime(row.getTimestamp("currentTime"));
        raceNormal.setNormalLoc(row.getFloat("normalLoc"));
        raceNormal.setNormalScale(row.getFloat("normalScale"));
        raceNormal.setNormalSize(row.getInt("normalSize"));
        raceNormal.setNormals(row.getList("normals", BigDecimal.class));
        return raceNormal;
    }

    public List<RaceNormal> findAll() {
        List<RaceNormal> raceNormals = new ArrayList<>();
        BoundStatement stmt =  findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> rowCall(row)
        ).forEach(raceNormals::add);
        return raceNormals;
    }

    public RaceNormal findOne(UUID id) {
        return mapper.get(id);
    }

    public RaceNormal save(RaceNormal raceNormal) {
        if (raceNormal.getRaceNormalId() == null) {
            raceNormal.setRaceNormalId(UUID.randomUUID());
        }
        mapper.save(raceNormal);
        return raceNormal;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt =  truncateStmt.bind();
        session.execute(stmt);
    }
}
