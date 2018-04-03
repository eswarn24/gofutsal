package my.com.gofutsal.repository;

import my.com.gofutsal.domain.BookingStatus;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the BookingStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookingStatusRepository extends JpaRepository<BookingStatus, Long> {

}
