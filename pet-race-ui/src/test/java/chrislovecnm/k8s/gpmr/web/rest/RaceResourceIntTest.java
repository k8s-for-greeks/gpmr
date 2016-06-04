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


    private static final UUID DEFAULT_RACE_ID = UUID.randomUUID();
    private static final UUID UPDATED_RACE_ID = UUID.randomUUID();

    private static final UUID DEFAULT_PET_CATEGORY_ID = UUID.randomUUID();
    private static final UUID UPDATED_PET_CATEGORY_ID = UUID.randomUUID();
    private static final String DEFAULT_PET_CATEGORY_NAME = "AAAAA";
    private static final String UPDATED_PET_CATEGORY_NAME = "BBBBB";

    private static final Integer DEFAULT_NUM_OF_PETS = 1;
    private static final Integer UPDATED_NUM_OF_PETS = 2;

    private static final Integer DEFAULT_LENGTH = 1;
    private static final Integer UPDATED_LENGTH = 2;
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final UUID DEFAULT_WINNER_ID = UUID.randomUUID();
    private static final UUID UPDATED_WINNER_ID = UUID.randomUUID();

    private static final Date DEFAULT_START_TIME = new Date();
    private static final Date UPDATED_START_TIME = new Date();

    private static final Float DEFAULT_BASE_SPEED = 1F;
    private static final Float UPDATED_BASE_SPEED = 2F;

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
        race.setRaceId(DEFAULT_RACE_ID);
        race.setPetCategoryId(DEFAULT_PET_CATEGORY_ID);
        race.setPetCategoryName(DEFAULT_PET_CATEGORY_NAME);
        race.setNumOfPets(DEFAULT_NUM_OF_PETS);
        race.setLength(DEFAULT_LENGTH);
        race.setDescription(DEFAULT_DESCRIPTION);
        race.setWinnerId(DEFAULT_WINNER_ID);
        race.setStartTime(DEFAULT_START_TIME);
        race.setBaseSpeed(DEFAULT_BASE_SPEED);
    }

    @Test
    public void createRace() throws Exception {
        int databaseSizeBeforeCreate = raceRepository.findAll().size();

        // Create the Race

        race.setRaceId(null);
        restRaceMockMvc.perform(post("/api/races")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(race)))
                .andExpect(status().isCreated());

        // Validate the Race in the database
        List<Race> races = raceRepository.findAll();
        assertThat(races).hasSize(databaseSizeBeforeCreate + 1);
        Race testRace = races.get(races.size() - 1);
        assertThat(testRace.getPetCategoryId()).isEqualTo(DEFAULT_PET_CATEGORY_ID);
        assertThat(testRace.getPetCategoryName()).isEqualTo(DEFAULT_PET_CATEGORY_NAME);
        assertThat(testRace.getNumOfPets()).isEqualTo(DEFAULT_NUM_OF_PETS);
        assertThat(testRace.getLength()).isEqualTo(DEFAULT_LENGTH);
        assertThat(testRace.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRace.getWinnerId()).isEqualTo(DEFAULT_WINNER_ID);
        assertThat(testRace.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testRace.getBaseSpeed()).isEqualTo(DEFAULT_BASE_SPEED);
        race.setRaceId(DEFAULT_RACE_ID);
    }

    @Test
    public void getAllRaces() throws Exception {
        // Initialize the database
        raceRepository.save(race);

        // Get all the races
        restRaceMockMvc.perform(get("/api/races?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].raceId").value(hasItem(DEFAULT_RACE_ID.toString())))
                .andExpect(jsonPath("$.[*].petCategoryId").value(hasItem(DEFAULT_PET_CATEGORY_ID.toString())))
                .andExpect(jsonPath("$.[*].petCategoryName").value(hasItem(DEFAULT_PET_CATEGORY_NAME.toString())))
                .andExpect(jsonPath("$.[*].numOfPets").value(hasItem(DEFAULT_NUM_OF_PETS)))
                .andExpect(jsonPath("$.[*].length").value(hasItem(DEFAULT_LENGTH)))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].winnerId").value(hasItem(DEFAULT_WINNER_ID.toString())))
                .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.getTime())))
                .andExpect(jsonPath("$.[*].baseSpeed").value(hasItem(DEFAULT_BASE_SPEED.doubleValue())));
    }

    @Test
    public void getRace() throws Exception {
        // Initialize the database
        raceRepository.save(race);

        // Get the race
        restRaceMockMvc.perform(get("/api/races/{id}", race.getRaceId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.raceId").value(race.getRaceId().toString()))
            .andExpect(jsonPath("$.petCategoryId").value(DEFAULT_PET_CATEGORY_ID.toString()))
            .andExpect(jsonPath("$.petCategoryName").value(DEFAULT_PET_CATEGORY_NAME.toString()))
            .andExpect(jsonPath("$.numOfPets").value(DEFAULT_NUM_OF_PETS))
            .andExpect(jsonPath("$.length").value(DEFAULT_LENGTH))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.winnerId").value(DEFAULT_WINNER_ID.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.getTime()))
            .andExpect(jsonPath("$.baseSpeed").value(DEFAULT_BASE_SPEED.doubleValue()));
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
        updatedRace.setRaceId(race.getRaceId());
        updatedRace.setPetCategoryId(UPDATED_PET_CATEGORY_ID);
        updatedRace.setPetCategoryName(UPDATED_PET_CATEGORY_NAME);
        updatedRace.setNumOfPets(UPDATED_NUM_OF_PETS);
        updatedRace.setLength(UPDATED_LENGTH);
        updatedRace.setDescription(UPDATED_DESCRIPTION);
        updatedRace.setWinnerId(UPDATED_WINNER_ID);
        updatedRace.setStartTime(UPDATED_START_TIME);
        updatedRace.setBaseSpeed(UPDATED_BASE_SPEED);

        restRaceMockMvc.perform(put("/api/races")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRace)))
                .andExpect(status().isOk());

        // Validate the Race in the database
        List<Race> races = raceRepository.findAll();
        assertThat(races).hasSize(databaseSizeBeforeUpdate);
        Race testRace = races.get(races.size() - 1);
        assertThat(testRace.getRaceId()).isEqualTo(race.getRaceId());
        assertThat(testRace.getPetCategoryId()).isEqualTo(UPDATED_PET_CATEGORY_ID);
        assertThat(testRace.getPetCategoryName()).isEqualTo(UPDATED_PET_CATEGORY_NAME);
        assertThat(testRace.getNumOfPets()).isEqualTo(UPDATED_NUM_OF_PETS);
        assertThat(testRace.getLength()).isEqualTo(UPDATED_LENGTH);
        assertThat(testRace.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRace.getWinnerId()).isEqualTo(UPDATED_WINNER_ID);
        assertThat(testRace.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testRace.getBaseSpeed()).isEqualTo(UPDATED_BASE_SPEED);
    }

    @Test
    public void deleteRace() throws Exception {
        // Initialize the database
        raceRepository.save(race);
        int databaseSizeBeforeDelete = raceRepository.findAll().size();

        // Get the race
        restRaceMockMvc.perform(delete("/api/races/{id}", race.getRaceId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Race> races = raceRepository.findAll();
        assertThat(races).hasSize(databaseSizeBeforeDelete - 1);
    }
}
