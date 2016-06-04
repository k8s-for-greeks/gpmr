package chrislovecnm.k8s.gpmr.web.rest;

import com.codahale.metrics.annotation.Timed;
import chrislovecnm.k8s.gpmr.domain.RaceResult;
import chrislovecnm.k8s.gpmr.repository.RaceResultRepository;
import chrislovecnm.k8s.gpmr.web.rest.util.HeaderUtil;
import chrislovecnm.k8s.gpmr.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
 * REST controller for managing RaceResult.
 */
@RestController
@RequestMapping("/api")
public class RaceResultResource {

    private final Logger log = LoggerFactory.getLogger(RaceResultResource.class);

    @Inject
    private RaceResultRepository raceResultRepository;

    /**
     * POST  /race-results : Create a new raceResult.
     *
     * @param raceResult the raceResult to create
     * @return the ResponseEntity with status 201 (Created) and with body the new raceResult, or with status 400 (Bad Request) if the raceResult has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/race-results",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RaceResult> createRaceResult(@RequestBody RaceResult raceResult) throws URISyntaxException {
        log.debug("REST request to save RaceResult : {}", raceResult);
        if (raceResult.getRaceResultId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("raceResult", "idexists", "A new raceResult cannot already have an ID")).body(null);
        }
        RaceResult result = raceResultRepository.save(raceResult);
        return ResponseEntity.created(new URI("/api/race-results/" + result.getRaceResultId()))
            .headers(HeaderUtil.createEntityCreationAlert("raceResult", result.getRaceResultId().toString()))
            .body(result);
    }

    /**
     * PUT  /race-results : Updates an existing raceResult.
     *
     * @param raceResult the raceResult to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated raceResult,
     * or with status 400 (Bad Request) if the raceResult is not valid,
     * or with status 500 (Internal Server Error) if the raceResult couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/race-results",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RaceResult> updateRaceResult(@RequestBody RaceResult raceResult) throws URISyntaxException {
        log.debug("REST request to update RaceResult : {}", raceResult);
        if (raceResult.getRaceResultId() == null) {
            return createRaceResult(raceResult);
        }
        RaceResult result = raceResultRepository.save(raceResult);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("raceResult", raceResult.getRaceResultId().toString()))
            .body(result);
    }

    /**
     * GET  /race-results : get all the raceResults.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of raceResults in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/race-results",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<RaceResult>> getAllRaceResults(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of RaceResults");
        Page<RaceResult> page = raceResultRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/race-results");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /race-results/:id : get the "id" raceResult.
     *
     * @param id the id of the raceResult to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the raceResult, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/race-results/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RaceResult> getRaceResult(@PathVariable String id) {
        log.debug("REST request to get RaceResult : {}", id);
        RaceResult raceResult = raceResultRepository.findOne(UUID.fromString(id));
        return Optional.ofNullable(raceResult)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /race-results/:id : delete the "id" raceResult.
     *
     * @param id the id of the raceResult to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/race-results/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRaceResult(@PathVariable String id) {
        log.debug("REST request to delete RaceResult : {}", id);
        raceResultRepository.delete(UUID.fromString(id));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("raceResult", id.toString())).build();
    }

}
