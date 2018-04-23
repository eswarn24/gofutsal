package my.com.gofutsal.repository;

import my.com.gofutsal.domain.Booking;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Booking entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {

    @Query("select booking from Booking booking where booking.bookingUser.login = ?#{principal.username}")
    List<Booking> findByBookingUserIsCurrentUser();

}
