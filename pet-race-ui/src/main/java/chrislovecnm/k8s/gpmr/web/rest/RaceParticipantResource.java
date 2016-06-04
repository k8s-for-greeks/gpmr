package chrislovecnm.k8s.gpmr.web.rest;

import com.codahale.metrics.annotation.Timed;
import chrislovecnm.k8s.gpmr.domain.RaceParticipant;
import chrislovecnm.k8s.gpmr.repository.RaceParticipantRepository;
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
 * REST controller for managing RaceParticipant.
 */
@RestController
@RequestMapping("/api")
public class RaceParticipantResource {

    private final Logger log = LoggerFactory.getLogger(RaceParticipantResource.class);

    @Inject
    private RaceParticipantRepository raceParticipantRepository;

    /**
     * POST  /race-participants : Create a new raceParticipant.
     *
     * @param raceParticipant the raceParticipant to create
     * @return the ResponseEntity with status 201 (Created) and with body the new raceParticipant, or with status 400 (Bad Request) if the raceParticipant has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/race-participants",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RaceParticipant> createRaceParticipant(@RequestBody RaceParticipant raceParticipant) throws URISyntaxException {
        log.debug("REST request to save RaceParticipant : {}", raceParticipant);
        if (raceParticipant.getRaceParticipantId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("raceParticipant", "idexists", "A new raceParticipant cannot already have an ID")).body(null);
        }
        RaceParticipant result = raceParticipantRepository.save(raceParticipant);
        return ResponseEntity.created(new URI("/api/race-participants/" + result.getRaceParticipantId()))
            .headers(HeaderUtil.createEntityCreationAlert("raceParticipant", result.getRaceParticipantId().toString()))
            .body(result);
    }

    /**
     * PUT  /race-participants : Updates an existing raceParticipant.
     *
     * @param raceParticipant the raceParticipant to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated raceParticipant,
     * or with status 400 (Bad Request) if the raceParticipant is not valid,
     * or with status 500 (Internal Server Error) if the raceParticipant couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/race-participants",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RaceParticipant> updateRaceParticipant(@RequestBody RaceParticipant raceParticipant) throws URISyntaxException {
        log.debug("REST request to update RaceParticipant : {}", raceParticipant);
        if (raceParticipant.getRaceParticipantId() == null) {
            return createRaceParticipant(raceParticipant);
        }
        RaceParticipant result = raceParticipantRepository.save(raceParticipant);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("raceParticipant", raceParticipant.getRaceParticipantId().toString()))
            .body(result);
    }

    /**
     * GET  /race-participants : get all the raceParticipants.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of raceParticipants in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/race-participants",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<RaceParticipant>> getAllRaceParticipants(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of RaceParticipants");
        Page<RaceParticipant> page = raceParticipantRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/race-participants");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /race-participants/:id : get the "id" raceParticipant.
     *
     * @param id the id of the raceParticipant to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the raceParticipant, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/race-participants/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RaceParticipant> getRaceParticipant(@PathVariable String id) {
        log.debug("REST request to get RaceParticipant : {}", id);
        RaceParticipant raceParticipant = raceParticipantRepository.findOne(UUID.fromString(id));
        return Optional.ofNullable(raceParticipant)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /race-participants/:id : delete the "id" raceParticipant.
     *
     * @param id the id of the raceParticipant to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/race-participants/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRaceParticipant(@PathVariable String id) {
        log.debug("REST request to delete RaceParticipant : {}", id);
        raceParticipantRepository.delete(UUID.fromString(id));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("raceParticipant", id.toString())).build();
    }

}
