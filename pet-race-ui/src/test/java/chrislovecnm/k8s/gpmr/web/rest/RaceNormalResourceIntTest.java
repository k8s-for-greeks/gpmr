package chrislovecnm.k8s.gpmr.web.rest;

import chrislovecnm.k8s.gpmr.AbstractCassandraTest;
import chrislovecnm.k8s.gpmr.GpmrApp;
import chrislovecnm.k8s.gpmr.domain.RaceNormal;
import chrislovecnm.k8s.gpmr.repository.RaceNormalRepository;

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
 * Test class for the RaceNormalResource REST controller.
 *
 * @see RaceNormalResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GpmrApp.class)
@WebAppConfiguration
@IntegrationTest
public class RaceNormalResourceIntTest extends AbstractCassandraTest {


    private static final UUID DEFAULT_RACE_NORMAL_ID = UUID.randomUUID();
    private static final UUID UPDATED_RACE_NORMAL_ID = UUID.randomUUID();

    private static final UUID DEFAULT_RACE_ID = UUID.randomUUID();
    private static final UUID UPDATED_RACE_ID = UUID.randomUUID();

    private static final UUID DEFAULT_PET_CATEGORY_ID = UUID.randomUUID();
    private static final UUID UPDATED_PET_CATEGORY_ID = UUID.randomUUID();
    private static final String DEFAULT_PET_CATEGORY_NAME = "AAAAA";
    private static final String UPDATED_PET_CATEGORY_NAME = "BBBBB";

    private static final Date DEFAULT_CURRENT_TIME = new Date();
    private static final Date UPDATED_CURRENT_TIME = new Date();

    private static final Float DEFAULT_NORMAL_LOC = 1F;
    private static final Float UPDATED_NORMAL_LOC = 2F;

    private static final Float DEFAULT_NORMAL_SCALE = 1F;
    private static final Float UPDATED_NORMAL_SCALE = 2F;

    private static final Integer DEFAULT_NORMAL_SIZE = 1;
    private static final Integer UPDATED_NORMAL_SIZE = 2;

    @Inject
    private RaceNormalRepository raceNormalRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRaceNormalMockMvc;

    private RaceNormal raceNormal;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RaceNormalResource raceNormalResource = new RaceNormalResource();
        ReflectionTestUtils.setField(raceNormalResource, "raceNormalRepository", raceNormalRepository);
        this.restRaceNormalMockMvc = MockMvcBuilders.standaloneSetup(raceNormalResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        raceNormalRepository.deleteAll();
        raceNormal = new RaceNormal();
        raceNormal.setRaceNormalId(DEFAULT_RACE_NORMAL_ID);
        raceNormal.setRaceId(DEFAULT_RACE_ID);
        raceNormal.setPetCategoryId(DEFAULT_PET_CATEGORY_ID);
        raceNormal.setPetCategoryName(DEFAULT_PET_CATEGORY_NAME);
        raceNormal.setCurrentTime(DEFAULT_CURRENT_TIME);
        raceNormal.setNormalLoc(DEFAULT_NORMAL_LOC);
        raceNormal.setNormalScale(DEFAULT_NORMAL_SCALE);
        raceNormal.setNormalSize(DEFAULT_NORMAL_SIZE);
    }

    @Test
    public void createRaceNormal() throws Exception {
        int databaseSizeBeforeCreate = raceNormalRepository.findAll().size();

        // Create the RaceNormal

        raceNormal.setRaceNormalId(null);
        restRaceNormalMockMvc.perform(post("/api/race-normals")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(raceNormal)))
                .andExpect(status().isCreated());

