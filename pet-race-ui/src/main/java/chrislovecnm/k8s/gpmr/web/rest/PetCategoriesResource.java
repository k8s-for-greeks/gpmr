package chrislovecnm.k8s.gpmr.web.rest;

import chrislovecnm.k8s.gpmr.domain.PetCategories;
import chrislovecnm.k8s.gpmr.service.PetCategoriesService;
import chrislovecnm.k8s.gpmr.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing PetCategories.
 */
@RestController
@RequestMapping("/api")
public class PetCategoriesResource {

    private final Logger log = LoggerFactory.getLogger(PetCategoriesResource.class);

    @Inject
    private PetCategoriesService petCategoriesService;

    /**
     * POST  /pet-categories : Create a new petCategories.
     *
     * @param petCategories the petCategories to create
     * @return the ResponseEntity with status 201 (Created) and with body the new petCategories, or with status 400 (Bad Request) if the petCategories has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/pet-categories",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PetCategories> createPetCategories(@RequestBody PetCategories petCategories) throws URISyntaxException {
        log.debug("REST request to save PetCategories : {}", petCategories);
        if (petCategories.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("petCategories", "idexists", "A new petCategories cannot already have an ID")).body(null);
        }
        PetCategories result = petCategoriesService.save(petCategories);
        return ResponseEntity.created(new URI("/api/pet-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("petCategories", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pet-categories : Updates an existing petCategories.
     *
     * @param petCategories the petCategories to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated petCategories,
     * or with status 400 (Bad Request) if the petCategories is not valid,
     * or with status 500 (Internal Server Error) if the petCategories couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/pet-categories",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PetCategories> updatePetCategories(@RequestBody PetCategories petCategories) throws URISyntaxException {
        log.debug("REST request to update PetCategories : {}", petCategories);
        if (petCategories.getId() == null) {
            return createPetCategories(petCategories);
        }
        PetCategories result = petCategoriesService.save(petCategories);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("petCategories", petCategories.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pet-categories : get all the petCategories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of petCategories in body
     */
    @RequestMapping(value = "/pet-categories",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PetCategories> getAllPetCategories() {
        log.debug("REST request to get all PetCategories");
        return petCategoriesService.findAll();
    }

    /**
     * GET  /pet-categories/:id : get the "id" petCategories.
     *
     * @param id the id of the petCategories to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the petCategories, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/pet-categories/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PetCategories> getPetCategories(@PathVariable String id) {
        log.debug("REST request to get PetCategories : {}", id);
        PetCategories petCategories = petCategoriesService.findOne(id);
        return Optional.ofNullable(petCategories)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /pet-categories/:id : delete the "id" petCategories.
     *
     * @param id the id of the petCategories to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/pet-categories/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePetCategories(@PathVariable String id) {
        log.debug("REST request to delete PetCategories : {}", id);
        petCategoriesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("petCategories", id.toString())).build();
    }

}
