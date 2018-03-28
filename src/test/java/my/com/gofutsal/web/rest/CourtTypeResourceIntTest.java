package my.com.gofutsal.web.rest;

import my.com.gofutsal.GofutsalApp;

import my.com.gofutsal.domain.CourtType;
import my.com.gofutsal.repository.CourtTypeRepository;
import my.com.gofutsal.service.CourtTypeService;
import my.com.gofutsal.repository.search.CourtTypeSearchRepository;
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
 * Test class for the CourtTypeResource REST controller.
 *
 * @see CourtTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GofutsalApp.class)
public class CourtTypeResourceIntTest {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    @Autowired
    private CourtTypeRepository courtTypeRepository;

    @Autowired
    private CourtTypeService courtTypeService;

    @Autowired
    private CourtTypeSearchRepository courtTypeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCourtTypeMockMvc;

    private CourtType courtType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CourtTypeResource courtTypeResource = new CourtTypeResource(courtTypeService);
        this.restCourtTypeMockMvc = MockMvcBuilders.standaloneSetup(courtTypeResource)
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
    public static CourtType createEntity(EntityManager em) {
        CourtType courtType = new CourtType()
            .type(DEFAULT_TYPE);
        return courtType;
    }

    @Before
    public void initTest() {
        courtTypeSearchRepository.deleteAll();
        courtType = createEntity(em);
    }

    @Test
    @Transactional
    public void createCourtType() throws Exception {
        int databaseSizeBeforeCreate = courtTypeRepository.findAll().size();

        // Create the CourtType
        restCourtTypeMockMvc.perform(post("/api/court-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courtType)))
            .andExpect(status().isCreated());

        // Validate the CourtType in the database
        List<CourtType> courtTypeList = courtTypeRepository.findAll();
        assertThat(courtTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CourtType testCourtType = courtTypeList.get(courtTypeList.size() - 1);
        assertThat(testCourtType.getType()).isEqualTo(DEFAULT_TYPE);

        // Validate the CourtType in Elasticsearch
        CourtType courtTypeEs = courtTypeSearchRepository.findOne(testCourtType.getId());
        assertThat(courtTypeEs).isEqualToIgnoringGivenFields(testCourtType);
    }

    @Test
    @Transactional
    public void createCourtTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courtTypeRepository.findAll().size();

        // Create the CourtType with an existing ID
        courtType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourtTypeMockMvc.perform(post("/api/court-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courtType)))
            .andExpect(status().isBadRequest());

        // Validate the CourtType in the database
        List<CourtType> courtTypeList = courtTypeRepository.findAll();
        assertThat(courtTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = courtTypeRepository.findAll().size();
        // set the field null
        courtType.setType(null);

        // Create the CourtType, which fails.

        restCourtTypeMockMvc.perform(post("/api/court-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courtType)))
            .andExpect(status().isBadRequest());

        List<CourtType> courtTypeList = courtTypeRepository.findAll();
        assertThat(courtTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCourtTypes() throws Exception {
        // Initialize the database
        courtTypeRepository.saveAndFlush(courtType);

        // Get all the courtTypeList
        restCourtTypeMockMvc.perform(get("/api/court-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courtType.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getCourtType() throws Exception {
        // Initialize the database
        courtTypeRepository.saveAndFlush(courtType);

        // Get the courtType
        restCourtTypeMockMvc.perform(get("/api/court-types/{id}", courtType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(courtType.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCourtType() throws Exception {
        // Get the courtType
        restCourtTypeMockMvc.perform(get("/api/court-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourtType() throws Exception {
        // Initialize the database
        courtTypeService.save(courtType);

        int databaseSizeBeforeUpdate = courtTypeRepository.findAll().size();

        // Update the courtType
        CourtType updatedCourtType = courtTypeRepository.findOne(courtType.getId());
        // Disconnect from session so that the updates on updatedCourtType are not directly saved in db
        em.detach(updatedCourtType);
        updatedCourtType
            .type(UPDATED_TYPE);

        restCourtTypeMockMvc.perform(put("/api/court-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCourtType)))
            .andExpect(status().isOk());

        // Validate the CourtType in the database
        List<CourtType> courtTypeList = courtTypeRepository.findAll();
        assertThat(courtTypeList).hasSize(databaseSizeBeforeUpdate);
        CourtType testCourtType = courtTypeList.get(courtTypeList.size() - 1);
        assertThat(testCourtType.getType()).isEqualTo(UPDATED_TYPE);

        // Validate the CourtType in Elasticsearch
        CourtType courtTypeEs = courtTypeSearchRepository.findOne(testCourtType.getId());
        assertThat(courtTypeEs).isEqualToIgnoringGivenFields(testCourtType);
    }

    @Test
    @Transactional
    public void updateNonExistingCourtType() throws Exception {
        int databaseSizeBeforeUpdate = courtTypeRepository.findAll().size();

        // Create the CourtType

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCourtTypeMockMvc.perform(put("/api/court-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courtType)))
            .andExpect(status().isCreated());

        // Validate the CourtType in the database
        List<CourtType> courtTypeList = courtTypeRepository.findAll();
        assertThat(courtTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCourtType() throws Exception {
        // Initialize the database
        courtTypeService.save(courtType);

        int databaseSizeBeforeDelete = courtTypeRepository.findAll().size();

        // Get the courtType
        restCourtTypeMockMvc.perform(delete("/api/court-types/{id}", courtType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean courtTypeExistsInEs = courtTypeSearchRepository.exists(courtType.getId());
        assertThat(courtTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<CourtType> courtTypeList = courtTypeRepository.findAll();
        assertThat(courtTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCourtType() throws Exception {
        // Initialize the database
        courtTypeService.save(courtType);

        // Search the courtType
        restCourtTypeMockMvc.perform(get("/api/_search/court-types?query=id:" + courtType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courtType.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourtType.class);
        CourtType courtType1 = new CourtType();
        courtType1.setId(1L);
        CourtType courtType2 = new CourtType();
        courtType2.setId(courtType1.getId());
        assertThat(courtType1).isEqualTo(courtType2);
        courtType2.setId(2L);
        assertThat(courtType1).isNotEqualTo(courtType2);
        courtType1.setId(null);
        assertThat(courtType1).isNotEqualTo(courtType2);
    }
}
