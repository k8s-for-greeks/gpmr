package chrislovecnm.k8s.gpmr.service;

import chrislovecnm.k8s.gpmr.domain.PetCategories;

import java.util.List;

/**
 * Service Interface for managing PetCategories.
 */
public interface PetCategoriesService {

    /**
     * Save a petCategories.
     * 
     * @param petCategories the entity to save
     * @return the persisted entity
     */
    PetCategories save(PetCategories petCategories);

    /**
     *  Get all the petCategories.
     *  
     *  @return the list of entities
     */
    List<PetCategories> findAll();

    /**
     *  Get the "id" petCategories.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    PetCategories findOne(String id);

    /**
     *  Delete the "id" petCategories.
     *  
     *  @param id the id of the entity
     */
    void delete(String id);
}
