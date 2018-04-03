package my.com.gofutsal.web.rest;

import my.com.gofutsal.GofutsalApp;

import my.com.gofutsal.domain.BookingStatus;
import my.com.gofutsal.repository.BookingStatusRepository;
import my.com.gofutsal.service.BookingStatusService;
import my.com.gofutsal.repository.search.BookingStatusSearchRepository;
import my.com.gofutsal.web.rest.errors.ExceptionTranslator;

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
import java.util.List;

import static my.com.gofutsal.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BookingStatusResource REST controller.
 *
 * @see BookingStatusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GofutsalApp.class)
public class BookingStatusResourceIntTest {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private BookingStatusRepository bookingStatusRepository;

    @Autowired
    private BookingStatusService bookingStatusService;

    @Autowired
    private BookingStatusSearchRepository bookingStatusSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBookingStatusMockMvc;

    private BookingStatus bookingStatus;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BookingStatusResource bookingStatusResource = new BookingStatusResource(bookingStatusService);
        this.restBookingStatusMockMvc = MockMvcBuilders.standaloneSetup(bookingStatusResource)
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
    public static BookingStatus createEntity(EntityManager em) {
        BookingStatus bookingStatus = new BookingStatus()
            .status(DEFAULT_STATUS);
        return bookingStatus;
    }

    @Before
    public void initTest() {
        bookingStatusSearchRepository.deleteAll();
        bookingStatus = createEntity(em);
    }

    @Test
    @Transactional
    public void createBookingStatus() throws Exception {
        int databaseSizeBeforeCreate = bookingStatusRepository.findAll().size();

        // Create the BookingStatus
        restBookingStatusMockMvc.perform(post("/api/booking-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookingStatus)))
            .andExpect(status().isCreated());

        // Validate the BookingStatus in the database
        List<BookingStatus> bookingStatusList = bookingStatusRepository.findAll();
        assertThat(bookingStatusList).hasSize(databaseSizeBeforeCreate + 1);
        BookingStatus testBookingStatus = bookingStatusList.get(bookingStatusList.size() - 1);
        assertThat(testBookingStatus.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the BookingStatus in Elasticsearch
        BookingStatus bookingStatusEs = bookingStatusSearchRepository.findOne(testBookingStatus.getId());
        assertThat(bookingStatusEs).isEqualToIgnoringGivenFields(testBookingStatus);
    }

    @Test
    @Transactional
    public void createBookingStatusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bookingStatusRepository.findAll().size();

        // Create the BookingStatus with an existing ID
        bookingStatus.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookingStatusMockMvc.perform(post("/api/booking-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookingStatus)))
            .andExpect(status().isBadRequest());

        // Validate the BookingStatus in the database
        List<BookingStatus> bookingStatusList = bookingStatusRepository.findAll();
        assertThat(bookingStatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingStatusRepository.findAll().size();
        // set the field null
        bookingStatus.setStatus(null);

        // Create the BookingStatus, which fails.

        restBookingStatusMockMvc.perform(post("/api/booking-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookingStatus)))
            .andExpect(status().isBadRequest());

        List<BookingStatus> bookingStatusList = bookingStatusRepository.findAll();
        assertThat(bookingStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBookingStatuses() throws Exception {
        // Initialize the database
        bookingStatusRepository.saveAndFlush(bookingStatus);

        // Get all the bookingStatusList
        restBookingStatusMockMvc.perform(get("/api/booking-statuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookingStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getBookingStatus() throws Exception {
        // Initialize the database
        bookingStatusRepository.saveAndFlush(bookingStatus);

        // Get the bookingStatus
        restBookingStatusMockMvc.perform(get("/api/booking-statuses/{id}", bookingStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bookingStatus.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBookingStatus() throws Exception {
        // Get the bookingStatus
        restBookingStatusMockMvc.perform(get("/api/booking-statuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBookingStatus() throws Exception {
        // Initialize the database
        bookingStatusService.save(bookingStatus);

        int databaseSizeBeforeUpdate = bookingStatusRepository.findAll().size();

        // Update the bookingStatus
        BookingStatus updatedBookingStatus = bookingStatusRepository.findOne(bookingStatus.getId());
        // Disconnect from session so that the updates on updatedBookingStatus are not directly saved in db
        em.detach(updatedBookingStatus);
        updatedBookingStatus
            .status(UPDATED_STATUS);

        restBookingStatusMockMvc.perform(put("/api/booking-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBookingStatus)))
            .andExpect(status().isOk());

        // Validate the BookingStatus in the database
        List<BookingStatus> bookingStatusList = bookingStatusRepository.findAll();
        assertThat(bookingStatusList).hasSize(databaseSizeBeforeUpdate);
        BookingStatus testBookingStatus = bookingStatusList.get(bookingStatusList.size() - 1);
        assertThat(testBookingStatus.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the BookingStatus in Elasticsearch
        BookingStatus bookingStatusEs = bookingStatusSearchRepository.findOne(testBookingStatus.getId());
        assertThat(bookingStatusEs).isEqualToIgnoringGivenFields(testBookingStatus);
    }

    @Test
    @Transactional
    public void updateNonExistingBookingStatus() throws Exception {
        int databaseSizeBeforeUpdate = bookingStatusRepository.findAll().size();

        // Create the BookingStatus

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBookingStatusMockMvc.perform(put("/api/booking-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookingStatus)))
            .andExpect(status().isCreated());

        // Validate the BookingStatus in the database
        List<BookingStatus> bookingStatusList = bookingStatusRepository.findAll();
        assertThat(bookingStatusList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBookingStatus() throws Exception {
        // Initialize the database
        bookingStatusService.save(bookingStatus);

        int databaseSizeBeforeDelete = bookingStatusRepository.findAll().size();

        // Get the bookingStatus
        restBookingStatusMockMvc.perform(delete("/api/booking-statuses/{id}", bookingStatus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean bookingStatusExistsInEs = bookingStatusSearchRepository.exists(bookingStatus.getId());
        assertThat(bookingStatusExistsInEs).isFalse();

        // Validate the database is empty
        List<BookingStatus> bookingStatusList = bookingStatusRepository.findAll();
        assertThat(bookingStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBookingStatus() throws Exception {
        // Initialize the database
        bookingStatusService.save(bookingStatus);

        // Search the bookingStatus
        restBookingStatusMockMvc.perform(get("/api/_search/booking-statuses?query=id:" + bookingStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookingStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookingStatus.class);
        BookingStatus bookingStatus1 = new BookingStatus();
        bookingStatus1.setId(1L);
        BookingStatus bookingStatus2 = new BookingStatus();
        bookingStatus2.setId(bookingStatus1.getId());
        assertThat(bookingStatus1).isEqualTo(bookingStatus2);
        bookingStatus2.setId(2L);
        assertThat(bookingStatus1).isNotEqualTo(bookingStatus2);
        bookingStatus1.setId(null);
        assertThat(bookingStatus1).isNotEqualTo(bookingStatus2);
    }
}
