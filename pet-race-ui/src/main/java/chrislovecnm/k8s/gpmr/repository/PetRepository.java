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
public class PetRepository extends CassandraPaging {

    private Mapper<Pet> mapper;

    @PostConstruct
    public void init() {
        mapper = new MappingManager(session).mapper(Pet.class);
        createPaging("gpmr","pet");
    }

    public List<Pet> findAll() {
        List<Pet> pets = new ArrayList<>();
        BoundStatement stmt =  findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                Pet pet = new Pet();
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

    public Pet findOne(UUID id) {
        return mapper.get(id);
    }

    public Pet save(Pet pet) {
        if (pet.getPetId() == null) {
            pet.setPetId(UUID.randomUUID());
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
