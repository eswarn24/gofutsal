package my.com.gofutsal.web.rest;

import my.com.gofutsal.GofutsalApp;

import my.com.gofutsal.domain.Booking;
import my.com.gofutsal.domain.Court;
import my.com.gofutsal.domain.User;
import my.com.gofutsal.repository.BookingRepository;
import my.com.gofutsal.service.BookingService;
import my.com.gofutsal.repository.search.BookingSearchRepository;
import my.com.gofutsal.web.rest.errors.ExceptionTranslator;
import my.com.gofutsal.service.dto.BookingCriteria;
import my.com.gofutsal.service.BookingQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static my.com.gofutsal.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import my.com.gofutsal.domain.enumeration.UserBookingStatus;
/**
 * Test class for the BookingResource REST controller.
 *
 * @see BookingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GofutsalApp.class)
public class BookingResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final UserBookingStatus DEFAULT_STATUS = UserBookingStatus.Requested;
    private static final UserBookingStatus UPDATED_STATUS = UserBookingStatus.Apporved;

    private static final String DEFAULT_BOOKING_TIME = "AAAAAAAAAA";
    private static final String UPDATED_BOOKING_TIME = "BBBBBBBBBB";

    private static final String DEFAULT_BOOKING_HOUR = "AAAAAAAAAA";
    private static final String UPDATED_BOOKING_HOUR = "BBBBBBBBBB";

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingSearchRepository bookingSearchRepository;

    @Autowired
    private BookingQueryService bookingQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBookingMockMvc;

    private Booking booking;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BookingResource bookingResource = new BookingResource(bookingService, bookingQueryService);
        this.restBookingMockMvc = MockMvcBuilders.standaloneSetup(bookingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Booking createEntity(EntityManager em) {
        Booking booking = new Booking()
            .date(DEFAULT_DATE)
            .status(DEFAULT_STATUS)
            .bookingTime(DEFAULT_BOOKING_TIME)
            .bookingHour(DEFAULT_BOOKING_HOUR);
        // Add required entity
        Court court = CourtResourceIntTest.createEntity(em);
        em.persist(court);
        em.flush();
        booking.setCourt(court);
        // Add required entity
        User bookingUser = UserResourceIntTest.createEntity(em);
        em.persist(bookingUser);
        em.flush();
        booking.setBookingUser(bookingUser);
        return booking;
    }

    @Before
    public void initTest() {
        bookingSearchRepository.deleteAll();
        booking = createEntity(em);
    }

    @Test
    @Transactional
    public void createBooking() throws Exception {
        int databaseSizeBeforeCreate = bookingRepository.findAll().size();

        // Create the Booking
        restBookingMockMvc.perform(post("/api/bookings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(booking)))
            .andExpect(status().isCreated());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeCreate + 1);
        Booking testBooking = bookingList.get(bookingList.size() - 1);
        assertThat(testBooking.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testBooking.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testBooking.getBookingTime()).isEqualTo(DEFAULT_BOOKING_TIME);
        assertThat(testBooking.getBookingHour()).isEqualTo(DEFAULT_BOOKING_HOUR);

        // Validate the Booking in Elasticsearch
        Booking bookingEs = bookingSearchRepository.findOne(testBooking.getId());
        assertThat(bookingEs).isEqualToIgnoringGivenFields(testBooking);
    }

    @Test
    @Transactional
    public void createBookingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bookingRepository.findAll().size();

        // Create the Booking with an existing ID
        booking.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookingMockMvc.perform(post("/api/bookings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(booking)))
            .andExpect(status().isBadRequest());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setDate(null);

        // Create the Booking, which fails.

        restBookingMockMvc.perform(post("/api/bookings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(booking)))
            .andExpect(status().isBadRequest());

        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBookingTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setBookingTime(null);

        // Create the Booking, which fails.

        restBookingMockMvc.perform(post("/api/bookings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(booking)))
            .andExpect(status().isBadRequest());

        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBookingHourIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setBookingHour(null);

        // Create the Booking, which fails.

        restBookingMockMvc.perform(post("/api/bookings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(booking)))
            .andExpect(status().isBadRequest());

        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBookings() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList
        restBookingMockMvc.perform(get("/api/bookings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(booking.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].bookingTime").value(hasItem(DEFAULT_BOOKING_TIME.toString())))
            .andExpect(jsonPath("$.[*].bookingHour").value(hasItem(DEFAULT_BOOKING_HOUR.toString())));
    }

    @Test
    @Transactional
    public void getBooking() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get the booking
        restBookingMockMvc.perform(get("/api/bookings/{id}", booking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(booking.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.bookingTime").value(DEFAULT_BOOKING_TIME.toString()))
            .andExpect(jsonPath("$.bookingHour").value(DEFAULT_BOOKING_HOUR.toString()));
    }

    @Test
    @Transactional
    public void getAllBookingsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where date equals to DEFAULT_DATE
        defaultBookingShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the bookingList where date equals to UPDATED_DATE
        defaultBookingShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllBookingsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where date in DEFAULT_DATE or UPDATED_DATE
        defaultBookingShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the bookingList where date equals to UPDATED_DATE
        defaultBookingShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllBookingsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where date is not null
        defaultBookingShouldBeFound("date.specified=true");

        // Get all the bookingList where date is null
        defaultBookingShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllBookingsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where date greater than or equals to DEFAULT_DATE
        defaultBookingShouldBeFound("date.greaterOrEqualThan=" + DEFAULT_DATE);

        // Get all the bookingList where date greater than or equals to UPDATED_DATE
        defaultBookingShouldNotBeFound("date.greaterOrEqualThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllBookingsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where date less than or equals to DEFAULT_DATE
        defaultBookingShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the bookingList where date less than or equals to UPDATED_DATE
        defaultBookingShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }


    @Test
    @Transactional
    public void getAllBookingsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where status equals to DEFAULT_STATUS
        defaultBookingShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the bookingList where status equals to UPDATED_STATUS
        defaultBookingShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllBookingsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultBookingShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the bookingList where status equals to UPDATED_STATUS
        defaultBookingShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllBookingsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where status is not null
        defaultBookingShouldBeFound("status.specified=true");

        // Get all the bookingList where status is null
        defaultBookingShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllBookingsByBookingTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where bookingTime equals to DEFAULT_BOOKING_TIME
        defaultBookingShouldBeFound("bookingTime.equals=" + DEFAULT_BOOKING_TIME);

        // Get all the bookingList where bookingTime equals to UPDATED_BOOKING_TIME
        defaultBookingShouldNotBeFound("bookingTime.equals=" + UPDATED_BOOKING_TIME);
    }

    @Test
    @Transactional
    public void getAllBookingsByBookingTimeIsInShouldWork() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where bookingTime in DEFAULT_BOOKING_TIME or UPDATED_BOOKING_TIME
        defaultBookingShouldBeFound("bookingTime.in=" + DEFAULT_BOOKING_TIME + "," + UPDATED_BOOKING_TIME);

        // Get all the bookingList where bookingTime equals to UPDATED_BOOKING_TIME
        defaultBookingShouldNotBeFound("bookingTime.in=" + UPDATED_BOOKING_TIME);
    }

    @Test
    @Transactional
    public void getAllBookingsByBookingTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where bookingTime is not null
        defaultBookingShouldBeFound("bookingTime.specified=true");

        // Get all the bookingList where bookingTime is null
        defaultBookingShouldNotBeFound("bookingTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllBookingsByBookingHourIsEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where bookingHour equals to DEFAULT_BOOKING_HOUR
        defaultBookingShouldBeFound("bookingHour.equals=" + DEFAULT_BOOKING_HOUR);

        // Get all the bookingList where bookingHour equals to UPDATED_BOOKING_HOUR
        defaultBookingShouldNotBeFound("bookingHour.equals=" + UPDATED_BOOKING_HOUR);
    }

    @Test
    @Transactional
    public void getAllBookingsByBookingHourIsInShouldWork() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where bookingHour in DEFAULT_BOOKING_HOUR or UPDATED_BOOKING_HOUR
        defaultBookingShouldBeFound("bookingHour.in=" + DEFAULT_BOOKING_HOUR + "," + UPDATED_BOOKING_HOUR);

        // Get all the bookingList where bookingHour equals to UPDATED_BOOKING_HOUR
        defaultBookingShouldNotBeFound("bookingHour.in=" + UPDATED_BOOKING_HOUR);
    }

    @Test
    @Transactional
    public void getAllBookingsByBookingHourIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where bookingHour is not null
        defaultBookingShouldBeFound("bookingHour.specified=true");

        // Get all the bookingList where bookingHour is null
        defaultBookingShouldNotBeFound("bookingHour.specified=false");
    }

    @Test
    @Transactional
    public void getAllBookingsByCourtIsEqualToSomething() throws Exception {
        // Initialize the database
        Court court = CourtResourceIntTest.createEntity(em);
        em.persist(court);
        em.flush();
        booking.setCourt(court);
        bookingRepository.saveAndFlush(booking);
        Long courtId = court.getId();

        // Get all the bookingList where court equals to courtId
        defaultBookingShouldBeFound("courtId.equals=" + courtId);

        // Get all the bookingList where court equals to courtId + 1
        defaultBookingShouldNotBeFound("courtId.equals=" + (courtId + 1));
    }


    @Test
    @Transactional
    public void getAllBookingsByBookingUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User bookingUser = UserResourceIntTest.createEntity(em);
        em.persist(bookingUser);
        em.flush();
        booking.setBookingUser(bookingUser);
        bookingRepository.saveAndFlush(booking);
        Long bookingUserId = bookingUser.getId();

        // Get all the bookingList where bookingUser equals to bookingUserId
        defaultBookingShouldBeFound("bookingUserId.equals=" + bookingUserId);

        // Get all the bookingList where bookingUser equals to bookingUserId + 1
        defaultBookingShouldNotBeFound("bookingUserId.equals=" + (bookingUserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultBookingShouldBeFound(String filter) throws Exception {
        restBookingMockMvc.perform(get("/api/bookings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(booking.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].bookingTime").value(hasItem(DEFAULT_BOOKING_TIME.toString())))
            .andExpect(jsonPath("$.[*].bookingHour").value(hasItem(DEFAULT_BOOKING_HOUR.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultBookingShouldNotBeFound(String filter) throws Exception {
        restBookingMockMvc.perform(get("/api/bookings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingBooking() throws Exception {
        // Get the booking
        restBookingMockMvc.perform(get("/api/bookings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBooking() throws Exception {
        // Initialize the database
        bookingService.save(booking);

        int databaseSizeBeforeUpdate = bookingRepository.findAll().size();

        // Update the booking
        Booking updatedBooking = bookingRepository.findOne(booking.getId());
        // Disconnect from session so that the updates on updatedBooking are not directly saved in db
        em.detach(updatedBooking);
        updatedBooking
            .date(UPDATED_DATE)
            .status(UPDATED_STATUS)
            .bookingTime(UPDATED_BOOKING_TIME)
            .bookingHour(UPDATED_BOOKING_HOUR);

        restBookingMockMvc.perform(put("/api/bookings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBooking)))
            .andExpect(status().isOk());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeUpdate);
        Booking testBooking = bookingList.get(bookingList.size() - 1);
        assertThat(testBooking.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testBooking.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBooking.getBookingTime()).isEqualTo(UPDATED_BOOKING_TIME);
        assertThat(testBooking.getBookingHour()).isEqualTo(UPDATED_BOOKING_HOUR);

        // Validate the Booking in Elasticsearch
        Booking bookingEs = bookingSearchRepository.findOne(testBooking.getId());
        assertThat(bookingEs).isEqualToIgnoringGivenFields(testBooking);
    }

    @Test
    @Transactional
    public void updateNonExistingBooking() throws Exception {
        int databaseSizeBeforeUpdate = bookingRepository.findAll().size();

        // Create the Booking

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBookingMockMvc.perform(put("/api/bookings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(booking)))
            .andExpect(status().isCreated());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBooking() throws Exception {
        // Initialize the database
        bookingService.save(booking);

        int databaseSizeBeforeDelete = bookingRepository.findAll().size();

        // Get the booking
        restBookingMockMvc.perform(delete("/api/bookings/{id}", booking.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean bookingExistsInEs = bookingSearchRepository.exists(booking.getId());
        assertThat(bookingExistsInEs).isFalse();

        // Validate the database is empty
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBooking() throws Exception {
        // Initialize the database
        bookingService.save(booking);

        // Search the booking
        restBookingMockMvc.perform(get("/api/_search/bookings?query=id:" + booking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(booking.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].bookingTime").value(hasItem(DEFAULT_BOOKING_TIME.toString())))
            .andExpect(jsonPath("$.[*].bookingHour").value(hasItem(DEFAULT_BOOKING_HOUR.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Booking.class);
        Booking booking1 = new Booking();
        booking1.setId(1L);
        Booking booking2 = new Booking();
        booking2.setId(booking1.getId());
        assertThat(booking1).isEqualTo(booking2);
        booking2.setId(2L);
        assertThat(booking1).isNotEqualTo(booking2);
        booking1.setId(null);
        assertThat(booking1).isNotEqualTo(booking2);
    }
}
