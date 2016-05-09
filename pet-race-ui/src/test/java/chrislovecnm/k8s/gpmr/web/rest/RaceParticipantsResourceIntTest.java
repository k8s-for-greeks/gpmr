package chrislovecnm.k8s.gpmr.web.rest;

import chrislovecnm.k8s.gpmr.AbstractCassandraTest;
import chrislovecnm.k8s.gpmr.GpmrApp;
import chrislovecnm.k8s.gpmr.domain.RaceParticipants;
import chrislovecnm.k8s.gpmr.repository.RaceParticipantsRepository;

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
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the RaceParticipantsResource REST controller.
 *
 * @see RaceParticipantsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GpmrApp.class)
@WebAppConfiguration
@IntegrationTest
public class RaceParticipantsResourceIntTest extends AbstractCassandraTest {


    private static final UUID DEFAULT_PET_ID = UUID.randomUUID();
    private static final UUID UPDATED_PET_ID = UUID.randomUUID();
    private static final String DEFAULT_PET_NAME = "AAAAA";
    private static final String UPDATED_PET_NAME = "BBBBB";
    private static final String DEFAULT_PET_TYPE = "AAAAA";
    private static final String UPDATED_PET_TYPE = "BBBBB";
    private static final String DEFAULT_PET_COLOR = "AAAAA";
    private static final String UPDATED_PET_COLOR = "BBBBB";
    private static final String DEFAULT_PET_CATEGORY = "AAAAA";
    private static final String UPDATED_PET_CATEGORY = "BBBBB";
    private static final String DEFAULT_PET_CATEGORY_ID = "AAAAA";
    private static final String UPDATED_PET_CATEGORY_ID = "BBBBB";

    private static final UUID DEFAULT_RACE_ID = UUID.randomUUID();
    private static final UUID UPDATED_RACE_ID = UUID.randomUUID();

    @Inject
    private RaceParticipantsRepository raceParticipantsRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRaceParticipantsMockMvc;

    private RaceParticipants raceParticipants;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RaceParticipantsResource raceParticipantsResource = new RaceParticipantsResource();
        ReflectionTestUtils.setField(raceParticipantsResource, "raceParticipantsRepository", raceParticipantsRepository);
        this.restRaceParticipantsMockMvc = MockMvcBuilders.standaloneSetup(raceParticipantsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        raceParticipantsRepository.deleteAll();
        raceParticipants = new RaceParticipants();
        raceParticipants.setPetId(DEFAULT_PET_ID);
        raceParticipants.setPetName(DEFAULT_PET_NAME);
        raceParticipants.setPetType(DEFAULT_PET_TYPE);
        raceParticipants.setPetColor(DEFAULT_PET_COLOR);
        raceParticipants.setPetCategory(DEFAULT_PET_CATEGORY);
        raceParticipants.setPetCategoryId(DEFAULT_PET_CATEGORY_ID);
        raceParticipants.setRaceId(DEFAULT_RACE_ID);
    }

