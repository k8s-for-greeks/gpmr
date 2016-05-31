package chrislovecnm.k8s.gpmr.repository;

import chrislovecnm.k8s.gpmr.domain.Pet;

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
 * Cassandra repository for the Pet entity.
 */
@Repository
public class PetRepository {

    @Inject
    private Session session;

    private Mapper<Pet> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement truncateStmt;

    @PostConstruct
    public void init() {
        mapper = new MappingManager(session).mapper(Pet.class);
        findAllStmt = session.prepare("SELECT * FROM pet");
        truncateStmt = session.prepare("TRUNCATE pet");
    }

    public List<Pet> findAll() {
        List<Pet> pets = new ArrayList<>();
        BoundStatement stmt =  findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                Pet pet = new Pet();
                pet.setId(row.getUUID("id"));
                pet.setName(row.getString("name"));
                pet.setPetCategory(row.getString("petCategory"));
                pet.setPetCategoryId(row.getUUID("petCategoryId"));
                pet.setPetSpeed(row.getDecimal("petSpeed"));
                return pet;
            }
        ).forEach(pets::add);
        return pets;
    }

    public Pet findOne(UUID id) {
        return mapper.get(id);
    }

    public Pet save(Pet pet) {
        if (pet.getId() == null) {
            pet.setId(UUID.randomUUID());
        }
        mapper.save(pet);
        return pet;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt =  truncateStmt.bind();
        session.execute(stmt);
    }
}
