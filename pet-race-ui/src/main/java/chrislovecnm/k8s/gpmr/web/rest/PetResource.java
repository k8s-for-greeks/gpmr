package chrislovecnm.k8s.gpmr.web.rest;

import com.codahale.metrics.annotation.Timed;
import chrislovecnm.k8s.gpmr.domain.Pet;
import chrislovecnm.k8s.gpmr.service.PetService;
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
 * REST controller for managing Pet.
 */
@RestController
@RequestMapping("/api")
public class PetResource {

    private final Logger log = LoggerFactory.getLogger(PetResource.class);

    @Inject
    private PetService petService;

    /**
     * POST  /pets : Create a new pet.
     *
     * @param pet the pet to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pet, or with status 400 (Bad Request) if the pet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/pets",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pet> createPet(@RequestBody Pet pet) throws URISyntaxException {
        log.debug("REST request to save Pet : {}", pet);
        if (pet.getPetId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("pet", "idexists", "A new pet cannot already have an ID")).body(null);
        }
        Pet result = petService.save(pet);
        return ResponseEntity.created(new URI("/api/pets/" + result.getPetId()))
            .headers(HeaderUtil.createEntityCreationAlert("pet", result.getPetId().toString()))
            .body(result);
    }

    /**
     * PUT  /pets : Updates an existing pet.
     *
     * @param pet the pet to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pet,
     * or with status 400 (Bad Request) if the pet is not valid,
     * or with status 500 (Internal Server Error) if the pet couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/pets",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pet> updatePet(@RequestBody Pet pet) throws URISyntaxException {
        log.debug("REST request to update Pet : {}", pet);
        if (pet.getPetId() == null) {
            return createPet(pet);
        }
        Pet result = petService.save(pet);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("pet", pet.getPetId().toString()))
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
    public List<Pet> getAllPets() {
        log.debug("REST request to get all Pets");
        return petService.findAll();
    }

    /**
     * GET  /pets/:id : get the "id" pet.
     *
     * @param id the id of the pet to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pet, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/pets/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pet> getPet(@PathVariable String id) {
        log.debug("REST request to get Pet : {}", id);
        Pet pet = petService.findOne(id);
        return Optional.ofNullable(pet)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /pets/:id : delete the "id" pet.
     *
     * @param id the id of the pet to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/pets/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePet(@PathVariable String id) {
        log.debug("REST request to delete Pet : {}", id);
        petService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("pet", id.toString())).build();
    }

}