    @Test
    public void createRaceParticipants() throws Exception {
        int databaseSizeBeforeCreate = raceParticipantsRepository.findAll().size();

        // Create the RaceParticipants

        restRaceParticipantsMockMvc.perform(post("/api/race-participants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(raceParticipants)))
                .andExpect(status().isCreated());

        // Validate the RaceParticipants in the database
        List<RaceParticipants> raceParticipants = raceParticipantsRepository.findAll();
        assertThat(raceParticipants).hasSize(databaseSizeBeforeCreate + 1);
        RaceParticipants testRaceParticipants = raceParticipants.get(raceParticipants.size() - 1);
        assertThat(testRaceParticipants.getPetId()).isEqualTo(DEFAULT_PET_ID);
        assertThat(testRaceParticipants.getPetName()).isEqualTo(DEFAULT_PET_NAME);
        assertThat(testRaceParticipants.getPetType()).isEqualTo(DEFAULT_PET_TYPE);
        assertThat(testRaceParticipants.getPetColor()).isEqualTo(DEFAULT_PET_COLOR);
        assertThat(testRaceParticipants.getPetCategory()).isEqualTo(DEFAULT_PET_CATEGORY);
        assertThat(testRaceParticipants.getPetCategoryId()).isEqualTo(DEFAULT_PET_CATEGORY_ID);
        assertThat(testRaceParticipants.getRaceId()).isEqualTo(DEFAULT_RACE_ID);
    }

    @Test
    public void getAllRaceParticipants() throws Exception {
        // Initialize the database
        raceParticipantsRepository.save(raceParticipants);

        // Get all the raceParticipants
        restRaceParticipantsMockMvc.perform(get("/api/race-participants?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(raceParticipants.getId().toString())))
                .andExpect(jsonPath("$.[*].petId").value(hasItem(DEFAULT_PET_ID.toString())))
                .andExpect(jsonPath("$.[*].petName").value(hasItem(DEFAULT_PET_NAME.toString())))
                .andExpect(jsonPath("$.[*].petType").value(hasItem(DEFAULT_PET_TYPE.toString())))
                .andExpect(jsonPath("$.[*].petColor").value(hasItem(DEFAULT_PET_COLOR.toString())))
                .andExpect(jsonPath("$.[*].petCategory").value(hasItem(DEFAULT_PET_CATEGORY.toString())))
                .andExpect(jsonPath("$.[*].petCategoryId").value(hasItem(DEFAULT_PET_CATEGORY_ID.toString())))
                .andExpect(jsonPath("$.[*].raceId").value(hasItem(DEFAULT_RACE_ID.toString())));
    }

    @Test
    public void getRaceParticipants() throws Exception {
        // Initialize the database
        raceParticipantsRepository.save(raceParticipants);

        // Get the raceParticipants
        restRaceParticipantsMockMvc.perform(get("/api/race-participants/{id}", raceParticipants.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(raceParticipants.getId().toString()))
            .andExpect(jsonPath("$.petId").value(DEFAULT_PET_ID.toString()))
            .andExpect(jsonPath("$.petName").value(DEFAULT_PET_NAME.toString()))
            .andExpect(jsonPath("$.petType").value(DEFAULT_PET_TYPE.toString()))
            .andExpect(jsonPath("$.petColor").value(DEFAULT_PET_COLOR.toString()))
            .andExpect(jsonPath("$.petCategory").value(DEFAULT_PET_CATEGORY.toString()))
            .andExpect(jsonPath("$.petCategoryId").value(DEFAULT_PET_CATEGORY_ID.toString()))
            .andExpect(jsonPath("$.raceId").value(DEFAULT_RACE_ID.toString()));
    }

    @Test
    public void getNonExistingRaceParticipants() throws Exception {
        // Get the raceParticipants
        restRaceParticipantsMockMvc.perform(get("/api/race-participants/{id}", UUID.randomUUID().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateRaceParticipants() throws Exception {
        // Initialize the database
        raceParticipantsRepository.save(raceParticipants);
        int databaseSizeBeforeUpdate = raceParticipantsRepository.findAll().size();

        // Update the raceParticipants
        RaceParticipants updatedRaceParticipants = new RaceParticipants();
        updatedRaceParticipants.setId(raceParticipants.getId());
        updatedRaceParticipants.setPetId(UPDATED_PET_ID);
        updatedRaceParticipants.setPetName(UPDATED_PET_NAME);
        updatedRaceParticipants.setPetType(UPDATED_PET_TYPE);
        updatedRaceParticipants.setPetColor(UPDATED_PET_COLOR);
        updatedRaceParticipants.setPetCategory(UPDATED_PET_CATEGORY);
        updatedRaceParticipants.setPetCategoryId(UPDATED_PET_CATEGORY_ID);
        updatedRaceParticipants.setRaceId(UPDATED_RACE_ID);

        restRaceParticipantsMockMvc.perform(put("/api/race-participants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRaceParticipants)))
                .andExpect(status().isOk());

        // Validate the RaceParticipants in the database
        List<RaceParticipants> raceParticipants = raceParticipantsRepository.findAll();
        assertThat(raceParticipants).hasSize(databaseSizeBeforeUpdate);
        RaceParticipants testRaceParticipants = raceParticipants.get(raceParticipants.size() - 1);
        assertThat(testRaceParticipants.getPetId()).isEqualTo(UPDATED_PET_ID);
        assertThat(testRaceParticipants.getPetName()).isEqualTo(UPDATED_PET_NAME);
        assertThat(testRaceParticipants.getPetType()).isEqualTo(UPDATED_PET_TYPE);
        assertThat(testRaceParticipants.getPetColor()).isEqualTo(UPDATED_PET_COLOR);
        assertThat(testRaceParticipants.getPetCategory()).isEqualTo(UPDATED_PET_CATEGORY);
        assertThat(testRaceParticipants.getPetCategoryId()).isEqualTo(UPDATED_PET_CATEGORY_ID);
        assertThat(testRaceParticipants.getRaceId()).isEqualTo(UPDATED_RACE_ID);
    }

    @Test
    public void deleteRaceParticipants() throws Exception {
        // Initialize the database
        raceParticipantsRepository.save(raceParticipants);
        int databaseSizeBeforeDelete = raceParticipantsRepository.findAll().size();

        // Get the raceParticipants
        restRaceParticipantsMockMvc.perform(delete("/api/race-participants/{id}", raceParticipants.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<RaceParticipants> raceParticipants = raceParticipantsRepository.findAll();
        assertThat(raceParticipants).hasSize(databaseSizeBeforeDelete - 1);
    }
}