        // Validate the RaceNormal in the database
        List<RaceNormal> raceNormals = raceNormalRepository.findAll();
        assertThat(raceNormals).hasSize(databaseSizeBeforeCreate + 1);
        RaceNormal testRaceNormal = raceNormals.get(raceNormals.size() - 1);
        assertThat(testRaceNormal.getRaceId()).isEqualTo(DEFAULT_RACE_ID);
        assertThat(testRaceNormal.getPetCategoryId()).isEqualTo(DEFAULT_PET_CATEGORY_ID);
        assertThat(testRaceNormal.getPetCategoryName()).isEqualTo(DEFAULT_PET_CATEGORY_NAME);
        assertThat(testRaceNormal.getCurrentTime()).isEqualTo(DEFAULT_CURRENT_TIME);
        assertThat(testRaceNormal.getNormalLoc()).isEqualTo(DEFAULT_NORMAL_LOC);
        assertThat(testRaceNormal.getNormalScale()).isEqualTo(DEFAULT_NORMAL_SCALE);
        assertThat(testRaceNormal.getNormalSize()).isEqualTo(DEFAULT_NORMAL_SIZE);
        raceNormal.setRaceNormalId(DEFAULT_RACE_NORMAL_ID);
    }

    @Test
    public void getAllRaceNormals() throws Exception {
        // Initialize the database
        raceNormalRepository.save(raceNormal);

        // Get all the raceNormals
        restRaceNormalMockMvc.perform(get("/api/race-normals?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].raceNormalId").value(hasItem(DEFAULT_RACE_NORMAL_ID.toString())))
                .andExpect(jsonPath("$.[*].raceId").value(hasItem(DEFAULT_RACE_ID.toString())))
                .andExpect(jsonPath("$.[*].petCategoryId").value(hasItem(DEFAULT_PET_CATEGORY_ID.toString())))
                .andExpect(jsonPath("$.[*].petCategoryName").value(hasItem(DEFAULT_PET_CATEGORY_NAME.toString())))
                .andExpect(jsonPath("$.[*].currentTime").value(hasItem(DEFAULT_CURRENT_TIME.getTime())))
                .andExpect(jsonPath("$.[*].normalLoc").value(hasItem(DEFAULT_NORMAL_LOC.doubleValue())))
                .andExpect(jsonPath("$.[*].normalScale").value(hasItem(DEFAULT_NORMAL_SCALE.doubleValue())))
                .andExpect(jsonPath("$.[*].normalSize").value(hasItem(DEFAULT_NORMAL_SIZE)));
    }

    @Test
    public void getRaceNormal() throws Exception {
        // Initialize the database
        raceNormalRepository.save(raceNormal);

        // Get the raceNormal
        restRaceNormalMockMvc.perform(get("/api/race-normals/{id}", raceNormal.getRaceNormalId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.raceNormalId").value(raceNormal.getRaceNormalId().toString()))
            .andExpect(jsonPath("$.raceId").value(DEFAULT_RACE_ID.toString()))
            .andExpect(jsonPath("$.petCategoryId").value(DEFAULT_PET_CATEGORY_ID.toString()))
            .andExpect(jsonPath("$.petCategoryName").value(DEFAULT_PET_CATEGORY_NAME.toString()))
            .andExpect(jsonPath("$.currentTime").value(DEFAULT_CURRENT_TIME.getTime()))
            .andExpect(jsonPath("$.normalLoc").value(DEFAULT_NORMAL_LOC.doubleValue()))
            .andExpect(jsonPath("$.normalScale").value(DEFAULT_NORMAL_SCALE.doubleValue()))
            .andExpect(jsonPath("$.normalSize").value(DEFAULT_NORMAL_SIZE));
    }

    @Test
    public void getNonExistingRaceNormal() throws Exception {
        // Get the raceNormal
        restRaceNormalMockMvc.perform(get("/api/race-normals/{id}", UUID.randomUUID().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateRaceNormal() throws Exception {
        // Initialize the database
        raceNormalRepository.save(raceNormal);
        int databaseSizeBeforeUpdate = raceNormalRepository.findAll().size();

        // Update the raceNormal
        RaceNormal updatedRaceNormal = new RaceNormal();
        updatedRaceNormal.setRaceNormalId(raceNormal.getRaceNormalId());
        updatedRaceNormal.setRaceId(UPDATED_RACE_ID);
        updatedRaceNormal.setPetCategoryId(UPDATED_PET_CATEGORY_ID);
        updatedRaceNormal.setPetCategoryName(UPDATED_PET_CATEGORY_NAME);
        updatedRaceNormal.setCurrentTime(UPDATED_CURRENT_TIME);
        updatedRaceNormal.setNormalLoc(UPDATED_NORMAL_LOC);
        updatedRaceNormal.setNormalScale(UPDATED_NORMAL_SCALE);
        updatedRaceNormal.setNormalSize(UPDATED_NORMAL_SIZE);

        restRaceNormalMockMvc.perform(put("/api/race-normals")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRaceNormal)))
                .andExpect(status().isOk());

        // Validate the RaceNormal in the database
        List<RaceNormal> raceNormals = raceNormalRepository.findAll();
        assertThat(raceNormals).hasSize(databaseSizeBeforeUpdate);
        RaceNormal testRaceNormal = raceNormals.get(raceNormals.size() - 1);
        assertThat(testRaceNormal.getRaceNormalId()).isEqualTo(raceNormal.getRaceNormalId());
        assertThat(testRaceNormal.getRaceId()).isEqualTo(UPDATED_RACE_ID);
        assertThat(testRaceNormal.getPetCategoryId()).isEqualTo(UPDATED_PET_CATEGORY_ID);
        assertThat(testRaceNormal.getPetCategoryName()).isEqualTo(UPDATED_PET_CATEGORY_NAME);
        assertThat(testRaceNormal.getCurrentTime()).isEqualTo(UPDATED_CURRENT_TIME);
        assertThat(testRaceNormal.getNormalLoc()).isEqualTo(UPDATED_NORMAL_LOC);
        assertThat(testRaceNormal.getNormalScale()).isEqualTo(UPDATED_NORMAL_SCALE);
        assertThat(testRaceNormal.getNormalSize()).isEqualTo(UPDATED_NORMAL_SIZE);
    }

    @Test
    public void deleteRaceNormal() throws Exception {
        // Initialize the database
        raceNormalRepository.save(raceNormal);
        int databaseSizeBeforeDelete = raceNormalRepository.findAll().size();

        // Get the raceNormal
        restRaceNormalMockMvc.perform(delete("/api/race-normals/{id}", raceNormal.getRaceNormalId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<RaceNormal> raceNormals = raceNormalRepository.findAll();
        assertThat(raceNormals).hasSize(databaseSizeBeforeDelete - 1);
    }
}
