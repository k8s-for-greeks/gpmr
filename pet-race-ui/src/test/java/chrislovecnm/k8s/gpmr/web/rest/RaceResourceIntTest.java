package chrislovecnm.k8s.gpmr.web.rest;

import chrislovecnm.k8s.gpmr.AbstractCassandraTest;
import chrislovecnm.k8s.gpmr.GpmrApp;
import chrislovecnm.k8s.gpmr.domain.Race;
import chrislovecnm.k8s.gpmr.repository.RaceRepository;

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
 * Test class for the RaceResource REST controller.
 *
 * @see RaceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GpmrApp.class)
@WebAppConfiguration
@IntegrationTest
public class RaceResourceIntTest extends AbstractCassandraTest {


    private static final Integer DEFAULT_NUM_OF_PETS = 1;
    private static final Integer UPDATED_NUM_OF_PETS = 2;

    private static final Integer DEFAULT_LENGTH = 1;
    private static final Integer UPDATED_LENGTH = 2;

    private static final Integer DEFAULT_NUM_OF_SAMPLES = 1;
    private static final Integer UPDATED_NUM_OF_SAMPLES = 2;

    private static final UUID DEFAULT_WINNER_ID = UUID.randomUUID();
    private static final UUID UPDATED_WINNER_ID = UUID.randomUUID();
    private static final String DEFAULT_WINNER_NAME = "AAAAA";
    private static final String UPDATED_WINNER_NAME = "BBBBB";
    private static final String DEFAULT_WINNNER_PET_CATEGORY = "AAAAA";
    private static final String UPDATED_WINNNER_PET_CATEGORY = "BBBBB";

    private static final Date DEFAULT_START_TIME = new Date();
    private static final Date UPDATED_START_TIME = new Date();

    private static final Date DEFAULT_END_TIME = new Date();
    private static final Date UPDATED_END_TIME = new Date();

    @Inject
    private RaceRepository raceRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRaceMockMvc;

    private Race race;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RaceResource raceResource = new RaceResource();
        ReflectionTestUtils.setField(raceResource, "raceRepository", raceRepository);
        this.restRaceMockMvc = MockMvcBuilders.standaloneSetup(raceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        raceRepository.deleteAll();
        race = new Race();
        race.setNumOfPets(DEFAULT_NUM_OF_PETS);
        race.setLength(DEFAULT_LENGTH);
        race.setNumOfSamples(DEFAULT_NUM_OF_SAMPLES);
        race.setWinnerId(DEFAULT_WINNER_ID);
        race.setWinnerName(DEFAULT_WINNER_NAME);
        race.setWinnnerPetCategory(DEFAULT_WINNNER_PET_CATEGORY);
        race.setStartTime(DEFAULT_START_TIME);
        race.setEndTime(DEFAULT_END_TIME);
    }

    @Test
    public void createRace() throws Exception {
        int databaseSizeBeforeCreate = raceRepository.findAll().size();

        // Create the Race

        restRaceMockMvc.perform(post("/api/races")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(race)))
                .andExpect(status().isCreated());

