package chrislovecnm.k8s.gpmr.web.rest;

import com.codahale.metrics.annotation.Timed;
import chrislovecnm.k8s.gpmr.domain.RaceResults;
import chrislovecnm.k8s.gpmr.repository.RaceResultsRepository;
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
 * REST controller for managing RaceResults.
 */
@RestController
@RequestMapping("/api")
public class RaceResultsResource {

    private final Logger log = LoggerFactory.getLogger(RaceResultsResource.class);
        
    @Inject
    private RaceResultsRepository raceResultsRepository;
    
    /**
     * POST  /race-results : Create a new raceResults.
     *
     * @param raceResults the raceResults to create
     * @return the ResponseEntity with status 201 (Created) and with body the new raceResults, or with status 400 (Bad Request) if the raceResults has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/race-results",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RaceResults> createRaceResults(@RequestBody RaceResults raceResults) throws URISyntaxException {
        log.debug("REST request to save RaceResults : {}", raceResults);
        if (raceResults.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("raceResults", "idexists", "A new raceResults cannot already have an ID")).body(null);
        }
        RaceResults result = raceResultsRepository.save(raceResults);
        return ResponseEntity.created(new URI("/api/race-results/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("raceResults", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /race-results : Updates an existing raceResults.
     *
     * @param raceResults the raceResults to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated raceResults,
     * or with status 400 (Bad Request) if the raceResults is not valid,
     * or with status 500 (Internal Server Error) if the raceResults couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/race-results",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RaceResults> updateRaceResults(@RequestBody RaceResults raceResults) throws URISyntaxException {
        log.debug("REST request to update RaceResults : {}", raceResults);
        if (raceResults.getId() == null) {
            return createRaceResults(raceResults);
        }
        RaceResults result = raceResultsRepository.save(raceResults);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("raceResults", raceResults.getId().toString()))
            .body(result);
    }

    /**
     * GET  /race-results : get all the raceResults.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of raceResults in body
     */
    @RequestMapping(value = "/race-results",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<RaceResults> getAllRaceResults() {
        log.debug("REST request to get all RaceResults");
        List<RaceResults> raceResults = raceResultsRepository.findAll();
        return raceResults;
    }

    /**
     * GET  /race-results/:id : get the "id" raceResults.
     *
     * @param id the id of the raceResults to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the raceResults, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/race-results/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RaceResults> getRaceResults(@PathVariable String id) {
        log.debug("REST request to get RaceResults : {}", id);
        RaceResults raceResults = raceResultsRepository.findOne(UUID.fromString(id));
        return Optional.ofNullable(raceResults)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /race-results/:id : delete the "id" raceResults.
     *
     * @param id the id of the raceResults to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/race-results/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRaceResults(@PathVariable String id) {
        log.debug("REST request to delete RaceResults : {}", id);
        raceResultsRepository.delete(UUID.fromString(id));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("raceResults", id.toString())).build();
    }

}
