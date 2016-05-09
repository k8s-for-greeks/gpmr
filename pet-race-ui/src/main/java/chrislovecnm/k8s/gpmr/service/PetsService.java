package chrislovecnm.k8s.gpmr.service;

import chrislovecnm.k8s.gpmr.domain.Pets;

import java.util.List;

/**
 * Service Interface for managing Pets.
 */
public interface PetsService {

    /**
     * Save a pets.
     * 
     * @param pets the entity to save
     * @return the persisted entity
     */
    Pets save(Pets pets);

    /**
     *  Get all the pets.
     *  
     *  @return the list of entities
     */
    List<Pets> findAll();

    /**
     *  Get the "id" pets.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Pets findOne(String id);

    /**
     *  Delete the "id" pets.
     *  
     *  @param id the id of the entity
     */
    void delete(String id);
}
