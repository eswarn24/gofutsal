package my.com.gofutsal.web.rest;

import com.codahale.metrics.annotation.Timed;
import my.com.gofutsal.domain.BookingStatus;
import my.com.gofutsal.service.BookingStatusService;
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
 * REST controller for managing BookingStatus.
 */
@RestController
@RequestMapping("/api")
public class BookingStatusResource {

    private final Logger log = LoggerFactory.getLogger(BookingStatusResource.class);

    private static final String ENTITY_NAME = "bookingStatus";

    private final BookingStatusService bookingStatusService;

    public BookingStatusResource(BookingStatusService bookingStatusService) {
        this.bookingStatusService = bookingStatusService;
    }

    /**
     * POST  /booking-statuses : Create a new bookingStatus.
     *
     * @param bookingStatus the bookingStatus to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bookingStatus, or with status 400 (Bad Request) if the bookingStatus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/booking-statuses")
    @Timed
    public ResponseEntity<BookingStatus> createBookingStatus(@Valid @RequestBody BookingStatus bookingStatus) throws URISyntaxException {
        log.debug("REST request to save BookingStatus : {}", bookingStatus);
        if (bookingStatus.getId() != null) {
            throw new BadRequestAlertException("A new bookingStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BookingStatus result = bookingStatusService.save(bookingStatus);
        return ResponseEntity.created(new URI("/api/booking-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /booking-statuses : Updates an existing bookingStatus.
     *
     * @param bookingStatus the bookingStatus to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bookingStatus,
     * or with status 400 (Bad Request) if the bookingStatus is not valid,
     * or with status 500 (Internal Server Error) if the bookingStatus couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/booking-statuses")
    @Timed
    public ResponseEntity<BookingStatus> updateBookingStatus(@Valid @RequestBody BookingStatus bookingStatus) throws URISyntaxException {
        log.debug("REST request to update BookingStatus : {}", bookingStatus);
        if (bookingStatus.getId() == null) {
            return createBookingStatus(bookingStatus);
        }
        BookingStatus result = bookingStatusService.save(bookingStatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bookingStatus.getId().toString()))
            .body(result);
    }

    /**
     * GET  /booking-statuses : get all the bookingStatuses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of bookingStatuses in body
     */
    @GetMapping("/booking-statuses")
    @Timed
    public List<BookingStatus> getAllBookingStatuses() {
        log.debug("REST request to get all BookingStatuses");
        return bookingStatusService.findAll();
        }

    /**
     * GET  /booking-statuses/:id : get the "id" bookingStatus.
     *
     * @param id the id of the bookingStatus to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bookingStatus, or with status 404 (Not Found)
     */
    @GetMapping("/booking-statuses/{id}")
    @Timed
    public ResponseEntity<BookingStatus> getBookingStatus(@PathVariable Long id) {
        log.debug("REST request to get BookingStatus : {}", id);
        BookingStatus bookingStatus = bookingStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bookingStatus));
    }

    /**
     * DELETE  /booking-statuses/:id : delete the "id" bookingStatus.
     *
     * @param id the id of the bookingStatus to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/booking-statuses/{id}")
    @Timed
    public ResponseEntity<Void> deleteBookingStatus(@PathVariable Long id) {
        log.debug("REST request to delete BookingStatus : {}", id);
        bookingStatusService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/booking-statuses?query=:query : search for the bookingStatus corresponding
     * to the query.
     *
     * @param query the query of the bookingStatus search
     * @return the result of the search
     */
    @GetMapping("/_search/booking-statuses")
    @Timed
    public List<BookingStatus> searchBookingStatuses(@RequestParam String query) {
        log.debug("REST request to search BookingStatuses for query {}", query);
        return bookingStatusService.search(query);
    }

}
