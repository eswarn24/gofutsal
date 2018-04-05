package my.com.gofutsal.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Court.
 */
@Entity
@Table(name = "court")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "court")
public class Court implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "rate", nullable = false)
    private String rate;

    @OneToOne
    @JoinColumn(unique = true)
    private CourtLocation courtLocation;

    @OneToOne
    @JoinColumn(unique = true)
    private CourtType courtType;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Court name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRate() {
        return rate;
    }

    public Court rate(String rate) {
        this.rate = rate;
        return this;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public CourtLocation getCourtLocation() {
        return courtLocation;
    }

    public Court courtLocation(CourtLocation courtLocation) {
        this.courtLocation = courtLocation;
        return this;
    }

    public void setCourtLocation(CourtLocation courtLocation) {
        this.courtLocation = courtLocation;
    }

    public CourtType getCourtType() {
        return courtType;
    }

    public Court courtType(CourtType courtType) {
        this.courtType = courtType;
        return this;
    }

    public void setCourtType(CourtType courtType) {
        this.courtType = courtType;
    }

    public User getUser() {
        return user;
    }

    public Court user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
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
        Court court = (Court) o;
        if (court.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), court.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Court{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", rate='" + getRate() + "'" +
            "}";
    }
}
