package my.com.gofutsal.repository;

import my.com.gofutsal.domain.CourtType;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CourtType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourtTypeRepository extends JpaRepository<CourtType, Long> {

}
