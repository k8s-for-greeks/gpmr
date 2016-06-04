package chrislovecnm.k8s.gpmr.service;

import chrislovecnm.k8s.gpmr.domain.PetCategory;

import java.util.List;

/**
 * Service Interface for managing PetCategory.
 */
public interface PetCategoryService {

    /**
     * Save a petCategory.
     * 
     * @param petCategory the entity to save
     * @return the persisted entity
     */
    PetCategory save(PetCategory petCategory);

    /**
     *  Get all the petCategories.
     *  
     *  @return the list of entities
     */
    List<PetCategory> findAll();

    /**
     *  Get the "id" petCategory.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    PetCategory findOne(String id);

    /**
     *  Delete the "id" petCategory.
     *  
     *  @param id the id of the entity
     */
    void delete(String id);
}
