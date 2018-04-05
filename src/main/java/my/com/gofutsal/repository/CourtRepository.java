package my.com.gofutsal.repository;

import my.com.gofutsal.domain.Court;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Court entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourtRepository extends JpaRepository<Court, Long> {

    @Query("select court from Court court where court.user.login = ?#{principal.username}")
    List<Court> findByUserIsCurrentUser();

}
