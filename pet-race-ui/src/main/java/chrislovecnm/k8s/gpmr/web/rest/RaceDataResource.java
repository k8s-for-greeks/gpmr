package chrislovecnm.k8s.gpmr.web.rest;

import com.codahale.metrics.annotation.Timed;
import chrislovecnm.k8s.gpmr.domain.RaceData;
import chrislovecnm.k8s.gpmr.repository.RaceDataRepository;
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
 * REST controller for managing RaceData.
 */
@RestController
@RequestMapping("/api")
public class RaceDataResource {

    private final Logger log = LoggerFactory.getLogger(RaceDataResource.class);
        
    @Inject
    private RaceDataRepository raceDataRepository;
    
    /**
     * POST  /race-data : Create a new raceData.
     *
     * @param raceData the raceData to create
     * @return the ResponseEntity with status 201 (Created) and with body the new raceData, or with status 400 (Bad Request) if the raceData has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/race-data",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RaceData> createRaceData(@RequestBody RaceData raceData) throws URISyntaxException {
        log.debug("REST request to save RaceData : {}", raceData);
        if (raceData.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("raceData", "idexists", "A new raceData cannot already have an ID")).body(null);
        }
        RaceData result = raceDataRepository.save(raceData);
        return ResponseEntity.created(new URI("/api/race-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("raceData", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /race-data : Updates an existing raceData.
     *
     * @param raceData the raceData to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated raceData,
     * or with status 400 (Bad Request) if the raceData is not valid,
     * or with status 500 (Internal Server Error) if the raceData couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/race-data",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RaceData> updateRaceData(@RequestBody RaceData raceData) throws URISyntaxException {
        log.debug("REST request to update RaceData : {}", raceData);
        if (raceData.getId() == null) {
            return createRaceData(raceData);
        }
        RaceData result = raceDataRepository.save(raceData);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("raceData", raceData.getId().toString()))
            .body(result);
    }

    /**
     * GET  /race-data : get all the raceData.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of raceData in body
     */
    @RequestMapping(value = "/race-data",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<RaceData> getAllRaceData() {
        log.debug("REST request to get all RaceData");
        List<RaceData> raceData = raceDataRepository.findAll();
        return raceData;
    }

    /**
     * GET  /race-data/:id : get the "id" raceData.
     *
     * @param id the id of the raceData to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the raceData, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/race-data/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RaceData> getRaceData(@PathVariable String id) {
        log.debug("REST request to get RaceData : {}", id);
        RaceData raceData = raceDataRepository.findOne(UUID.fromString(id));
        return Optional.ofNullable(raceData)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /race-data/:id : delete the "id" raceData.
     *
     * @param id the id of the raceData to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/race-data/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRaceData(@PathVariable String id) {
        log.debug("REST request to delete RaceData : {}", id);
        raceDataRepository.delete(UUID.fromString(id));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("raceData", id.toString())).build();
    }

}
