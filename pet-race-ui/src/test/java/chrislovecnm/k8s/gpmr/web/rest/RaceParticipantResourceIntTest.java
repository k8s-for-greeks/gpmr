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
        raceParticipant.setPetId(DEFAULT_PET_ID);
        raceParticipant.setPetName(DEFAULT_PET_NAME);
        raceParticipant.setPetType(DEFAULT_PET_TYPE);
        raceParticipant.setPetColor(DEFAULT_PET_COLOR);
        raceParticipant.setPetCategory(DEFAULT_PET_CATEGORY);
        raceParticipant.setPetCategoryId(DEFAULT_PET_CATEGORY_ID);
        raceParticipant.setRaceId(DEFAULT_RACE_ID);
    }

    @Test
    public void createRaceParticipant() throws Exception {
        int databaseSizeBeforeCreate = raceParticipantRepository.findAll().size();

        // Create the RaceParticipant

        restRaceParticipantMockMvc.perform(post("/api/race-participants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(raceParticipant)))
                .andExpect(status().isCreated());

        // Validate the RaceParticipant in the database
        List<RaceParticipant> raceParticipants = raceParticipantRepository.findAll();
        assertThat(raceParticipants).hasSize(databaseSizeBeforeCreate + 1);
        RaceParticipant testRaceParticipant = raceParticipants.get(raceParticipants.size() - 1);
        assertThat(testRaceParticipant.getPetId()).isEqualTo(DEFAULT_PET_ID);
        assertThat(testRaceParticipant.getPetName()).isEqualTo(DEFAULT_PET_NAME);
        assertThat(testRaceParticipant.getPetType()).isEqualTo(DEFAULT_PET_TYPE);
        assertThat(testRaceParticipant.getPetColor()).isEqualTo(DEFAULT_PET_COLOR);
        assertThat(testRaceParticipant.getPetCategory()).isEqualTo(DEFAULT_PET_CATEGORY);
        assertThat(testRaceParticipant.getPetCategoryId()).isEqualTo(DEFAULT_PET_CATEGORY_ID);
        assertThat(testRaceParticipant.getRaceId()).isEqualTo(DEFAULT_RACE_ID);
    }

    @Test
    public void getAllRaceParticipants() throws Exception {
        // Initialize the database
        raceParticipantRepository.save(raceParticipant);

        // Get all the raceParticipants
        restRaceParticipantMockMvc.perform(get("/api/race-participants?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(raceParticipant.getId().toString())))
                .andExpect(jsonPath("$.[*].petId").value(hasItem(DEFAULT_PET_ID.toString())))
                .andExpect(jsonPath("$.[*].petName").value(hasItem(DEFAULT_PET_NAME.toString())))
                .andExpect(jsonPath("$.[*].petType").value(hasItem(DEFAULT_PET_TYPE.toString())))
                .andExpect(jsonPath("$.[*].petColor").value(hasItem(DEFAULT_PET_COLOR.toString())))
                .andExpect(jsonPath("$.[*].petCategory").value(hasItem(DEFAULT_PET_CATEGORY.toString())))
                .andExpect(jsonPath("$.[*].petCategoryId").value(hasItem(DEFAULT_PET_CATEGORY_ID.toString())))
                .andExpect(jsonPath("$.[*].raceId").value(hasItem(DEFAULT_RACE_ID.toString())));
    }

    @Test
    public void getRaceParticipant() throws Exception {
        // Initialize the database
        raceParticipantRepository.save(raceParticipant);

        // Get the raceParticipant
        restRaceParticipantMockMvc.perform(get("/api/race-participants/{id}", raceParticipant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(raceParticipant.getId().toString()))
            .andExpect(jsonPath("$.petId").value(DEFAULT_PET_ID.toString()))
            .andExpect(jsonPath("$.petName").value(DEFAULT_PET_NAME.toString()))
            .andExpect(jsonPath("$.petType").value(DEFAULT_PET_TYPE.toString()))
            .andExpect(jsonPath("$.petColor").value(DEFAULT_PET_COLOR.toString()))
            .andExpect(jsonPath("$.petCategory").value(DEFAULT_PET_CATEGORY.toString()))
            .andExpect(jsonPath("$.petCategoryId").value(DEFAULT_PET_CATEGORY_ID.toString()))
            .andExpect(jsonPath("$.raceId").value(DEFAULT_RACE_ID.toString()));
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
        updatedRaceParticipant.setId(raceParticipant.getId());
        updatedRaceParticipant.setPetId(UPDATED_PET_ID);
        updatedRaceParticipant.setPetName(UPDATED_PET_NAME);
        updatedRaceParticipant.setPetType(UPDATED_PET_TYPE);
        updatedRaceParticipant.setPetColor(UPDATED_PET_COLOR);
        updatedRaceParticipant.setPetCategory(UPDATED_PET_CATEGORY);
        updatedRaceParticipant.setPetCategoryId(UPDATED_PET_CATEGORY_ID);
        updatedRaceParticipant.setRaceId(UPDATED_RACE_ID);

        restRaceParticipantMockMvc.perform(put("/api/race-participants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRaceParticipant)))
                .andExpect(status().isOk());

        // Validate the RaceParticipant in the database
        List<RaceParticipant> raceParticipants = raceParticipantRepository.findAll();
        assertThat(raceParticipants).hasSize(databaseSizeBeforeUpdate);
        RaceParticipant testRaceParticipant = raceParticipants.get(raceParticipants.size() - 1);
        assertThat(testRaceParticipant.getPetId()).isEqualTo(UPDATED_PET_ID);
        assertThat(testRaceParticipant.getPetName()).isEqualTo(UPDATED_PET_NAME);
        assertThat(testRaceParticipant.getPetType()).isEqualTo(UPDATED_PET_TYPE);
        assertThat(testRaceParticipant.getPetColor()).isEqualTo(UPDATED_PET_COLOR);
        assertThat(testRaceParticipant.getPetCategory()).isEqualTo(UPDATED_PET_CATEGORY);
        assertThat(testRaceParticipant.getPetCategoryId()).isEqualTo(UPDATED_PET_CATEGORY_ID);
        assertThat(testRaceParticipant.getRaceId()).isEqualTo(UPDATED_RACE_ID);
    }

    @Test
    public void deleteRaceParticipant() throws Exception {
        // Initialize the database
        raceParticipantRepository.save(raceParticipant);
        int databaseSizeBeforeDelete = raceParticipantRepository.findAll().size();

        // Get the raceParticipant
        restRaceParticipantMockMvc.perform(delete("/api/race-participants/{id}", raceParticipant.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<RaceParticipant> raceParticipants = raceParticipantRepository.findAll();
        assertThat(raceParticipants).hasSize(databaseSizeBeforeDelete - 1);
    }
}
