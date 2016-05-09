package chrislovecnm.k8s.gpmr.web.rest;

import chrislovecnm.k8s.gpmr.AbstractCassandraTest;
import chrislovecnm.k8s.gpmr.GpmrApp;
import chrislovecnm.k8s.gpmr.domain.RaceResults;
import chrislovecnm.k8s.gpmr.repository.RaceResultsRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the RaceResultsResource REST controller.
 *
 * @see RaceResultsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GpmrApp.class)
@WebAppConfiguration
@IntegrationTest
public class RaceResultsResourceIntTest extends AbstractCassandraTest {


    private static final UUID DEFAULT_PET_ID = UUID.randomUUID();
    private static final UUID UPDATED_PET_ID = UUID.randomUUID();
    private static final String DEFAULT_PET_NAME = "AAAAA";
    private static final String UPDATED_PET_NAME = "BBBBB";
    private static final String DEFAULT_PET_CATEGORY = "AAAAA";
    private static final String UPDATED_PET_CATEGORY = "BBBBB";

    private static final UUID DEFAULT_PET_CATEGORY_ID = UUID.randomUUID();
    private static final UUID UPDATED_PET_CATEGORY_ID = UUID.randomUUID();

    private static final Integer DEFAULT_PLACE = 1;
    private static final Integer UPDATED_PLACE = 2;

    private static final Date DEFAULT_TIME = new Date();
    private static final Date UPDATED_TIME = new Date();

    @Inject
    private RaceResultsRepository raceResultsRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRaceResultsMockMvc;

    private RaceResults raceResults;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RaceResultsResource raceResultsResource = new RaceResultsResource();
        ReflectionTestUtils.setField(raceResultsResource, "raceResultsRepository", raceResultsRepository);
        this.restRaceResultsMockMvc = MockMvcBuilders.standaloneSetup(raceResultsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        raceResultsRepository.deleteAll();
        raceResults = new RaceResults();
        raceResults.setPetId(DEFAULT_PET_ID);
        raceResults.setPetName(DEFAULT_PET_NAME);
        raceResults.setPetCategory(DEFAULT_PET_CATEGORY);
        raceResults.setPetCategoryId(DEFAULT_PET_CATEGORY_ID);
        raceResults.setPlace(DEFAULT_PLACE);
        raceResults.setTime(DEFAULT_TIME);
    }

    @Test
    public void createRaceResults() throws Exception {
        int databaseSizeBeforeCreate = raceResultsRepository.findAll().size();

        // Create the RaceResults

        restRaceResultsMockMvc.perform(post("/api/race-results")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(raceResults)))
                .andExpect(status().isCreated());

