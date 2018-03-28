package my.com.gofutsal.web.rest;

import my.com.gofutsal.GofutsalApp;

import my.com.gofutsal.domain.CourtLocation;
import my.com.gofutsal.repository.CourtLocationRepository;
import my.com.gofutsal.service.CourtLocationService;
import my.com.gofutsal.repository.search.CourtLocationSearchRepository;
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
 * Test class for the CourtLocationResource REST controller.
 *
 * @see CourtLocationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GofutsalApp.class)
public class CourtLocationResourceIntTest {

    private static final String DEFAULT_REGION = "AAAAAAAAAA";
    private static final String UPDATED_REGION = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    @Autowired
    private CourtLocationRepository courtLocationRepository;

    @Autowired
    private CourtLocationService courtLocationService;

    @Autowired
    private CourtLocationSearchRepository courtLocationSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCourtLocationMockMvc;

    private CourtLocation courtLocation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CourtLocationResource courtLocationResource = new CourtLocationResource(courtLocationService);
        this.restCourtLocationMockMvc = MockMvcBuilders.standaloneSetup(courtLocationResource)
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
    public static CourtLocation createEntity(EntityManager em) {
        CourtLocation courtLocation = new CourtLocation()
            .region(DEFAULT_REGION)
            .address(DEFAULT_ADDRESS)
            .state(DEFAULT_STATE)
            .country(DEFAULT_COUNTRY);
        return courtLocation;
    }

    @Before
    public void initTest() {
        courtLocationSearchRepository.deleteAll();
        courtLocation = createEntity(em);
    }

