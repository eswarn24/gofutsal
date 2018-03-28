package my.com.gofutsal.repository;

import my.com.gofutsal.domain.CourtLocation;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CourtLocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourtLocationRepository extends JpaRepository<CourtLocation, Long> {

}
