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
import java.math.BigDecimal;
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


    private static final UUID DEFAULT_RACE_RESULT_ID = UUID.randomUUID();
    private static final UUID UPDATED_RACE_RESULT_ID = UUID.randomUUID();

    private static final UUID DEFAULT_RACE_ID = UUID.randomUUID();
    private static final UUID UPDATED_RACE_ID = UUID.randomUUID();

    private static final UUID DEFAULT_PET_CATEGORY_ID = UUID.randomUUID();
    private static final UUID UPDATED_PET_CATEGORY_ID = UUID.randomUUID();

    private static final UUID DEFAULT_RACE_PARTICIPANT_ID = UUID.randomUUID();
    private static final UUID UPDATED_RACE_PARTICIPANT_ID = UUID.randomUUID();
    private static final String DEFAULT_PET_NAME = "AAAAA";
    private static final String UPDATED_PET_NAME = "BBBBB";
    private static final String DEFAULT_PET_TYPE = "AAAAA";
    private static final String UPDATED_PET_TYPE = "BBBBB";

    private static final String DEFAULT_PET_COLOR = "RED";
    private static final String UPDATED_PET_COLOR = "BLUE";
    private static final String DEFAULT_PET_CATEGORY_NAME = "AAAAA";
    private static final String UPDATED_PET_CATEGORY_NAME = "BBBBB";

    private static final Integer DEFAULT_FINISH_POSITION = 1;
    private static final Integer UPDATED_FINISH_POSITION = 2;

    private static final BigDecimal DEFAULT_FINISH_TIME = BigDecimal.valueOf(1D);
    private static final BigDecimal UPDATED_FINISH_TIME = BigDecimal.valueOf(2D);

    private static final Date DEFAULT_START_TIME = new Date();
    private static final Date UPDATED_START_TIME = new Date();

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
        raceResult.setRaceResultId(DEFAULT_RACE_RESULT_ID);
        raceResult.setRaceId(DEFAULT_RACE_ID);
        raceResult.setPetCategoryId(DEFAULT_PET_CATEGORY_ID);
        raceResult.setRaceParticipantId(DEFAULT_RACE_PARTICIPANT_ID);
        raceResult.setPetName(DEFAULT_PET_NAME);
        raceResult.setPetType(DEFAULT_PET_TYPE);
        raceResult.setPetColor(DEFAULT_PET_COLOR);
        raceResult.setPetCategoryName(DEFAULT_PET_CATEGORY_NAME);
        raceResult.setFinishPosition(DEFAULT_FINISH_POSITION);
        raceResult.setFinishTime(DEFAULT_FINISH_TIME);
        raceResult.setStartTime(DEFAULT_START_TIME);
    }

    @Test
    public void createRaceResult() throws Exception {
        int databaseSizeBeforeCreate = raceResultRepository.findAll().size();

        // Create the RaceResult

        raceResult.setRaceResultId(null);
        restRaceResultMockMvc.perform(post("/api/race-results")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(raceResult)))
                .andExpect(status().isCreated());

        // Validate the RaceResult in the database
        List<RaceResult> raceResults = raceResultRepository.findAll();
        assertThat(raceResults).hasSize(databaseSizeBeforeCreate + 1);
        RaceResult testRaceResult = raceResults.get(raceResults.size() - 1);
        assertThat(testRaceResult.getRaceId()).isEqualTo(DEFAULT_RACE_ID);
        assertThat(testRaceResult.getPetCategoryId()).isEqualTo(DEFAULT_PET_CATEGORY_ID);
        assertThat(testRaceResult.getRaceParticipantId()).isEqualTo(DEFAULT_RACE_PARTICIPANT_ID);
        assertThat(testRaceResult.getPetName()).isEqualTo(DEFAULT_PET_NAME);
        assertThat(testRaceResult.getPetType()).isEqualTo(DEFAULT_PET_TYPE);
        assertThat(testRaceResult.getPetColor()).isEqualTo(DEFAULT_PET_COLOR);
        assertThat(testRaceResult.getPetCategoryName()).isEqualTo(DEFAULT_PET_CATEGORY_NAME);
        assertThat(testRaceResult.getFinishPosition()).isEqualTo(DEFAULT_FINISH_POSITION);
        assertThat(testRaceResult.getFinishTime()).isEqualTo(DEFAULT_FINISH_TIME);
        assertThat(testRaceResult.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        raceResult.setRaceResultId(DEFAULT_RACE_RESULT_ID);
    }

    @Test
    public void getAllRaceResults() throws Exception {
        // Initialize the database
        raceResultRepository.save(raceResult);

        // Get all the raceResults
        restRaceResultMockMvc.perform(get("/api/race-results?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].raceResultId").value(hasItem(raceResult.getRaceResultId().toString())))
                .andExpect(jsonPath("$.[*].raceId").value(hasItem(DEFAULT_RACE_ID.toString())))
                .andExpect(jsonPath("$.[*].petCategoryId").value(hasItem(DEFAULT_PET_CATEGORY_ID.toString())))
                .andExpect(jsonPath("$.[*].raceParticipantId").value(hasItem(DEFAULT_RACE_PARTICIPANT_ID.toString())))
                .andExpect(jsonPath("$.[*].petName").value(hasItem(DEFAULT_PET_NAME.toString())))
                .andExpect(jsonPath("$.[*].petType").value(hasItem(DEFAULT_PET_TYPE.toString())))
                .andExpect(jsonPath("$.[*].petColor").value(hasItem(DEFAULT_PET_COLOR.toString())))
                .andExpect(jsonPath("$.[*].petCategoryName").value(hasItem(DEFAULT_PET_CATEGORY_NAME.toString())))
                .andExpect(jsonPath("$.[*].finishPosition").value(hasItem(DEFAULT_FINISH_POSITION)))
                .andExpect(jsonPath("$.[*].finishTime").value(hasItem(DEFAULT_FINISH_TIME.doubleValue())))
                .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.getTime())));
    }

    @Test
    public void getRaceResult() throws Exception {
        // Initialize the database
        raceResultRepository.save(raceResult);

        // Get the raceResult
        restRaceResultMockMvc.perform(get("/api/race-results/{id}", raceResult.getRaceResultId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.raceResultId").value(raceResult.getRaceResultId().toString()))
            .andExpect(jsonPath("$.raceId").value(DEFAULT_RACE_ID.toString()))
            .andExpect(jsonPath("$.petCategoryId").value(DEFAULT_PET_CATEGORY_ID.toString()))
            .andExpect(jsonPath("$.raceParticipantId").value(DEFAULT_RACE_PARTICIPANT_ID.toString()))
            .andExpect(jsonPath("$.petName").value(DEFAULT_PET_NAME.toString()))
            .andExpect(jsonPath("$.petType").value(DEFAULT_PET_TYPE.toString()))
            .andExpect(jsonPath("$.petColor").value(DEFAULT_PET_COLOR.toString()))
            .andExpect(jsonPath("$.petCategoryName").value(DEFAULT_PET_CATEGORY_NAME.toString()))
            .andExpect(jsonPath("$.finishPosition").value(DEFAULT_FINISH_POSITION))
            .andExpect(jsonPath("$.finishTime").value(DEFAULT_FINISH_TIME.doubleValue()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.getTime()));
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
        updatedRaceResult.setRaceResultId(raceResult.getRaceResultId());
        updatedRaceResult.setRaceId(UPDATED_RACE_ID);
        updatedRaceResult.setPetCategoryId(UPDATED_PET_CATEGORY_ID);
        updatedRaceResult.setRaceParticipantId(UPDATED_RACE_PARTICIPANT_ID);
        updatedRaceResult.setPetName(UPDATED_PET_NAME);
        updatedRaceResult.setPetType(UPDATED_PET_TYPE);
        updatedRaceResult.setPetColor(UPDATED_PET_COLOR);
        updatedRaceResult.setPetCategoryName(UPDATED_PET_CATEGORY_NAME);
        updatedRaceResult.setFinishPosition(UPDATED_FINISH_POSITION);
        updatedRaceResult.setFinishTime(UPDATED_FINISH_TIME);
        updatedRaceResult.setStartTime(UPDATED_START_TIME);

        restRaceResultMockMvc.perform(put("/api/race-results")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRaceResult)))
                .andExpect(status().isOk());

        // Validate the RaceResult in the database
        List<RaceResult> raceResults = raceResultRepository.findAll();
        assertThat(raceResults).hasSize(databaseSizeBeforeUpdate);
        RaceResult testRaceResult = raceResults.get(raceResults.size() - 1);
        assertThat(testRaceResult.getRaceResultId()).isEqualTo(raceResult.getRaceResultId());
        assertThat(testRaceResult.getRaceId()).isEqualTo(UPDATED_RACE_ID);
        assertThat(testRaceResult.getPetCategoryId()).isEqualTo(UPDATED_PET_CATEGORY_ID);
        assertThat(testRaceResult.getRaceParticipantId()).isEqualTo(UPDATED_RACE_PARTICIPANT_ID);
        assertThat(testRaceResult.getPetName()).isEqualTo(UPDATED_PET_NAME);
        assertThat(testRaceResult.getPetType()).isEqualTo(UPDATED_PET_TYPE);
        assertThat(testRaceResult.getPetColor()).isEqualTo(UPDATED_PET_COLOR);
        assertThat(testRaceResult.getPetCategoryName()).isEqualTo(UPDATED_PET_CATEGORY_NAME);
        assertThat(testRaceResult.getFinishPosition()).isEqualTo(UPDATED_FINISH_POSITION);
        assertThat(testRaceResult.getFinishTime()).isEqualTo(UPDATED_FINISH_TIME);
        assertThat(testRaceResult.getStartTime()).isEqualTo(UPDATED_START_TIME);
    }

    @Test
    public void deleteRaceResult() throws Exception {
        // Initialize the database
        raceResultRepository.save(raceResult);
        int databaseSizeBeforeDelete = raceResultRepository.findAll().size();

        // Get the raceResult
        restRaceResultMockMvc.perform(delete("/api/race-results/{id}", raceResult.getRaceResultId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<RaceResult> raceResults = raceResultRepository.findAll();
        assertThat(raceResults).hasSize(databaseSizeBeforeDelete - 1);
    }
}