    @Test
    @Transactional
    public void createCourtLocation() throws Exception {
        int databaseSizeBeforeCreate = courtLocationRepository.findAll().size();

        // Create the CourtLocation
        restCourtLocationMockMvc.perform(post("/api/court-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courtLocation)))
            .andExpect(status().isCreated());

        // Validate the CourtLocation in the database
        List<CourtLocation> courtLocationList = courtLocationRepository.findAll();
        assertThat(courtLocationList).hasSize(databaseSizeBeforeCreate + 1);
        CourtLocation testCourtLocation = courtLocationList.get(courtLocationList.size() - 1);
        assertThat(testCourtLocation.getRegion()).isEqualTo(DEFAULT_REGION);
        assertThat(testCourtLocation.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testCourtLocation.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testCourtLocation.getCountry()).isEqualTo(DEFAULT_COUNTRY);

        // Validate the CourtLocation in Elasticsearch
        CourtLocation courtLocationEs = courtLocationSearchRepository.findOne(testCourtLocation.getId());
        assertThat(courtLocationEs).isEqualToIgnoringGivenFields(testCourtLocation);
    }

    @Test
    @Transactional
    public void createCourtLocationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courtLocationRepository.findAll().size();

        // Create the CourtLocation with an existing ID
        courtLocation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourtLocationMockMvc.perform(post("/api/court-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courtLocation)))
            .andExpect(status().isBadRequest());

        // Validate the CourtLocation in the database
        List<CourtLocation> courtLocationList = courtLocationRepository.findAll();
        assertThat(courtLocationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkRegionIsRequired() throws Exception {
        int databaseSizeBeforeTest = courtLocationRepository.findAll().size();
        // set the field null
        courtLocation.setRegion(null);

        // Create the CourtLocation, which fails.

        restCourtLocationMockMvc.perform(post("/api/court-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courtLocation)))
            .andExpect(status().isBadRequest());

        List<CourtLocation> courtLocationList = courtLocationRepository.findAll();
        assertThat(courtLocationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = courtLocationRepository.findAll().size();
        // set the field null
        courtLocation.setAddress(null);

        // Create the CourtLocation, which fails.

        restCourtLocationMockMvc.perform(post("/api/court-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courtLocation)))
            .andExpect(status().isBadRequest());

        List<CourtLocation> courtLocationList = courtLocationRepository.findAll();
        assertThat(courtLocationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = courtLocationRepository.findAll().size();
        // set the field null
        courtLocation.setState(null);

        // Create the CourtLocation, which fails.

        restCourtLocationMockMvc.perform(post("/api/court-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courtLocation)))
            .andExpect(status().isBadRequest());

        List<CourtLocation> courtLocationList = courtLocationRepository.findAll();
        assertThat(courtLocationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = courtLocationRepository.findAll().size();
        // set the field null
        courtLocation.setCountry(null);

        // Create the CourtLocation, which fails.

        restCourtLocationMockMvc.perform(post("/api/court-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courtLocation)))
            .andExpect(status().isBadRequest());

        List<CourtLocation> courtLocationList = courtLocationRepository.findAll();
        assertThat(courtLocationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCourtLocations() throws Exception {
        // Initialize the database
        courtLocationRepository.saveAndFlush(courtLocation);

        // Get all the courtLocationList
        restCourtLocationMockMvc.perform(get("/api/court-locations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courtLocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())));
    }

    @Test
    @Transactional
    public void getCourtLocation() throws Exception {
        // Initialize the database
        courtLocationRepository.saveAndFlush(courtLocation);

        // Get the courtLocation
        restCourtLocationMockMvc.perform(get("/api/court-locations/{id}", courtLocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(courtLocation.getId().intValue()))
            .andExpect(jsonPath("$.region").value(DEFAULT_REGION.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCourtLocation() throws Exception {
        // Get the courtLocation
        restCourtLocationMockMvc.perform(get("/api/court-locations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourtLocation() throws Exception {
        // Initialize the database
        courtLocationService.save(courtLocation);

        int databaseSizeBeforeUpdate = courtLocationRepository.findAll().size();

        // Update the courtLocation
        CourtLocation updatedCourtLocation = courtLocationRepository.findOne(courtLocation.getId());
        // Disconnect from session so that the updates on updatedCourtLocation are not directly saved in db
        em.detach(updatedCourtLocation);
        updatedCourtLocation
            .region(UPDATED_REGION)
            .address(UPDATED_ADDRESS)
            .state(UPDATED_STATE)
            .country(UPDATED_COUNTRY);

        restCourtLocationMockMvc.perform(put("/api/court-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCourtLocation)))
            .andExpect(status().isOk());

        // Validate the CourtLocation in the database
        List<CourtLocation> courtLocationList = courtLocationRepository.findAll();
        assertThat(courtLocationList).hasSize(databaseSizeBeforeUpdate);
        CourtLocation testCourtLocation = courtLocationList.get(courtLocationList.size() - 1);
        assertThat(testCourtLocation.getRegion()).isEqualTo(UPDATED_REGION);
        assertThat(testCourtLocation.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCourtLocation.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testCourtLocation.getCountry()).isEqualTo(UPDATED_COUNTRY);

        // Validate the CourtLocation in Elasticsearch
        CourtLocation courtLocationEs = courtLocationSearchRepository.findOne(testCourtLocation.getId());
        assertThat(courtLocationEs).isEqualToIgnoringGivenFields(testCourtLocation);
    }

    @Test
    @Transactional
    public void updateNonExistingCourtLocation() throws Exception {
        int databaseSizeBeforeUpdate = courtLocationRepository.findAll().size();

        // Create the CourtLocation

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCourtLocationMockMvc.perform(put("/api/court-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courtLocation)))
            .andExpect(status().isCreated());

        // Validate the CourtLocation in the database
        List<CourtLocation> courtLocationList = courtLocationRepository.findAll();
        assertThat(courtLocationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCourtLocation() throws Exception {
        // Initialize the database
        courtLocationService.save(courtLocation);

        int databaseSizeBeforeDelete = courtLocationRepository.findAll().size();

        // Get the courtLocation
        restCourtLocationMockMvc.perform(delete("/api/court-locations/{id}", courtLocation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean courtLocationExistsInEs = courtLocationSearchRepository.exists(courtLocation.getId());
        assertThat(courtLocationExistsInEs).isFalse();

        // Validate the database is empty
        List<CourtLocation> courtLocationList = courtLocationRepository.findAll();
        assertThat(courtLocationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCourtLocation() throws Exception {
        // Initialize the database
        courtLocationService.save(courtLocation);

        // Search the courtLocation
        restCourtLocationMockMvc.perform(get("/api/_search/court-locations?query=id:" + courtLocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courtLocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourtLocation.class);
        CourtLocation courtLocation1 = new CourtLocation();
        courtLocation1.setId(1L);
        CourtLocation courtLocation2 = new CourtLocation();
        courtLocation2.setId(courtLocation1.getId());
        assertThat(courtLocation1).isEqualTo(courtLocation2);
        courtLocation2.setId(2L);
        assertThat(courtLocation1).isNotEqualTo(courtLocation2);
        courtLocation1.setId(null);
        assertThat(courtLocation1).isNotEqualTo(courtLocation2);
    }
}
