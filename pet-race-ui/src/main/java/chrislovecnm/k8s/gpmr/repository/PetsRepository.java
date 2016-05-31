package chrislovecnm.k8s.gpmr.repository;

import chrislovecnm.k8s.gpmr.domain.Pets;
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
 * Cassandra repository for the Pets entity.
 */
@Repository
public class PetsRepository {

    @Inject
    private Session session;

    private Mapper<Pets> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement truncateStmt;

    @PostConstruct
    public void init() {
        mapper = new MappingManager(session).mapper(Pets.class);
        findAllStmt = session.prepare("SELECT * FROM pets");
        truncateStmt = session.prepare("TRUNCATE pets");
    }

    public List<Pets> findAll() {
        List<Pets> pets = new ArrayList<>();
        BoundStatement stmt = findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                Pets pet = new Pets();
                pet.setId(row.getUUID("id"));
                pet.setPetId(row.getUUID("petId"));
                pet.setName(row.getString("name"));
                pet.setDescription(row.getString("description"));
                pet.setPetCategoryName(row.getString("petCategoryName"));
                pet.setPetCategoryId(row.getUUID("petCategoryId"));
                pet.setPetSpeed(row.getFloat("petSpeed"));
                return pet;
            }
        ).forEach(pets::add);
        return pets;
    }

    public Pets findOne(UUID id) {
        return mapper.get(id);
    }

    public Pets save(Pets pets) {
        if (pets.getId() == null) {
            pets.setId(UUID.randomUUID());
        }
        mapper.save(pets);
        return pets;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt = truncateStmt.bind();
        session.execute(stmt);
    }
}
