package chrislovecnm.k8s.gpmr.web.rest;

import chrislovecnm.k8s.gpmr.AbstractCassandraTest;
import chrislovecnm.k8s.gpmr.GpmrApp;
import chrislovecnm.k8s.gpmr.domain.RaceResult;
import chrislovecnm.k8s.gpmr.repository.RaceResultRepository;

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
 * Test class for the RaceResultResource REST controller.
 *
 * @see RaceResultResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GpmrApp.class)
@WebAppConfiguration
@IntegrationTest
public class RaceResultResourceIntTest extends AbstractCassandraTest {


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
    private RaceResultRepository raceResultRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRaceResultMockMvc;

    private RaceResult raceResult;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RaceResultResource raceResultResource = new RaceResultResource();
        ReflectionTestUtils.setField(raceResultResource, "raceResultRepository", raceResultRepository);
        this.restRaceResultMockMvc = MockMvcBuilders.standaloneSetup(raceResultResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        raceResultRepository.deleteAll();
        raceResult = new RaceResult();
        raceResult.setPetId(DEFAULT_PET_ID);
        raceResult.setPetName(DEFAULT_PET_NAME);
        raceResult.setPetCategory(DEFAULT_PET_CATEGORY);
        raceResult.setPetCategoryId(DEFAULT_PET_CATEGORY_ID);
        raceResult.setPlace(DEFAULT_PLACE);
        raceResult.setTime(DEFAULT_TIME);
    }

    @Test
    public void createRaceResult() throws Exception {
        int databaseSizeBeforeCreate = raceResultRepository.findAll().size();

        // Create the RaceResult

        restRaceResultMockMvc.perform(post("/api/race-results")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(raceResult)))
                .andExpect(status().isCreated());

        // Validate the RaceResult in the database
        List<RaceResult> raceResults = raceResultRepository.findAll();
        assertThat(raceResults).hasSize(databaseSizeBeforeCreate + 1);
        RaceResult testRaceResult = raceResults.get(raceResults.size() - 1);
        assertThat(testRaceResult.getPetId()).isEqualTo(DEFAULT_PET_ID);
        assertThat(testRaceResult.getPetName()).isEqualTo(DEFAULT_PET_NAME);
        assertThat(testRaceResult.getPetCategory()).isEqualTo(DEFAULT_PET_CATEGORY);
        assertThat(testRaceResult.getPetCategoryId()).isEqualTo(DEFAULT_PET_CATEGORY_ID);
        assertThat(testRaceResult.getPlace()).isEqualTo(DEFAULT_PLACE);
        assertThat(testRaceResult.getTime()).isEqualTo(DEFAULT_TIME);
    }

    @Test
    public void getAllRaceResults() throws Exception {
        // Initialize the database
        raceResultRepository.save(raceResult);

        // Get all the raceResults
        restRaceResultMockMvc.perform(get("/api/race-results?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(raceResult.getId().toString())))
                .andExpect(jsonPath("$.[*].petId").value(hasItem(DEFAULT_PET_ID.toString())))
                .andExpect(jsonPath("$.[*].petName").value(hasItem(DEFAULT_PET_NAME.toString())))
                .andExpect(jsonPath("$.[*].petCategory").value(hasItem(DEFAULT_PET_CATEGORY.toString())))
                .andExpect(jsonPath("$.[*].petCategoryId").value(hasItem(DEFAULT_PET_CATEGORY_ID.toString())))
                .andExpect(jsonPath("$.[*].place").value(hasItem(DEFAULT_PLACE)))
                .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.getTime())));
    }

    @Test
    public void getRaceResult() throws Exception {
        // Initialize the database
        raceResultRepository.save(raceResult);

        // Get the raceResult
        restRaceResultMockMvc.perform(get("/api/race-results/{id}", raceResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(raceResult.getId().toString()))
            .andExpect(jsonPath("$.petId").value(DEFAULT_PET_ID.toString()))
            .andExpect(jsonPath("$.petName").value(DEFAULT_PET_NAME.toString()))
            .andExpect(jsonPath("$.petCategory").value(DEFAULT_PET_CATEGORY.toString()))
            .andExpect(jsonPath("$.petCategoryId").value(DEFAULT_PET_CATEGORY_ID.toString()))
            .andExpect(jsonPath("$.place").value(DEFAULT_PLACE))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME.getTime()));
    }

    @Test
    public void getNonExistingRaceResult() throws Exception {
        // Get the raceResult
        restRaceResultMockMvc.perform(get("/api/race-results/{id}", UUID.randomUUID().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateRaceResult() throws Exception {
        // Initialize the database
        raceResultRepository.save(raceResult);
        int databaseSizeBeforeUpdate = raceResultRepository.findAll().size();

        // Update the raceResult
        RaceResult updatedRaceResult = new RaceResult();
        updatedRaceResult.setId(raceResult.getId());
        updatedRaceResult.setPetId(UPDATED_PET_ID);
        updatedRaceResult.setPetName(UPDATED_PET_NAME);
        updatedRaceResult.setPetCategory(UPDATED_PET_CATEGORY);
        updatedRaceResult.setPetCategoryId(UPDATED_PET_CATEGORY_ID);
        updatedRaceResult.setPlace(UPDATED_PLACE);
        updatedRaceResult.setTime(UPDATED_TIME);

        restRaceResultMockMvc.perform(put("/api/race-results")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRaceResult)))
                .andExpect(status().isOk());

        // Validate the RaceResult in the database
        List<RaceResult> raceResults = raceResultRepository.findAll();
        assertThat(raceResults).hasSize(databaseSizeBeforeUpdate);
        RaceResult testRaceResult = raceResults.get(raceResults.size() - 1);
        assertThat(testRaceResult.getPetId()).isEqualTo(UPDATED_PET_ID);
        assertThat(testRaceResult.getPetName()).isEqualTo(UPDATED_PET_NAME);
        assertThat(testRaceResult.getPetCategory()).isEqualTo(UPDATED_PET_CATEGORY);
        assertThat(testRaceResult.getPetCategoryId()).isEqualTo(UPDATED_PET_CATEGORY_ID);
        assertThat(testRaceResult.getPlace()).isEqualTo(UPDATED_PLACE);
        assertThat(testRaceResult.getTime()).isEqualTo(UPDATED_TIME);
    }

    @Test
    public void deleteRaceResult() throws Exception {
        // Initialize the database
        raceResultRepository.save(raceResult);
        int databaseSizeBeforeDelete = raceResultRepository.findAll().size();

        // Get the raceResult
        restRaceResultMockMvc.perform(delete("/api/race-results/{id}", raceResult.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<RaceResult> raceResults = raceResultRepository.findAll();
        assertThat(raceResults).hasSize(databaseSizeBeforeDelete - 1);
    }
}
