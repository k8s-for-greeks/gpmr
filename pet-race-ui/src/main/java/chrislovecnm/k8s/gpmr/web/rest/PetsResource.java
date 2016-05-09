package chrislovecnm.k8s.gpmr.web.rest;

import com.codahale.metrics.annotation.Timed;
import chrislovecnm.k8s.gpmr.domain.Pets;
import chrislovecnm.k8s.gpmr.service.PetsService;
import chrislovecnm.k8s.gpmr.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing Pets.
 */
@RestController
@RequestMapping("/api")
public class PetsResource {

    private final Logger log = LoggerFactory.getLogger(PetsResource.class);
        
    @Inject
    private PetsService petsService;
    
    /**
     * POST  /pets : Create a new pets.
     *
     * @param pets the pets to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pets, or with status 400 (Bad Request) if the pets has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/pets",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pets> createPets(@RequestBody Pets pets) throws URISyntaxException {
        log.debug("REST request to save Pets : {}", pets);
        if (pets.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("pets", "idexists", "A new pets cannot already have an ID")).body(null);
        }
        Pets result = petsService.save(pets);
        return ResponseEntity.created(new URI("/api/pets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("pets", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pets : Updates an existing pets.
     *
     * @param pets the pets to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pets,
     * or with status 400 (Bad Request) if the pets is not valid,
     * or with status 500 (Internal Server Error) if the pets couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/pets",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pets> updatePets(@RequestBody Pets pets) throws URISyntaxException {
        log.debug("REST request to update Pets : {}", pets);
        if (pets.getId() == null) {
            return createPets(pets);
        }
        Pets result = petsService.save(pets);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("pets", pets.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pets : get all the pets.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of pets in body
     */
    @RequestMapping(value = "/pets",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Pets> getAllPets() {
        log.debug("REST request to get all Pets");
        return petsService.findAll();
    }

    /**
     * GET  /pets/:id : get the "id" pets.
     *
     * @param id the id of the pets to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pets, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/pets/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pets> getPets(@PathVariable String id) {
        log.debug("REST request to get Pets : {}", id);
        Pets pets = petsService.findOne(id);
        return Optional.ofNullable(pets)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /pets/:id : delete the "id" pets.
     *
     * @param id the id of the pets to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/pets/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePets(@PathVariable String id) {
        log.debug("REST request to delete Pets : {}", id);
        petsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("pets", id.toString())).build();
    }

}
