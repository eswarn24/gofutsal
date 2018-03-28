package my.com.gofutsal.web.rest;

import com.codahale.metrics.annotation.Timed;
import my.com.gofutsal.domain.CourtType;
import my.com.gofutsal.service.CourtTypeService;
import my.com.gofutsal.web.rest.errors.BadRequestAlertException;
import my.com.gofutsal.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing CourtType.
 */
@RestController
@RequestMapping("/api")
public class CourtTypeResource {

    private final Logger log = LoggerFactory.getLogger(CourtTypeResource.class);

    private static final String ENTITY_NAME = "courtType";

    private final CourtTypeService courtTypeService;

    public CourtTypeResource(CourtTypeService courtTypeService) {
        this.courtTypeService = courtTypeService;
    }

    /**
     * POST  /court-types : Create a new courtType.
     *
     * @param courtType the courtType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new courtType, or with status 400 (Bad Request) if the courtType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/court-types")
    @Timed
    public ResponseEntity<CourtType> createCourtType(@Valid @RequestBody CourtType courtType) throws URISyntaxException {
        log.debug("REST request to save CourtType : {}", courtType);
        if (courtType.getId() != null) {
            throw new BadRequestAlertException("A new courtType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourtType result = courtTypeService.save(courtType);
        return ResponseEntity.created(new URI("/api/court-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /court-types : Updates an existing courtType.
     *
     * @param courtType the courtType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated courtType,
     * or with status 400 (Bad Request) if the courtType is not valid,
     * or with status 500 (Internal Server Error) if the courtType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/court-types")
    @Timed
    public ResponseEntity<CourtType> updateCourtType(@Valid @RequestBody CourtType courtType) throws URISyntaxException {
        log.debug("REST request to update CourtType : {}", courtType);
        if (courtType.getId() == null) {
            return createCourtType(courtType);
        }
        CourtType result = courtTypeService.save(courtType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, courtType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /court-types : get all the courtTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of courtTypes in body
     */
    @GetMapping("/court-types")
    @Timed
    public List<CourtType> getAllCourtTypes() {
        log.debug("REST request to get all CourtTypes");
        return courtTypeService.findAll();
        }

    /**
     * GET  /court-types/:id : get the "id" courtType.
     *
     * @param id the id of the courtType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the courtType, or with status 404 (Not Found)
     */
    @GetMapping("/court-types/{id}")
    @Timed
    public ResponseEntity<CourtType> getCourtType(@PathVariable Long id) {
        log.debug("REST request to get CourtType : {}", id);
        CourtType courtType = courtTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(courtType));
    }

    /**
     * DELETE  /court-types/:id : delete the "id" courtType.
     *
     * @param id the id of the courtType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/court-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteCourtType(@PathVariable Long id) {
        log.debug("REST request to delete CourtType : {}", id);
        courtTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/court-types?query=:query : search for the courtType corresponding
     * to the query.
     *
     * @param query the query of the courtType search
     * @return the result of the search
     */
    @GetMapping("/_search/court-types")
    @Timed
    public List<CourtType> searchCourtTypes(@RequestParam String query) {
        log.debug("REST request to search CourtTypes for query {}", query);
        return courtTypeService.search(query);
    }

}
