package chrislovecnm.k8s.gpmr.service.impl;

import chrislovecnm.k8s.gpmr.service.PetCategoriesService;
import chrislovecnm.k8s.gpmr.domain.PetCategories;
import chrislovecnm.k8s.gpmr.repository.PetCategoriesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

/**
 * Service Implementation for managing PetCategories.
 */
@Service
public class PetCategoriesServiceImpl implements PetCategoriesService{

    private final Logger log = LoggerFactory.getLogger(PetCategoriesServiceImpl.class);
    
    @Inject
    private PetCategoriesRepository petCategoriesRepository;
    
    /**
     * Save a petCategories.
     * 
     * @param petCategories the entity to save
     * @return the persisted entity
     */
    public PetCategories save(PetCategories petCategories) {
        log.debug("Request to save PetCategories : {}", petCategories);
        PetCategories result = petCategoriesRepository.save(petCategories);
        return result;
    }

    /**
     *  Get all the petCategories.
     *  
     *  @return the list of entities
     */
    public List<PetCategories> findAll() {
        log.debug("Request to get all PetCategories");
        List<PetCategories> result = petCategoriesRepository.findAll();
        return result;
    }

    /**
     *  Get one petCategories by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public PetCategories findOne(String id) {
        log.debug("Request to get PetCategories : {}", id);
        PetCategories petCategories = petCategoriesRepository.findOne(UUID.fromString(id));
        return petCategories;
    }

    /**
     *  Delete the  petCategories by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete PetCategories : {}", id);
        petCategoriesRepository.delete(UUID.fromString(id));
    }
}
