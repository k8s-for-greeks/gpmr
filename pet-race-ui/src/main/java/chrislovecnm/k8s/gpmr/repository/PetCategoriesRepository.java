package chrislovecnm.k8s.gpmr.repository;

import chrislovecnm.k8s.gpmr.domain.PetCategories;

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
 * Cassandra repository for the PetCategories entity.
 */
@Repository
public class PetCategoriesRepository {

    @Inject
    private Session session;

    private Mapper<PetCategories> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement truncateStmt;

    @PostConstruct
    public void init() {
        mapper = new MappingManager(session).mapper(PetCategories.class);
        findAllStmt = session.prepare("SELECT * FROM petCategories");
        truncateStmt = session.prepare("TRUNCATE petCategories");
    }

    public List<PetCategories> findAll() {
        List<PetCategories> petCategories = new ArrayList<>();
        BoundStatement stmt =  findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                PetCategories petCategories = new PetCategories();
                petCategories.setId(row.getUUID("id"));
                petCategories.setName(row.getString("name"));
                return petCategories;
            }
        ).forEach(petCategories::add);
        return petCategories;
    }

    public PetCategories findOne(UUID id) {
        return mapper.get(id);
    }

    public PetCategories save(PetCategories petCategories) {
        if (petCategories.getId() == null) {
            petCategories.setId(UUID.randomUUID());
        }
        mapper.save(petCategories);
        return petCategories;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt =  truncateStmt.bind();
        session.execute(stmt);
    }
}
