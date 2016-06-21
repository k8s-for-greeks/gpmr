package chrislovecnm.k8s.gpmr.repository;

import chrislovecnm.k8s.gpmr.domain.PetCategory;

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
 * Cassandra repository for the PetCategory entity.
 */
@Repository
public class PetCategoryRepository extends CassandraPaging {

    private Mapper<PetCategory> mapper;


    @PostConstruct
    public void init() {
        mapper = new MappingManager(session).mapper(PetCategory.class);
        createPaging("gpmr","pet_category");
    }

    public List<PetCategory> findAll() {
        List<PetCategory> petCategories = new ArrayList<>();
        BoundStatement stmt =  findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                PetCategory petCategory = new PetCategory();
                petCategory.setPetCategoryId(row.getUUID("petCategoryId"));
                petCategory.setName(row.getString("name"));
                petCategory.setSpeed(row.getFloat("speed"));
                return petCategory;
            }
        ).forEach(petCategories::add);
        return petCategories;
    }

    public PetCategory findOne(UUID id) {
        return mapper.get(id);
    }

    public PetCategory save(PetCategory petCategory) {
        if (petCategory.getPetCategoryId() == null) {
            petCategory.setPetCategoryId(UUID.randomUUID());
        }
        mapper.save(petCategory);
        return petCategory;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt =  truncateStmt.bind();
        session.execute(stmt);
    }
}
