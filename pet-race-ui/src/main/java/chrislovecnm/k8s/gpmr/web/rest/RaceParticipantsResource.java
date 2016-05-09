package chrislovecnm.k8s.gpmr.web.rest;

import com.codahale.metrics.annotation.Timed;
import chrislovecnm.k8s.gpmr.domain.RaceParticipants;
import chrislovecnm.k8s.gpmr.repository.RaceParticipantsRepository;
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
 * REST controller for managing RaceParticipants.
 */
@RestController
@RequestMapping("/api")
public class RaceParticipantsResource {

    private final Logger log = LoggerFactory.getLogger(RaceParticipantsResource.class);
        
    @Inject
    private RaceParticipantsRepository raceParticipantsRepository;
    
    /**
     * POST  /race-participants : Create a new raceParticipants.
     *
     * @param raceParticipants the raceParticipants to create
     * @return the ResponseEntity with status 201 (Created) and with body the new raceParticipants, or with status 400 (Bad Request) if the raceParticipants has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/race-participants",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RaceParticipants> createRaceParticipants(@RequestBody RaceParticipants raceParticipants) throws URISyntaxException {
        log.debug("REST request to save RaceParticipants : {}", raceParticipants);
        if (raceParticipants.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("raceParticipants", "idexists", "A new raceParticipants cannot already have an ID")).body(null);
        }
        RaceParticipants result = raceParticipantsRepository.save(raceParticipants);
        return ResponseEntity.created(new URI("/api/race-participants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("raceParticipants", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /race-participants : Updates an existing raceParticipants.
     *
     * @param raceParticipants the raceParticipants to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated raceParticipants,
     * or with status 400 (Bad Request) if the raceParticipants is not valid,
     * or with status 500 (Internal Server Error) if the raceParticipants couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/race-participants",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RaceParticipants> updateRaceParticipants(@RequestBody RaceParticipants raceParticipants) throws URISyntaxException {
        log.debug("REST request to update RaceParticipants : {}", raceParticipants);
        if (raceParticipants.getId() == null) {
            return createRaceParticipants(raceParticipants);
        }
        RaceParticipants result = raceParticipantsRepository.save(raceParticipants);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("raceParticipants", raceParticipants.getId().toString()))
            .body(result);
    }

    /**
     * GET  /race-participants : get all the raceParticipants.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of raceParticipants in body
     */
    @RequestMapping(value = "/race-participants",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<RaceParticipants> getAllRaceParticipants() {
        log.debug("REST request to get all RaceParticipants");
        List<RaceParticipants> raceParticipants = raceParticipantsRepository.findAll();
        return raceParticipants;
    }

    /**
     * GET  /race-participants/:id : get the "id" raceParticipants.
     *
     * @param id the id of the raceParticipants to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the raceParticipants, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/race-participants/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RaceParticipants> getRaceParticipants(@PathVariable String id) {
        log.debug("REST request to get RaceParticipants : {}", id);
        RaceParticipants raceParticipants = raceParticipantsRepository.findOne(UUID.fromString(id));
        return Optional.ofNullable(raceParticipants)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /race-participants/:id : delete the "id" raceParticipants.
     *
     * @param id the id of the raceParticipants to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/race-participants/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRaceParticipants(@PathVariable String id) {
        log.debug("REST request to delete RaceParticipants : {}", id);
        raceParticipantsRepository.delete(UUID.fromString(id));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("raceParticipants", id.toString())).build();
    }

}
