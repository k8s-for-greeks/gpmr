package chrislovecnm.k8s.gpmr.web.rest;

import chrislovecnm.k8s.gpmr.AbstractCassandraTest;
import chrislovecnm.k8s.gpmr.GpmrApp;
import chrislovecnm.k8s.gpmr.domain.RaceParticipant;
import chrislovecnm.k8s.gpmr.repository.RaceParticipantRepository;

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
 * Test class for the RaceParticipantResource REST controller.
 *
 * @see RaceParticipantResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GpmrApp.class)
@WebAppConfiguration
@IntegrationTest
public class RaceParticipantResourceIntTest extends AbstractCassandraTest {


    private static final UUID DEFAULT_RACE_PARTICIPANT_ID = UUID.randomUUID();
    private static final UUID UPDATED_RACE_PARTICIPANT_ID = UUID.randomUUID();

    private static final UUID DEFAULT_PET_ID = UUID.randomUUID();
    private static final UUID UPDATED_PET_ID = UUID.randomUUID();

    private static final UUID DEFAULT_RACE_ID = UUID.randomUUID();
    private static final UUID UPDATED_RACE_ID = UUID.randomUUID();
    private static final String DEFAULT_PET_NAME = "AAAAA";
    private static final String UPDATED_PET_NAME = "BBBBB";

    private static final String DEFAULT_PET_COLOR = "BLUE";
    private static final String UPDATED_PET_COLOR = "RED";
    private static final String DEFAULT_PET_CATEGORY_NAME = "AAAAA";
    private static final String UPDATED_PET_CATEGORY_NAME = "BBBBB";

    private static final UUID DEFAULT_PET_CATEGORY_ID = UUID.randomUUID();
    private static final UUID UPDATED_PET_CATEGORY_ID = UUID.randomUUID();

    private static final Date DEFAULT_START_TIME = new Date();
    private static final Date UPDATED_START_TIME = new Date();

    private static final BigDecimal DEFAULT_FINISH_TIME = BigDecimal.valueOf(1D);
    private static final BigDecimal UPDATED_FINISH_TIME = BigDecimal.valueOf(2D);

    private static final Integer DEFAULT_FINISH_POSITION = 1;
    private static final Integer UPDATED_FINISH_POSITION = 2;

    private static final Boolean DEFAULT_FINISHED = false;
    private static final Boolean UPDATED_FINISHED = true;

    @Inject
    private RaceParticipantRepository raceParticipantRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRaceParticipantMockMvc;

    private RaceParticipant raceParticipant;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RaceParticipantResource raceParticipantResource = new RaceParticipantResource();
        ReflectionTestUtils.setField(raceParticipantResource, "raceParticipantRepository", raceParticipantRepository);
        this.restRaceParticipantMockMvc = MockMvcBuilders.standaloneSetup(raceParticipantResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        raceParticipantRepository.deleteAll();
        raceParticipant = new RaceParticipant();
        raceParticipant.setRaceParticipantId(DEFAULT_RACE_PARTICIPANT_ID);
        raceParticipant.setPetId(DEFAULT_PET_ID);
        raceParticipant.setRaceId(DEFAULT_RACE_ID);
        raceParticipant.setPetName(DEFAULT_PET_NAME);
        raceParticipant.setPetColor(DEFAULT_PET_COLOR);
        raceParticipant.setPetCategoryName(DEFAULT_PET_CATEGORY_NAME);
        raceParticipant.setPetCategoryId(DEFAULT_PET_CATEGORY_ID);
        raceParticipant.setStartTime(DEFAULT_START_TIME);
        raceParticipant.setFinishTime(DEFAULT_FINISH_TIME);
        raceParticipant.setFinishPosition(DEFAULT_FINISH_POSITION);
        raceParticipant.setFinished(DEFAULT_FINISHED);
    }

