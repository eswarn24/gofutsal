package my.com.gofutsal.service;

import my.com.gofutsal.domain.CourtType;
import my.com.gofutsal.repository.CourtTypeRepository;
import my.com.gofutsal.repository.search.CourtTypeSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing CourtType.
 */
@Service
@Transactional
public class CourtTypeService {

    private final Logger log = LoggerFactory.getLogger(CourtTypeService.class);

    private final CourtTypeRepository courtTypeRepository;

    private final CourtTypeSearchRepository courtTypeSearchRepository;

    public CourtTypeService(CourtTypeRepository courtTypeRepository, CourtTypeSearchRepository courtTypeSearchRepository) {
        this.courtTypeRepository = courtTypeRepository;
        this.courtTypeSearchRepository = courtTypeSearchRepository;
    }

    /**
     * Save a courtType.
     *
     * @param courtType the entity to save
     * @return the persisted entity
     */
    public CourtType save(CourtType courtType) {
        log.debug("Request to save CourtType : {}", courtType);
        CourtType result = courtTypeRepository.save(courtType);
        courtTypeSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the courtTypes.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<CourtType> findAll() {
        log.debug("Request to get all CourtTypes");
        return courtTypeRepository.findAll();
    }

    /**
     * Get one courtType by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public CourtType findOne(Long id) {
        log.debug("Request to get CourtType : {}", id);
        return courtTypeRepository.findOne(id);
    }

    /**
     * Delete the courtType by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CourtType : {}", id);
        courtTypeRepository.delete(id);
        courtTypeSearchRepository.delete(id);
    }

    /**
     * Search for the courtType corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<CourtType> search(String query) {
        log.debug("Request to search CourtTypes for query {}", query);
        return StreamSupport
            .stream(courtTypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
