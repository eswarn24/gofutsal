package my.com.gofutsal.repository;

import my.com.gofutsal.domain.CourtLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.Optional;


/**
 * Spring Data JPA repository for the CourtLocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourtLocationRepository extends JpaRepository<CourtLocation, Long> {

   /* Page<CourtLocation> findByUserLogin(Optional<String> currentUserLogin, Pageable pageable);*/
}
