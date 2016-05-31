package chrislovecnm.k8s.gpmr.web.rest;

import com.codahale.metrics.annotation.Timed;
import chrislovecnm.k8s.gpmr.domain.PetCategory;
import chrislovecnm.k8s.gpmr.service.PetCategoryService;
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
 * REST controller for managing PetCategory.
 */
@RestController
@RequestMapping("/api")
public class PetCategoryResource {

    private final Logger log = LoggerFactory.getLogger(PetCategoryResource.class);
        
    @Inject
    private PetCategoryService petCategoryService;
    
    /**
     * POST  /pet-categories : Create a new petCategory.
     *
     * @param petCategory the petCategory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new petCategory, or with status 400 (Bad Request) if the petCategory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/pet-categories",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PetCategory> createPetCategory(@RequestBody PetCategory petCategory) throws URISyntaxException {
        log.debug("REST request to save PetCategory : {}", petCategory);
        if (petCategory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("petCategory", "idexists", "A new petCategory cannot already have an ID")).body(null);
        }
        PetCategory result = petCategoryService.save(petCategory);
        return ResponseEntity.created(new URI("/api/pet-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("petCategory", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pet-categories : Updates an existing petCategory.
     *
     * @param petCategory the petCategory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated petCategory,
     * or with status 400 (Bad Request) if the petCategory is not valid,
     * or with status 500 (Internal Server Error) if the petCategory couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/pet-categories",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PetCategory> updatePetCategory(@RequestBody PetCategory petCategory) throws URISyntaxException {
        log.debug("REST request to update PetCategory : {}", petCategory);
        if (petCategory.getId() == null) {
            return createPetCategory(petCategory);
        }
        PetCategory result = petCategoryService.save(petCategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("petCategory", petCategory.getId().toString()))
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
    public List<PetCategory> getAllPetCategories() {
        log.debug("REST request to get all PetCategories");
        return petCategoryService.findAll();
    }

    /**
     * GET  /pet-categories/:id : get the "id" petCategory.
     *
     * @param id the id of the petCategory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the petCategory, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/pet-categories/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PetCategory> getPetCategory(@PathVariable String id) {
        log.debug("REST request to get PetCategory : {}", id);
        PetCategory petCategory = petCategoryService.findOne(id);
        return Optional.ofNullable(petCategory)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /pet-categories/:id : delete the "id" petCategory.
     *
     * @param id the id of the petCategory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/pet-categories/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePetCategory(@PathVariable String id) {
        log.debug("REST request to delete PetCategory : {}", id);
        petCategoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("petCategory", id.toString())).build();
    }

}
