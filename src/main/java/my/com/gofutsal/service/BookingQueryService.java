package my.com.gofutsal.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import my.com.gofutsal.domain.Booking;
import my.com.gofutsal.domain.*; // for static metamodels
import my.com.gofutsal.repository.BookingRepository;
import my.com.gofutsal.repository.search.BookingSearchRepository;
import my.com.gofutsal.service.dto.BookingCriteria;


/**
 * Service for executing complex queries for Booking entities in the database.
 * The main input is a {@link BookingCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Booking} or a {@link Page} of {@link Booking} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BookingQueryService extends QueryService<Booking> {

    private final Logger log = LoggerFactory.getLogger(BookingQueryService.class);


    private final BookingRepository bookingRepository;

    private final BookingSearchRepository bookingSearchRepository;

    public BookingQueryService(BookingRepository bookingRepository, BookingSearchRepository bookingSearchRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingSearchRepository = bookingSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Booking} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Booking> findByCriteria(BookingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Booking> specification = createSpecification(criteria);
        return bookingRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Booking} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Booking> findByCriteria(BookingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Booking> specification = createSpecification(criteria);
        return bookingRepository.findAll(specification, page);
    }

    /**
     * Function to convert BookingCriteria to a {@link Specifications}
     */
    private Specifications<Booking> createSpecification(BookingCriteria criteria) {
        Specifications<Booking> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Booking_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Booking_.date));
            }
            if (criteria.getStartTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartTime(), Booking_.startTime));
            }
            if (criteria.getEndTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndTime(), Booking_.endTime));
            }
            if (criteria.getCourtId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCourtId(), Booking_.court, Court_.id));
            }
        }
        return specification;
    }

}
