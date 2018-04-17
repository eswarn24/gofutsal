package my.com.gofutsal.service.dto;

import java.io.Serializable;
import my.com.gofutsal.domain.enumeration.UserBookingStatus;
import my.com.gofutsal.domain.enumeration.BookingDuration;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import io.github.jhipster.service.filter.InstantFilter;
import io.github.jhipster.service.filter.LocalDateFilter;



/**
 * Criteria class for the Booking entity. This class is used in BookingResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /bookings?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BookingCriteria implements Serializable {
    /**
     * Class for filtering UserBookingStatus
     */
    public static class UserBookingStatusFilter extends Filter<UserBookingStatus> {
    }

    /**
     * Class for filtering BookingDuration
     */
    public static class BookingDurationFilter extends Filter<BookingDuration> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private LocalDateFilter date;

    private UserBookingStatusFilter status;

    private InstantFilter time;

    private BookingDurationFilter duration;

    private LongFilter courtId;

    public BookingCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getDate() {
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public UserBookingStatusFilter getStatus() {
        return status;
    }

    public void setStatus(UserBookingStatusFilter status) {
        this.status = status;
    }

    public InstantFilter getTime() {
        return time;
    }

    public void setTime(InstantFilter time) {
        this.time = time;
    }

    public BookingDurationFilter getDuration() {
        return duration;
    }

    public void setDuration(BookingDurationFilter duration) {
        this.duration = duration;
    }

    public LongFilter getCourtId() {
        return courtId;
    }

    public void setCourtId(LongFilter courtId) {
        this.courtId = courtId;
    }

    @Override
    public String toString() {
        return "BookingCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (time != null ? "time=" + time + ", " : "") +
                (duration != null ? "duration=" + duration + ", " : "") +
                (courtId != null ? "courtId=" + courtId + ", " : "") +
            "}";
    }

}
