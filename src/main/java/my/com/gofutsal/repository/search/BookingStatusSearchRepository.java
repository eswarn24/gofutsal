package my.com.gofutsal.repository.search;

import my.com.gofutsal.domain.BookingStatus;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the BookingStatus entity.
 */
public interface BookingStatusSearchRepository extends ElasticsearchRepository<BookingStatus, Long> {
}
