package my.com.gofutsal.web.rest;

import com.codahale.metrics.annotation.Timed;
import my.com.gofutsal.domain.Court;
import my.com.gofutsal.service.CourtService;
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
 * REST controller for managing Court.
 */
@RestController
@RequestMapping("/api")
public class CourtResource {

    private final Logger log = LoggerFactory.getLogger(CourtResource.class);

    private static final String ENTITY_NAME = "court";

    private final CourtService courtService;

    public CourtResource(CourtService courtService) {
        this.courtService = courtService;
    }

    /**
     * POST  /courts : Create a new court.
     *
     * @param court the court to create
     * @return the ResponseEntity with status 201 (Created) and with body the new court, or with status 400 (Bad Request) if the court has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/courts")
    @Timed
    public ResponseEntity<Court> createCourt(@Valid @RequestBody Court court) throws URISyntaxException {
        log.debug("REST request to save Court : {}", court);
        if (court.getId() != null) {
            throw new BadRequestAlertException("A new court cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Court result = courtService.save(court);
        return ResponseEntity.created(new URI("/api/courts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /courts : Updates an existing court.
     *
     * @param court the court to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated court,
     * or with status 400 (Bad Request) if the court is not valid,
     * or with status 500 (Internal Server Error) if the court couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/courts")
    @Timed
    public ResponseEntity<Court> updateCourt(@Valid @RequestBody Court court) throws URISyntaxException {
        log.debug("REST request to update Court : {}", court);
        if (court.getId() == null) {
            return createCourt(court);
        }
        Court result = courtService.save(court);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, court.getId().toString()))
            .body(result);
    }

    /**
     * GET  /courts : get all the courts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of courts in body
     */
    @GetMapping("/courts")
    @Timed
    public ResponseEntity<List<Court>> getAllCourts(Pageable pageable) {
        log.debug("REST request to get a page of Courts");
        Page<Court> page = courtService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/courts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /courts/:id : get the "id" court.
     *
     * @param id the id of the court to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the court, or with status 404 (Not Found)
     */
    @GetMapping("/courts/{id}")
    @Timed
    public ResponseEntity<Court> getCourt(@PathVariable Long id) {
        log.debug("REST request to get Court : {}", id);
        Court court = courtService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(court));
    }

    /**
     * DELETE  /courts/:id : delete the "id" court.
     *
     * @param id the id of the court to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/courts/{id}")
    @Timed
    public ResponseEntity<Void> deleteCourt(@PathVariable Long id) {
        log.debug("REST request to delete Court : {}", id);
        courtService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/courts?query=:query : search for the court corresponding
     * to the query.
     *
     * @param query the query of the court search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/courts")
    @Timed
    public ResponseEntity<List<Court>> searchCourts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Courts for query {}", query);
        Page<Court> page = courtService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/courts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
