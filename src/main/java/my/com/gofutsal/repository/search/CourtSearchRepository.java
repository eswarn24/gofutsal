package my.com.gofutsal.repository.search;

import my.com.gofutsal.domain.Court;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Court entity.
 */
public interface CourtSearchRepository extends ElasticsearchRepository<Court, Long> {
}
