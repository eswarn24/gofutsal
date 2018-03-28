package my.com.gofutsal.repository.search;

import my.com.gofutsal.domain.CourtLocation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CourtLocation entity.
 */
public interface CourtLocationSearchRepository extends ElasticsearchRepository<CourtLocation, Long> {
}
