package my.com.gofutsal.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

import my.com.gofutsal.domain.enumeration.UserBookingStatus;

import my.com.gofutsal.domain.enumeration.BookingDuration;

/**
 * A Booking.
 */
@Entity
@Table(name = "booking")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "booking")
public class Booking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserBookingStatus status;

    @NotNull
    @Column(name = "jhi_time", nullable = false)
    private Instant time;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "duration", nullable = false)
    private BookingDuration duration;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Court court;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Booking date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public UserBookingStatus getStatus() {
        return status;
    }

    public Booking status(UserBookingStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(UserBookingStatus status) {
        this.status = status;
    }

    public Instant getTime() {
        return time;
    }

    public Booking time(Instant time) {
        this.time = time;
        return this;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public BookingDuration getDuration() {
        return duration;
    }

    public Booking duration(BookingDuration duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(BookingDuration duration) {
        this.duration = duration;
    }

    public Court getCourt() {
        return court;
    }

    public Booking court(Court court) {
        this.court = court;
        return this;
    }

    public void setCourt(Court court) {
        this.court = court;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Booking booking = (Booking) o;
        if (booking.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), booking.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Booking{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", time='" + getTime() + "'" +
            ", duration='" + getDuration() + "'" +
            "}";
    }
}