    @Test
    public void createRaceParticipant() throws Exception {
        int databaseSizeBeforeCreate = raceParticipantRepository.findAll().size();

        // Create the RaceParticipant
        raceParticipant.setRaceParticipantId(null);

        restRaceParticipantMockMvc.perform(post("/api/race-participants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(raceParticipant)))
                .andExpect(status().isCreated());

        // Validate the RaceParticipant in the database
        List<RaceParticipant> raceParticipants = raceParticipantRepository.findAll();
        assertThat(raceParticipants).hasSize(databaseSizeBeforeCreate + 1);
        RaceParticipant testRaceParticipant = raceParticipants.get(raceParticipants.size() - 1);
        assertThat(testRaceParticipant.getPetId()).isEqualTo(DEFAULT_PET_ID);
        assertThat(testRaceParticipant.getRaceId()).isEqualTo(DEFAULT_RACE_ID);
        assertThat(testRaceParticipant.getPetName()).isEqualTo(DEFAULT_PET_NAME);
        assertThat(testRaceParticipant.getPetColor()).isEqualTo(DEFAULT_PET_COLOR);
        assertThat(testRaceParticipant.getPetCategoryName()).isEqualTo(DEFAULT_PET_CATEGORY_NAME);
        assertThat(testRaceParticipant.getPetCategoryId()).isEqualTo(DEFAULT_PET_CATEGORY_ID);
        assertThat(testRaceParticipant.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testRaceParticipant.getFinishTime()).isEqualTo(DEFAULT_FINISH_TIME);
        assertThat(testRaceParticipant.getFinishPosition()).isEqualTo(DEFAULT_FINISH_POSITION);
        assertThat(testRaceParticipant.isFinished()).isEqualTo(DEFAULT_FINISHED);
        raceParticipant.setRaceParticipantId(DEFAULT_RACE_PARTICIPANT_ID);
    }

    @Test
    public void getAllRaceParticipants() throws Exception {
        // Initialize the database
        raceParticipantRepository.save(raceParticipant);

        // Get all the raceParticipants
        restRaceParticipantMockMvc.perform(get("/api/race-participants?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].raceParticipantId").value(hasItem(DEFAULT_RACE_PARTICIPANT_ID.toString())))
                .andExpect(jsonPath("$.[*].petId").value(hasItem(DEFAULT_PET_ID.toString())))
                .andExpect(jsonPath("$.[*].raceId").value(hasItem(DEFAULT_RACE_ID.toString())))
                .andExpect(jsonPath("$.[*].petName").value(hasItem(DEFAULT_PET_NAME.toString())))
                .andExpect(jsonPath("$.[*].petColor").value(hasItem(DEFAULT_PET_COLOR.toString())))
                .andExpect(jsonPath("$.[*].petCategoryName").value(hasItem(DEFAULT_PET_CATEGORY_NAME.toString())))
                .andExpect(jsonPath("$.[*].petCategoryId").value(hasItem(DEFAULT_PET_CATEGORY_ID.toString())))
                .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.getTime())))
                .andExpect(jsonPath("$.[*].finishTime").value(hasItem(DEFAULT_FINISH_TIME.doubleValue())))
                .andExpect(jsonPath("$.[*].finishPosition").value(hasItem(DEFAULT_FINISH_POSITION)))
                .andExpect(jsonPath("$.[*].finished").value(hasItem(DEFAULT_FINISHED.booleanValue())));
    }

    @Test
    public void getRaceParticipant() throws Exception {
        // Initialize the database
        raceParticipantRepository.save(raceParticipant);

        // Get the raceParticipant
        restRaceParticipantMockMvc.perform(get("/api/race-participants/{id}", raceParticipant.getRaceParticipantId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.raceParticipantId").value(DEFAULT_RACE_PARTICIPANT_ID.toString()))
            .andExpect(jsonPath("$.petId").value(DEFAULT_PET_ID.toString()))
            .andExpect(jsonPath("$.raceId").value(DEFAULT_RACE_ID.toString()))
            .andExpect(jsonPath("$.petName").value(DEFAULT_PET_NAME.toString()))
            .andExpect(jsonPath("$.petColor").value(DEFAULT_PET_COLOR.toString()))
            .andExpect(jsonPath("$.petCategoryName").value(DEFAULT_PET_CATEGORY_NAME.toString()))
            .andExpect(jsonPath("$.petCategoryId").value(DEFAULT_PET_CATEGORY_ID.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.getTime()))
            .andExpect(jsonPath("$.finishTime").value(DEFAULT_FINISH_TIME.doubleValue()))
            .andExpect(jsonPath("$.finishPosition").value(DEFAULT_FINISH_POSITION))
            .andExpect(jsonPath("$.finished").value(DEFAULT_FINISHED.booleanValue()));
    }

    @Test
    public void getNonExistingRaceParticipant() throws Exception {
        // Get the raceParticipant
        restRaceParticipantMockMvc.perform(get("/api/race-participants/{id}", UUID.randomUUID().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateRaceParticipant() throws Exception {
        // Initialize the database
        raceParticipantRepository.save(raceParticipant);
        int databaseSizeBeforeUpdate = raceParticipantRepository.findAll().size();

        // Update the raceParticipant
        RaceParticipant updatedRaceParticipant = new RaceParticipant();
        updatedRaceParticipant.setRaceParticipantId(raceParticipant.getRaceParticipantId());
        updatedRaceParticipant.setPetId(UPDATED_PET_ID);
        updatedRaceParticipant.setRaceId(UPDATED_RACE_ID);
        updatedRaceParticipant.setPetName(UPDATED_PET_NAME);
        updatedRaceParticipant.setPetColor(UPDATED_PET_COLOR);
        updatedRaceParticipant.setPetCategoryName(UPDATED_PET_CATEGORY_NAME);
        updatedRaceParticipant.setPetCategoryId(UPDATED_PET_CATEGORY_ID);
        updatedRaceParticipant.setStartTime(UPDATED_START_TIME);
        updatedRaceParticipant.setFinishTime(UPDATED_FINISH_TIME);
        updatedRaceParticipant.setFinishPosition(UPDATED_FINISH_POSITION);
        updatedRaceParticipant.setFinished(UPDATED_FINISHED);

        restRaceParticipantMockMvc.perform(put("/api/race-participants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRaceParticipant)))
                .andExpect(status().isOk());

        // Validate the RaceParticipant in the database
        List<RaceParticipant> raceParticipants = raceParticipantRepository.findAll();
        assertThat(raceParticipants).hasSize(databaseSizeBeforeUpdate);
        RaceParticipant testRaceParticipant = raceParticipants.get(raceParticipants.size() - 1);
        assertThat(testRaceParticipant.getRaceParticipantId()).isEqualTo(raceParticipant.getRaceParticipantId());
        assertThat(testRaceParticipant.getPetId()).isEqualTo(UPDATED_PET_ID);
        assertThat(testRaceParticipant.getRaceId()).isEqualTo(UPDATED_RACE_ID);
        assertThat(testRaceParticipant.getPetName()).isEqualTo(UPDATED_PET_NAME);
        assertThat(testRaceParticipant.getPetColor()).isEqualTo(UPDATED_PET_COLOR);
        assertThat(testRaceParticipant.getPetCategoryName()).isEqualTo(UPDATED_PET_CATEGORY_NAME);
        assertThat(testRaceParticipant.getPetCategoryId()).isEqualTo(UPDATED_PET_CATEGORY_ID);
        assertThat(testRaceParticipant.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testRaceParticipant.getFinishTime()).isEqualTo(UPDATED_FINISH_TIME);
        assertThat(testRaceParticipant.getFinishPosition()).isEqualTo(UPDATED_FINISH_POSITION);
        assertThat(testRaceParticipant.isFinished()).isEqualTo(UPDATED_FINISHED);
    }

    @Test
    public void deleteRaceParticipant() throws Exception {
        // Initialize the database
        raceParticipantRepository.save(raceParticipant);
        int databaseSizeBeforeDelete = raceParticipantRepository.findAll().size();

        // Get the raceParticipant
        restRaceParticipantMockMvc.perform(delete("/api/race-participants/{id}", raceParticipant.getRaceParticipantId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<RaceParticipant> raceParticipants = raceParticipantRepository.findAll();
        assertThat(raceParticipants).hasSize(databaseSizeBeforeDelete - 1);
    }
}
