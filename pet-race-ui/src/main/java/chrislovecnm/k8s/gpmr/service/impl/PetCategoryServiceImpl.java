package chrislovecnm.k8s.gpmr.service.impl;

import chrislovecnm.k8s.gpmr.domain.PetCategory;
import chrislovecnm.k8s.gpmr.repository.PetCategoryRepository;
import chrislovecnm.k8s.gpmr.service.PetCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

/**
 * Service Implementation for managing PetCategory.
 */
@Service
public class PetCategoryServiceImpl implements PetCategoryService {

    private final Logger log = LoggerFactory.getLogger(PetCategoryServiceImpl.class);

    @Inject
    private PetCategoryRepository petCategoryRepository;

    /**
     * Save a petCategory.
     *
     * @param petCategory the entity to save
     * @return the persisted entity
     */
    public PetCategory save(PetCategory petCategory) {
        log.debug("Request to save PetCategory : {}", petCategory);
        PetCategory result = petCategoryRepository.save(petCategory);
        return result;
    }

    /**
     * Get all the petCategories.
     *
     * @return the list of entities
     */
    public List<PetCategory> findAll() {
        log.debug("Request to get all PetCategories");
        List<PetCategory> result = petCategoryRepository.findAll();
        return result;
    }

    /**
     * Get one petCategory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public PetCategory findOne(String id) {
        log.debug("Request to get PetCategory : {}", id);
        PetCategory petCategory = petCategoryRepository.findOne(UUID.fromString(id));
        return petCategory;
    }

    /**
     * Delete the  petCategory by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete PetCategory : {}", id);
        petCategoryRepository.delete(UUID.fromString(id));
    }
}
