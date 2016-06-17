package chrislovecnm.k8s.gpmr.web.rest;

import com.codahale.metrics.annotation.Timed;
import chrislovecnm.k8s.gpmr.domain.DataCounter;
import chrislovecnm.k8s.gpmr.repository.DataCounterRepository;
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
 * REST controller for managing DataCounter.
 */
@RestController
@RequestMapping("/api")
public class DataCounterResource {

    private final Logger log = LoggerFactory.getLogger(DataCounterResource.class);

    @Inject
    private DataCounterRepository dataCounterRepository;

    /**
     * POST  /data-counters : Create a new dataCounter.
     *
     * @param dataCounter the dataCounter to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dataCounter, or with status 400 (Bad Request) if the dataCounter has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/data-counters",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DataCounter> createDataCounter(@RequestBody DataCounter dataCounter) throws URISyntaxException {
        log.debug("REST request to save DataCounter : {}", dataCounter);
        DataCounter result = dataCounterRepository.save(dataCounter);
        return ResponseEntity.created(new URI("/api/data-counters/" + result.getVtype()))
            .headers(HeaderUtil.createEntityCreationAlert("dataCounter", result.getVtype()))
            .body(result);
    }

    /**
     * PUT  /data-counters : Updates an existing dataCounter.
     *
     * @param dataCounter the dataCounter to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dataCounter,
     * or with status 400 (Bad Request) if the dataCounter is not valid,
     * or with status 500 (Internal Server Error) if the dataCounter couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/data-counters",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DataCounter> updateDataCounter(@RequestBody DataCounter dataCounter) throws URISyntaxException {
        log.debug("REST request to update DataCounter : {}", dataCounter);
        DataCounter result = dataCounterRepository.save(dataCounter);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("dataCounter", dataCounter.getVtype()))
            .body(result);
    }

    /**
     * GET  /data-counters : get all the dataCounters.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of dataCounters in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/data-counters",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<DataCounter>> getAllDataCounters(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of DataCounters");
        Page<DataCounter> page = dataCounterRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/data-counters");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /data-counters/:id : get the "id" dataCounter.
     *
     * @param id the id of the dataCounter to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dataCounter, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/data-counters/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DataCounter> getDataCounter(@PathVariable String id) {
        log.debug("REST request to get DataCounter : {}", id);
        DataCounter dataCounter = dataCounterRepository.findOne(id);
        return Optional.ofNullable(dataCounter)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /data-counters/:id : delete the "id" dataCounter.
     *
     * @param id the id of the dataCounter to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/data-counters/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDataCounter(@PathVariable String id) {
        log.debug("REST request to delete DataCounter : {}", id);
        dataCounterRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("dataCounter", id.toString())).build();
    }

}
