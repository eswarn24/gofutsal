package my.com.gofutsal.service;

import my.com.gofutsal.domain.BookingStatus;
import my.com.gofutsal.repository.BookingStatusRepository;
import my.com.gofutsal.repository.search.BookingStatusSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing BookingStatus.
 */
@Service
@Transactional
public class BookingStatusService {

    private final Logger log = LoggerFactory.getLogger(BookingStatusService.class);

    private final BookingStatusRepository bookingStatusRepository;

    private final BookingStatusSearchRepository bookingStatusSearchRepository;

    public BookingStatusService(BookingStatusRepository bookingStatusRepository, BookingStatusSearchRepository bookingStatusSearchRepository) {
        this.bookingStatusRepository = bookingStatusRepository;
        this.bookingStatusSearchRepository = bookingStatusSearchRepository;
    }

    /**
     * Save a bookingStatus.
     *
     * @param bookingStatus the entity to save
     * @return the persisted entity
     */
    public BookingStatus save(BookingStatus bookingStatus) {
        log.debug("Request to save BookingStatus : {}", bookingStatus);
        BookingStatus result = bookingStatusRepository.save(bookingStatus);
        bookingStatusSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the bookingStatuses.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<BookingStatus> findAll() {
        log.debug("Request to get all BookingStatuses");
        return bookingStatusRepository.findAll();
    }

    /**
     * Get one bookingStatus by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public BookingStatus findOne(Long id) {
        log.debug("Request to get BookingStatus : {}", id);
        return bookingStatusRepository.findOne(id);
    }

    /**
     * Delete the bookingStatus by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BookingStatus : {}", id);
        bookingStatusRepository.delete(id);
        bookingStatusSearchRepository.delete(id);
    }

    /**
     * Search for the bookingStatus corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<BookingStatus> search(String query) {
        log.debug("Request to search BookingStatuses for query {}", query);
        return StreamSupport
            .stream(bookingStatusSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
