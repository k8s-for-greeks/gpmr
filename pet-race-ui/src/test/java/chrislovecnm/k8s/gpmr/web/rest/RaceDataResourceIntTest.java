package chrislovecnm.k8s.gpmr.web.rest;

import chrislovecnm.k8s.gpmr.AbstractCassandraTest;
import chrislovecnm.k8s.gpmr.GpmrApp;
import chrislovecnm.k8s.gpmr.domain.RaceData;
import chrislovecnm.k8s.gpmr.repository.RaceDataRepository;

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
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the RaceDataResource REST controller.
 *
 * @see RaceDataResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GpmrApp.class)
@WebAppConfiguration
@IntegrationTest
public class RaceDataResourceIntTest extends AbstractCassandraTest {


    private static final UUID DEFAULT_RACE_DATA_ID = UUID.randomUUID();
    private static final UUID UPDATED_RACE_DATA_ID = UUID.randomUUID();

    private static final UUID DEFAULT_PET_ID = UUID.randomUUID();
    private static final UUID UPDATED_PET_ID = UUID.randomUUID();

    private static final UUID DEFAULT_RACE_ID = UUID.randomUUID();
    private static final UUID UPDATED_RACE_ID = UUID.randomUUID();
    private static final String DEFAULT_PET_NAME = "AAAAA";
    private static final String UPDATED_PET_NAME = "BBBBB";
    private static final String DEFAULT_PET_CATEGORY_NAME = "AAAAA";
    private static final String UPDATED_PET_CATEGORY_NAME = "BBBBB";

    private static final UUID DEFAULT_PET_CATEGORY_ID = UUID.randomUUID();
    private static final UUID UPDATED_PET_CATEGORY_ID = UUID.randomUUID();

    private static final Integer DEFAULT_INTERVAL = 1;
    private static final Integer UPDATED_INTERVAL = 2;

    private static final Integer DEFAULT_RUNNER_POSITION = 1;
    private static final Integer UPDATED_RUNNER_POSITION = 2;

    private static final BigDecimal DEFAULT_RUNNER_DISTANCE = BigDecimal.valueOf(1D);
    private static final BigDecimal UPDATED_RUNNER_DISTANCE = BigDecimal.valueOf(2D);

    private static final Date DEFAULT_START_TIME = new Date();
    private static final Date UPDATED_START_TIME = new Date();

    private static final Boolean DEFAULT_FINISHED = false;
    private static final Boolean UPDATED_FINISHED = true;

    private static final BigDecimal DEFAULT_RUNNER_PREVIOUS_DISTANCE = BigDecimal.valueOf(1D);
    private static final BigDecimal UPDATED_RUNNER_PREVIOUS_DISTANCE = BigDecimal.valueOf(2D);

    @Inject
    private RaceDataRepository raceDataRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRaceDataMockMvc;

    private RaceData raceData;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RaceDataResource raceDataResource = new RaceDataResource();
        ReflectionTestUtils.setField(raceDataResource, "raceDataRepository", raceDataRepository);
        this.restRaceDataMockMvc = MockMvcBuilders.standaloneSetup(raceDataResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        raceDataRepository.deleteAll();
        raceData = new RaceData();
        raceData.setPetId(DEFAULT_PET_ID);
        raceData.setRaceId(DEFAULT_RACE_ID);
        raceData.setPetName(DEFAULT_PET_NAME);
        raceData.setPetCategoryName(DEFAULT_PET_CATEGORY_NAME);
        raceData.setPetCategoryId(DEFAULT_PET_CATEGORY_ID);
        raceData.setInterval(DEFAULT_INTERVAL);
        raceData.setRunnerPosition(DEFAULT_RUNNER_POSITION);
        raceData.setRunnerDistance(DEFAULT_RUNNER_DISTANCE);
        raceData.setStartTime(DEFAULT_START_TIME);
        raceData.setFinished(DEFAULT_FINISHED);
        raceData.setRunnerPreviousDistance(DEFAULT_RUNNER_PREVIOUS_DISTANCE);
    }

