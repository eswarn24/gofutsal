package my.com.gofutsal.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import my.com.gofutsal.domain.enumeration.UserBookingStatus;

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
    @Column(name = "booking_time", nullable = false)
    private String bookingTime;

    @NotNull
    @Column(name = "booking_hour", nullable = false)
    private String bookingHour;

    @OneToOne(optional = false)

    @JoinColumn(unique = true)
    private Court court;

    @ManyToOne(optional = false)

    private User bookingUser;

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

    public String getBookingTime() {
        return bookingTime;
    }

    public Booking bookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
        return this;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getBookingHour() {
        return bookingHour;
    }

    public Booking bookingHour(String bookingHour) {
        this.bookingHour = bookingHour;
        return this;
    }

    public void setBookingHour(String bookingHour) {
        this.bookingHour = bookingHour;
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

    public User getBookingUser() {
        return bookingUser;
    }

    public Booking bookingUser(User user) {
        this.bookingUser = user;
        return this;
    }

    public void setBookingUser(User user) {
        this.bookingUser = user;
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
            ", bookingTime='" + getBookingTime() + "'" +
            ", bookingHour='" + getBookingHour() + "'" +
            "}";
    }
}
