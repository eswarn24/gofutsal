package my.com.gofutsal.service;

import my.com.gofutsal.domain.Court;
import my.com.gofutsal.repository.CourtRepository;
import my.com.gofutsal.repository.search.CourtSearchRepository;
import my.com.gofutsal.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Court.
 */
@Service
@Transactional
public class CourtService {

    private final Logger log = LoggerFactory.getLogger(CourtService.class);

    private final CourtRepository courtRepository;

    private final CourtSearchRepository courtSearchRepository;

    public CourtService(CourtRepository courtRepository, CourtSearchRepository courtSearchRepository) {
        this.courtRepository = courtRepository;
        this.courtSearchRepository = courtSearchRepository;
    }

    /**
     * Save a court.
     *
     * @param court the entity to save
     * @return the persisted entity
     */
    public Court save(Court court) {
        log.debug("Request to save Court : {}", court);
        Court result = courtRepository.save(court);
        courtSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the courts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Court> findAll(Pageable pageable) {
        log.debug("Request to get all Courts");
        //return courtRepository.findByUserIsCurrentUser();
       //return courtRepository.findAll(pageable);
        return courtRepository.findByUserLogin(SecurityUtils.getCurrentUserLogin(),pageable);
    }

    /**
     * Get one court by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Court findOne(Long id) {
        log.debug("Request to get Court : {}", id);
        return courtRepository.findOne(id);
    }

    /**
     * Delete the court by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Court : {}", id);
        courtRepository.delete(id);
        courtSearchRepository.delete(id);
    }

    /**
     * Search for the court corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Court> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Courts for query {}", query);
        Page<Court> result = courtSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