    @Test
    public void createRaceData() throws Exception {
        int databaseSizeBeforeCreate = raceDataRepository.findAll().size();

        // Create the RaceData

        raceData.setRaceDataId(null);
        restRaceDataMockMvc.perform(post("/api/race-data")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(raceData)))
                .andExpect(status().isCreated());

        // Validate the RaceData in the database
        List<RaceData> raceData = raceDataRepository.findAll();
        assertThat(raceData).hasSize(databaseSizeBeforeCreate + 1);
        RaceData testRaceData = raceData.get(raceData.size() - 1);
        assertThat(testRaceData.getPetId()).isEqualTo(DEFAULT_PET_ID);
        assertThat(testRaceData.getRaceId()).isEqualTo(DEFAULT_RACE_ID);
        assertThat(testRaceData.getPetName()).isEqualTo(DEFAULT_PET_NAME);
        assertThat(testRaceData.getPetCategoryName()).isEqualTo(DEFAULT_PET_CATEGORY_NAME);
        assertThat(testRaceData.getPetCategoryId()).isEqualTo(DEFAULT_PET_CATEGORY_ID);
        assertThat(testRaceData.getInterval()).isEqualTo(DEFAULT_INTERVAL);
        assertThat(testRaceData.getRunnerPosition()).isEqualTo(DEFAULT_RUNNER_POSITION);
        assertThat(testRaceData.getRunnerDistance()).isEqualTo(DEFAULT_RUNNER_DISTANCE);
        assertThat(testRaceData.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testRaceData.isFinished()).isEqualTo(DEFAULT_FINISHED);
        assertThat(testRaceData.getRunnerPreviousDistance()).isEqualTo(DEFAULT_RUNNER_PREVIOUS_DISTANCE);
        this.raceData.setRaceDataId(DEFAULT_RACE_DATA_ID);
    }

    @Test
    public void getAllRaceData() throws Exception {
        // Initialize the database
        raceDataRepository.save(raceData);

        // Get all the raceData
        restRaceDataMockMvc.perform(get("/api/race-data?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].raceDataId").value(hasItem(raceData.getRaceDataId().toString())))
                .andExpect(jsonPath("$.[*].petId").value(hasItem(DEFAULT_PET_ID.toString())))
                .andExpect(jsonPath("$.[*].raceId").value(hasItem(DEFAULT_RACE_ID.toString())))
                .andExpect(jsonPath("$.[*].petName").value(hasItem(DEFAULT_PET_NAME.toString())))
                .andExpect(jsonPath("$.[*].petCategoryName").value(hasItem(DEFAULT_PET_CATEGORY_NAME.toString())))
                .andExpect(jsonPath("$.[*].petCategoryId").value(hasItem(DEFAULT_PET_CATEGORY_ID.toString())))
                .andExpect(jsonPath("$.[*].interval").value(hasItem(DEFAULT_INTERVAL)))
                .andExpect(jsonPath("$.[*].runnerPosition").value(hasItem(DEFAULT_RUNNER_POSITION)))
                .andExpect(jsonPath("$.[*].runnerDistance").value(hasItem(DEFAULT_RUNNER_DISTANCE.doubleValue())))
                .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.getTime())))
                .andExpect(jsonPath("$.[*].finished").value(hasItem(DEFAULT_FINISHED.booleanValue())))
                .andExpect(jsonPath("$.[*].runnerPreviousDistance").value(hasItem(DEFAULT_RUNNER_PREVIOUS_DISTANCE.doubleValue())));
    }

    @Test
    public void getRaceData() throws Exception {
        // Initialize the database
        raceDataRepository.save(raceData);

        // Get the raceData
        restRaceDataMockMvc.perform(get("/api/race-data/{id}", raceData.getRaceDataId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.raceDataId").value(raceData.getRaceDataId().toString()))
            .andExpect(jsonPath("$.petId").value(DEFAULT_PET_ID.toString()))
            .andExpect(jsonPath("$.raceId").value(DEFAULT_RACE_ID.toString()))
            .andExpect(jsonPath("$.petName").value(DEFAULT_PET_NAME.toString()))
            .andExpect(jsonPath("$.petCategoryName").value(DEFAULT_PET_CATEGORY_NAME.toString()))
            .andExpect(jsonPath("$.petCategoryId").value(DEFAULT_PET_CATEGORY_ID.toString()))
            .andExpect(jsonPath("$.interval").value(DEFAULT_INTERVAL))
            .andExpect(jsonPath("$.runnerPosition").value(DEFAULT_RUNNER_POSITION))
            .andExpect(jsonPath("$.runnerDistance").value(DEFAULT_RUNNER_DISTANCE.doubleValue()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.getTime()))
            .andExpect(jsonPath("$.finished").value(DEFAULT_FINISHED.booleanValue()))
            .andExpect(jsonPath("$.runnerPreviousDistance").value(DEFAULT_RUNNER_PREVIOUS_DISTANCE.doubleValue()));
    }

    @Test
    public void getNonExistingRaceData() throws Exception {
        // Get the raceData
        restRaceDataMockMvc.perform(get("/api/race-data/{id}", UUID.randomUUID().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateRaceData() throws Exception {
        // Initialize the database
        raceDataRepository.save(raceData);
        int databaseSizeBeforeUpdate = raceDataRepository.findAll().size();

        // Update the raceData
        RaceData updatedRaceData = new RaceData();
        updatedRaceData.setRaceDataId(raceData.getRaceDataId());
        updatedRaceData.setPetId(UPDATED_PET_ID);
        updatedRaceData.setRaceId(UPDATED_RACE_ID);
        updatedRaceData.setPetName(UPDATED_PET_NAME);
        updatedRaceData.setPetCategoryName(UPDATED_PET_CATEGORY_NAME);
        updatedRaceData.setPetCategoryId(UPDATED_PET_CATEGORY_ID);
        updatedRaceData.setInterval(UPDATED_INTERVAL);
        updatedRaceData.setRunnerPosition(UPDATED_RUNNER_POSITION);
        updatedRaceData.setRunnerDistance(UPDATED_RUNNER_DISTANCE);
        updatedRaceData.setStartTime(UPDATED_START_TIME);
        updatedRaceData.setFinished(UPDATED_FINISHED);
        updatedRaceData.setRunnerPreviousDistance(UPDATED_RUNNER_PREVIOUS_DISTANCE);

        restRaceDataMockMvc.perform(put("/api/race-data")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRaceData)))
                .andExpect(status().isOk());

        // Validate the RaceData in the database
        List<RaceData> raceDatas = raceDataRepository.findAll();
        assertThat(raceDatas).hasSize(databaseSizeBeforeUpdate);
        RaceData testRaceData = raceDatas.get(raceDatas.size() - 1);
        assertThat(testRaceData.getRaceDataId()).isEqualTo(raceData.getRaceDataId());
        assertThat(testRaceData.getPetId()).isEqualTo(UPDATED_PET_ID);
        assertThat(testRaceData.getRaceId()).isEqualTo(UPDATED_RACE_ID);
        assertThat(testRaceData.getPetName()).isEqualTo(UPDATED_PET_NAME);
        assertThat(testRaceData.getPetCategoryName()).isEqualTo(UPDATED_PET_CATEGORY_NAME);
        assertThat(testRaceData.getPetCategoryId()).isEqualTo(UPDATED_PET_CATEGORY_ID);
        assertThat(testRaceData.getInterval()).isEqualTo(UPDATED_INTERVAL);
        assertThat(testRaceData.getRunnerPosition()).isEqualTo(UPDATED_RUNNER_POSITION);
        assertThat(testRaceData.getRunnerDistance()).isEqualTo(UPDATED_RUNNER_DISTANCE);
        assertThat(testRaceData.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testRaceData.isFinished()).isEqualTo(UPDATED_FINISHED);
        assertThat(testRaceData.getRunnerPreviousDistance()).isEqualTo(UPDATED_RUNNER_PREVIOUS_DISTANCE);
    }

    @Test
    public void deleteRaceData() throws Exception {
        // Initialize the database
        raceDataRepository.save(raceData);
        int databaseSizeBeforeDelete = raceDataRepository.findAll().size();

        // Get the raceData
        restRaceDataMockMvc.perform(delete("/api/race-data/{id}", raceData.getRaceDataId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<RaceData> raceData = raceDataRepository.findAll();
        assertThat(raceData).hasSize(databaseSizeBeforeDelete - 1);
    }
}