        // Validate the RaceResults in the database
        List<RaceResults> raceResults = raceResultsRepository.findAll();
        assertThat(raceResults).hasSize(databaseSizeBeforeCreate + 1);
        RaceResults testRaceResults = raceResults.get(raceResults.size() - 1);
        assertThat(testRaceResults.getPetId()).isEqualTo(DEFAULT_PET_ID);
        assertThat(testRaceResults.getPetName()).isEqualTo(DEFAULT_PET_NAME);
        assertThat(testRaceResults.getPetCategory()).isEqualTo(DEFAULT_PET_CATEGORY);
        assertThat(testRaceResults.getPetCategoryId()).isEqualTo(DEFAULT_PET_CATEGORY_ID);
        assertThat(testRaceResults.getPlace()).isEqualTo(DEFAULT_PLACE);
        assertThat(testRaceResults.getTime()).isEqualTo(DEFAULT_TIME);
    }

    @Test
    public void getAllRaceResults() throws Exception {
        // Initialize the database
        raceResultsRepository.save(raceResults);

        // Get all the raceResults
        restRaceResultsMockMvc.perform(get("/api/race-results?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(raceResults.getId().toString())))
                .andExpect(jsonPath("$.[*].petId").value(hasItem(DEFAULT_PET_ID.toString())))
                .andExpect(jsonPath("$.[*].petName").value(hasItem(DEFAULT_PET_NAME.toString())))
                .andExpect(jsonPath("$.[*].petCategory").value(hasItem(DEFAULT_PET_CATEGORY.toString())))
                .andExpect(jsonPath("$.[*].petCategoryId").value(hasItem(DEFAULT_PET_CATEGORY_ID.toString())))
                .andExpect(jsonPath("$.[*].place").value(hasItem(DEFAULT_PLACE)))
                .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.getTime())));
    }

    @Test
    public void getRaceResults() throws Exception {
        // Initialize the database
        raceResultsRepository.save(raceResults);

        // Get the raceResults
        restRaceResultsMockMvc.perform(get("/api/race-results/{id}", raceResults.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(raceResults.getId().toString()))
            .andExpect(jsonPath("$.petId").value(DEFAULT_PET_ID.toString()))
            .andExpect(jsonPath("$.petName").value(DEFAULT_PET_NAME.toString()))
            .andExpect(jsonPath("$.petCategory").value(DEFAULT_PET_CATEGORY.toString()))
            .andExpect(jsonPath("$.petCategoryId").value(DEFAULT_PET_CATEGORY_ID.toString()))
            .andExpect(jsonPath("$.place").value(DEFAULT_PLACE))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME.getTime()));
    }

    @Test
    public void getNonExistingRaceResults() throws Exception {
        // Get the raceResults
        restRaceResultsMockMvc.perform(get("/api/race-results/{id}", UUID.randomUUID().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateRaceResults() throws Exception {
        // Initialize the database
        raceResultsRepository.save(raceResults);
        int databaseSizeBeforeUpdate = raceResultsRepository.findAll().size();

        // Update the raceResults
        RaceResults updatedRaceResults = new RaceResults();
        updatedRaceResults.setId(raceResults.getId());
        updatedRaceResults.setPetId(UPDATED_PET_ID);
        updatedRaceResults.setPetName(UPDATED_PET_NAME);
        updatedRaceResults.setPetCategory(UPDATED_PET_CATEGORY);
        updatedRaceResults.setPetCategoryId(UPDATED_PET_CATEGORY_ID);
        updatedRaceResults.setPlace(UPDATED_PLACE);
        updatedRaceResults.setTime(UPDATED_TIME);

        restRaceResultsMockMvc.perform(put("/api/race-results")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRaceResults)))
                .andExpect(status().isOk());

        // Validate the RaceResults in the database
        List<RaceResults> raceResults = raceResultsRepository.findAll();
        assertThat(raceResults).hasSize(databaseSizeBeforeUpdate);
        RaceResults testRaceResults = raceResults.get(raceResults.size() - 1);
        assertThat(testRaceResults.getPetId()).isEqualTo(UPDATED_PET_ID);
        assertThat(testRaceResults.getPetName()).isEqualTo(UPDATED_PET_NAME);
        assertThat(testRaceResults.getPetCategory()).isEqualTo(UPDATED_PET_CATEGORY);
        assertThat(testRaceResults.getPetCategoryId()).isEqualTo(UPDATED_PET_CATEGORY_ID);
        assertThat(testRaceResults.getPlace()).isEqualTo(UPDATED_PLACE);
        assertThat(testRaceResults.getTime()).isEqualTo(UPDATED_TIME);
    }

    @Test
    public void deleteRaceResults() throws Exception {
        // Initialize the database
        raceResultsRepository.save(raceResults);
        int databaseSizeBeforeDelete = raceResultsRepository.findAll().size();

        // Get the raceResults
        restRaceResultsMockMvc.perform(delete("/api/race-results/{id}", raceResults.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<RaceResults> raceResults = raceResultsRepository.findAll();
        assertThat(raceResults).hasSize(databaseSizeBeforeDelete - 1);
    }
}
