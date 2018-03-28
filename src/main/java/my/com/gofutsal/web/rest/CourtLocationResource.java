package my.com.gofutsal.web.rest;

import com.codahale.metrics.annotation.Timed;
import my.com.gofutsal.domain.CourtLocation;
import my.com.gofutsal.service.CourtLocationService;
import my.com.gofutsal.web.rest.errors.BadRequestAlertException;
import my.com.gofutsal.web.rest.util.HeaderUtil;
import my.com.gofutsal.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing CourtLocation.
 */
@RestController
@RequestMapping("/api")
public class CourtLocationResource {

    private final Logger log = LoggerFactory.getLogger(CourtLocationResource.class);

    private static final String ENTITY_NAME = "courtLocation";

    private final CourtLocationService courtLocationService;

    public CourtLocationResource(CourtLocationService courtLocationService) {
        this.courtLocationService = courtLocationService;
    }

    /**
     * POST  /court-locations : Create a new courtLocation.
     *
     * @param courtLocation the courtLocation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new courtLocation, or with status 400 (Bad Request) if the courtLocation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/court-locations")
    @Timed
    public ResponseEntity<CourtLocation> createCourtLocation(@Valid @RequestBody CourtLocation courtLocation) throws URISyntaxException {
        log.debug("REST request to save CourtLocation : {}", courtLocation);
        if (courtLocation.getId() != null) {
            throw new BadRequestAlertException("A new courtLocation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourtLocation result = courtLocationService.save(courtLocation);
        return ResponseEntity.created(new URI("/api/court-locations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /court-locations : Updates an existing courtLocation.
     *
     * @param courtLocation the courtLocation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated courtLocation,
     * or with status 400 (Bad Request) if the courtLocation is not valid,
     * or with status 500 (Internal Server Error) if the courtLocation couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/court-locations")
    @Timed
    public ResponseEntity<CourtLocation> updateCourtLocation(@Valid @RequestBody CourtLocation courtLocation) throws URISyntaxException {
        log.debug("REST request to update CourtLocation : {}", courtLocation);
        if (courtLocation.getId() == null) {
            return createCourtLocation(courtLocation);
        }
        CourtLocation result = courtLocationService.save(courtLocation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, courtLocation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /court-locations : get all the courtLocations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of courtLocations in body
     */
    @GetMapping("/court-locations")
    @Timed
    public ResponseEntity<List<CourtLocation>> getAllCourtLocations(Pageable pageable) {
        log.debug("REST request to get a page of CourtLocations");
        Page<CourtLocation> page = courtLocationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/court-locations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /court-locations/:id : get the "id" courtLocation.
     *
     * @param id the id of the courtLocation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the courtLocation, or with status 404 (Not Found)
     */
    @GetMapping("/court-locations/{id}")
    @Timed
    public ResponseEntity<CourtLocation> getCourtLocation(@PathVariable Long id) {
        log.debug("REST request to get CourtLocation : {}", id);
        CourtLocation courtLocation = courtLocationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(courtLocation));
    }

    /**
     * DELETE  /court-locations/:id : delete the "id" courtLocation.
     *
     * @param id the id of the courtLocation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/court-locations/{id}")
    @Timed
    public ResponseEntity<Void> deleteCourtLocation(@PathVariable Long id) {
        log.debug("REST request to delete CourtLocation : {}", id);
        courtLocationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/court-locations?query=:query : search for the courtLocation corresponding
     * to the query.
     *
     * @param query the query of the courtLocation search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/court-locations")
    @Timed
    public ResponseEntity<List<CourtLocation>> searchCourtLocations(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CourtLocations for query {}", query);
        Page<CourtLocation> page = courtLocationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/court-locations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
