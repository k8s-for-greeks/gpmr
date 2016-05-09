package chrislovecnm.k8s.gpmr.service.impl;

import chrislovecnm.k8s.gpmr.service.PetsService;
import chrislovecnm.k8s.gpmr.domain.Pets;
import chrislovecnm.k8s.gpmr.repository.PetsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

/**
 * Service Implementation for managing Pets.
 */
@Service
public class PetsServiceImpl implements PetsService{

    private final Logger log = LoggerFactory.getLogger(PetsServiceImpl.class);
    
    @Inject
    private PetsRepository petsRepository;
    
    /**
     * Save a pets.
     * 
     * @param pets the entity to save
     * @return the persisted entity
     */
    public Pets save(Pets pets) {
        log.debug("Request to save Pets : {}", pets);
        Pets result = petsRepository.save(pets);
        return result;
    }

    /**
     *  Get all the pets.
     *  
     *  @return the list of entities
     */
    public List<Pets> findAll() {
        log.debug("Request to get all Pets");
        List<Pets> result = petsRepository.findAll();
        return result;
    }

    /**
     *  Get one pets by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public Pets findOne(String id) {
        log.debug("Request to get Pets : {}", id);
        Pets pets = petsRepository.findOne(UUID.fromString(id));
        return pets;
    }

    /**
     *  Delete the  pets by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Pets : {}", id);
        petsRepository.delete(UUID.fromString(id));
    }
}
