package my.com.gofutsal.repository.search;

import my.com.gofutsal.domain.CourtType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CourtType entity.
 */
public interface CourtTypeSearchRepository extends ElasticsearchRepository<CourtType, Long> {
}