        // Validate the Race in the database
        List<Race> races = raceRepository.findAll();
        assertThat(races).hasSize(databaseSizeBeforeCreate + 1);
        Race testRace = races.get(races.size() - 1);
        assertThat(testRace.getNumOfPets()).isEqualTo(DEFAULT_NUM_OF_PETS);
        assertThat(testRace.getLength()).isEqualTo(DEFAULT_LENGTH);
        assertThat(testRace.getNumOfSamples()).isEqualTo(DEFAULT_NUM_OF_SAMPLES);
        assertThat(testRace.getWinnerId()).isEqualTo(DEFAULT_WINNER_ID);
        assertThat(testRace.getWinnerName()).isEqualTo(DEFAULT_WINNER_NAME);
        assertThat(testRace.getWinnnerPetCategory()).isEqualTo(DEFAULT_WINNNER_PET_CATEGORY);
        assertThat(testRace.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testRace.getEndTime()).isEqualTo(DEFAULT_END_TIME);
    }

    @Test
    public void getAllRaces() throws Exception {
        // Initialize the database
        raceRepository.save(race);

        // Get all the races
        restRaceMockMvc.perform(get("/api/races?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(race.getId().toString())))
                .andExpect(jsonPath("$.[*].numOfPets").value(hasItem(DEFAULT_NUM_OF_PETS)))
                .andExpect(jsonPath("$.[*].length").value(hasItem(DEFAULT_LENGTH)))
                .andExpect(jsonPath("$.[*].numOfSamples").value(hasItem(DEFAULT_NUM_OF_SAMPLES)))
                .andExpect(jsonPath("$.[*].winnerId").value(hasItem(DEFAULT_WINNER_ID.toString())))
                .andExpect(jsonPath("$.[*].winnerName").value(hasItem(DEFAULT_WINNER_NAME.toString())))
                .andExpect(jsonPath("$.[*].winnnerPetCategory").value(hasItem(DEFAULT_WINNNER_PET_CATEGORY.toString())))
                .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.getTime())))
                .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.getTime())));
    }

    @Test
    public void getRace() throws Exception {
        // Initialize the database
        raceRepository.save(race);

        // Get the race
        restRaceMockMvc.perform(get("/api/races/{id}", race.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(race.getId().toString()))
            .andExpect(jsonPath("$.numOfPets").value(DEFAULT_NUM_OF_PETS))
            .andExpect(jsonPath("$.length").value(DEFAULT_LENGTH))
            .andExpect(jsonPath("$.numOfSamples").value(DEFAULT_NUM_OF_SAMPLES))
            .andExpect(jsonPath("$.winnerId").value(DEFAULT_WINNER_ID.toString()))
            .andExpect(jsonPath("$.winnerName").value(DEFAULT_WINNER_NAME.toString()))
            .andExpect(jsonPath("$.winnnerPetCategory").value(DEFAULT_WINNNER_PET_CATEGORY.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.getTime()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.getTime()));
    }

    @Test
    public void getNonExistingRace() throws Exception {
        // Get the race
        restRaceMockMvc.perform(get("/api/races/{id}", UUID.randomUUID().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateRace() throws Exception {
        // Initialize the database
        raceRepository.save(race);
        int databaseSizeBeforeUpdate = raceRepository.findAll().size();

        // Update the race
        Race updatedRace = new Race();
        updatedRace.setId(race.getId());
        updatedRace.setNumOfPets(UPDATED_NUM_OF_PETS);
        updatedRace.setLength(UPDATED_LENGTH);
        updatedRace.setNumOfSamples(UPDATED_NUM_OF_SAMPLES);
        updatedRace.setWinnerId(UPDATED_WINNER_ID);
        updatedRace.setWinnerName(UPDATED_WINNER_NAME);
        updatedRace.setWinnnerPetCategory(UPDATED_WINNNER_PET_CATEGORY);
        updatedRace.setStartTime(UPDATED_START_TIME);
        updatedRace.setEndTime(UPDATED_END_TIME);

        restRaceMockMvc.perform(put("/api/races")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRace)))
                .andExpect(status().isOk());

        // Validate the Race in the database
        List<Race> races = raceRepository.findAll();
        assertThat(races).hasSize(databaseSizeBeforeUpdate);
        Race testRace = races.get(races.size() - 1);
        assertThat(testRace.getNumOfPets()).isEqualTo(UPDATED_NUM_OF_PETS);
        assertThat(testRace.getLength()).isEqualTo(UPDATED_LENGTH);
        assertThat(testRace.getNumOfSamples()).isEqualTo(UPDATED_NUM_OF_SAMPLES);
        assertThat(testRace.getWinnerId()).isEqualTo(UPDATED_WINNER_ID);
        assertThat(testRace.getWinnerName()).isEqualTo(UPDATED_WINNER_NAME);
        assertThat(testRace.getWinnnerPetCategory()).isEqualTo(UPDATED_WINNNER_PET_CATEGORY);
        assertThat(testRace.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testRace.getEndTime()).isEqualTo(UPDATED_END_TIME);
    }

    @Test
    public void deleteRace() throws Exception {
        // Initialize the database
        raceRepository.save(race);
        int databaseSizeBeforeDelete = raceRepository.findAll().size();

        // Get the race
        restRaceMockMvc.perform(delete("/api/races/{id}", race.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Race> races = raceRepository.findAll();
        assertThat(races).hasSize(databaseSizeBeforeDelete - 1);
    }
}
