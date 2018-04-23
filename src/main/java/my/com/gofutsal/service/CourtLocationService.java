package my.com.gofutsal.service;

import my.com.gofutsal.domain.CourtLocation;
import my.com.gofutsal.repository.CourtLocationRepository;
import my.com.gofutsal.repository.search.CourtLocationSearchRepository;
import my.com.gofutsal.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing CourtLocation.
 */
@Service
@Transactional
public class CourtLocationService {

    private final Logger log = LoggerFactory.getLogger(CourtLocationService.class);

    private final CourtLocationRepository courtLocationRepository;

    private final CourtLocationSearchRepository courtLocationSearchRepository;

    public CourtLocationService(CourtLocationRepository courtLocationRepository, CourtLocationSearchRepository courtLocationSearchRepository) {
        this.courtLocationRepository = courtLocationRepository;
        this.courtLocationSearchRepository = courtLocationSearchRepository;
    }

    /**
     * Save a courtLocation.
     *
     * @param courtLocation the entity to save
     * @return the persisted entity
     */
    public CourtLocation save(CourtLocation courtLocation) {
        log.debug("Request to save CourtLocation : {}", courtLocation);
        CourtLocation result = courtLocationRepository.save(courtLocation);
        courtLocationSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the courtLocations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CourtLocation> findAll(Pageable pageable) {
        log.debug("Request to get all CourtLocations");
        return courtLocationRepository.findAll(pageable);
        //return courtLocationRepository.findByUserLogin(SecurityUtils.getCurrentUserLogin(),pageable);
    }

    /**
     * Get one courtLocation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public CourtLocation findOne(Long id) {
        log.debug("Request to get CourtLocation : {}", id);
        return courtLocationRepository.findOne(id);
    }

    /**
     * Delete the courtLocation by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CourtLocation : {}", id);
        courtLocationRepository.delete(id);
        courtLocationSearchRepository.delete(id);
    }

    /**
     * Search for the courtLocation corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CourtLocation> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CourtLocations for query {}", query);
        Page<CourtLocation> result = courtLocationSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
