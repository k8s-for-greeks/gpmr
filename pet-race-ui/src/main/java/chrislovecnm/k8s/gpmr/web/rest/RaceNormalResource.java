package chrislovecnm.k8s.gpmr.web.rest;

import com.codahale.metrics.annotation.Timed;
import chrislovecnm.k8s.gpmr.domain.RaceNormal;
import chrislovecnm.k8s.gpmr.repository.RaceNormalRepository;
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
 * REST controller for managing RaceNormal.
 */
@RestController
@RequestMapping("/api")
public class RaceNormalResource {

    private final Logger log = LoggerFactory.getLogger(RaceNormalResource.class);

    @Inject
    private RaceNormalRepository raceNormalRepository;

    /**
     * POST  /race-normals : Create a new raceNormal.
     *
     * @param raceNormal the raceNormal to create
     * @return the ResponseEntity with status 201 (Created) and with body the new raceNormal, or with status 400 (Bad Request) if the raceNormal has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/race-normals",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RaceNormal> createRaceNormal(@RequestBody RaceNormal raceNormal) throws URISyntaxException {
        log.debug("REST request to save RaceNormal : {}", raceNormal);
        if (raceNormal.getRaceNormalId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("raceNormal", "idexists", "A new raceNormal cannot already have an ID")).body(null);
        }
        RaceNormal result = raceNormalRepository.save(raceNormal);
        return ResponseEntity.created(new URI("/api/race-normals/" + result.getRaceId()))
            .headers(HeaderUtil.createEntityCreationAlert("raceNormal", result.getRaceId().toString()))
            .body(result);
    }

    /**
     * PUT  /race-normals : Updates an existing raceNormal.
     *
     * @param raceNormal the raceNormal to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated raceNormal,
     * or with status 400 (Bad Request) if the raceNormal is not valid,
     * or with status 500 (Internal Server Error) if the raceNormal couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/race-normals",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RaceNormal> updateRaceNormal(@RequestBody RaceNormal raceNormal) throws URISyntaxException {
        log.debug("REST request to update RaceNormal : {}", raceNormal);
        if (raceNormal.getRaceId() == null) {
            return createRaceNormal(raceNormal);
        }
        RaceNormal result = raceNormalRepository.save(raceNormal);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("raceNormal", raceNormal.getRaceId().toString()))
            .body(result);
    }
    /**
     * GET  /race-normals-all : get all the raceNormals.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of raceNormals in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/race-normals-all",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<RaceNormal>> getAllRaceNormals()
        throws URISyntaxException {
        log.debug("REST request to get all RaceNormals");
        List<RaceNormal> normals = raceNormalRepository.findAll();
        return new ResponseEntity<>(normals, HttpStatus.OK);
    }

    /**
     * GET  /race-normals : get all the raceNormals.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of raceNormals in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/race-normals",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<RaceNormal>> getAllRaceNormals(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of RaceNormals");
        Page<RaceNormal> page = raceNormalRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/race-normals");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /race-normals/:id : get the "id" raceNormal.
     *
     * @param id the id of the raceNormal to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the raceNormal, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/race-normals/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RaceNormal> getRaceNormal(@PathVariable String id) {
        log.debug("REST request to get RaceNormal : {}", id);
        RaceNormal raceNormal = raceNormalRepository.findOne(UUID.fromString(id));
        return Optional.ofNullable(raceNormal)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /race-normals/:id : delete the "id" raceNormal.
     *
     * @param id the id of the raceNormal to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/race-normals/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRaceNormal(@PathVariable String id) {
        log.debug("REST request to delete RaceNormal : {}", id);
        raceNormalRepository.delete(UUID.fromString(id));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("raceNormal", id.toString())).build();
    }

}
