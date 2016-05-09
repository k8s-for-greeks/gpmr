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


    private static final UUID DEFAULT_PET_ID = UUID.randomUUID();
    private static final UUID UPDATED_PET_ID = UUID.randomUUID();
    private static final String DEFAULT_PET_NAME = "AAAAA";
    private static final String UPDATED_PET_NAME = "BBBBB";
    private static final String DEFAULT_PET_CATEGORY = "AAAAA";
    private static final String UPDATED_PET_CATEGORY = "BBBBB";

    private static final UUID DEFAULT_PET_CATEGORY_ID = UUID.randomUUID();
    private static final UUID UPDATED_PET_CATEGORY_ID = UUID.randomUUID();

    private static final Integer DEFAULT_RUNNER_POSTION = 1;
    private static final Integer UPDATED_RUNNER_POSTION = 2;
    private static final String DEFAULT_RUNNER_SASH_COLOR = "AAAAA";
    private static final String UPDATED_RUNNER_SASH_COLOR = "BBBBB";

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
        raceData.setPetName(DEFAULT_PET_NAME);
        raceData.setPetCategory(DEFAULT_PET_CATEGORY);
        raceData.setPetCategoryId(DEFAULT_PET_CATEGORY_ID);
        raceData.setRunnerPostion(DEFAULT_RUNNER_POSTION);
        raceData.setRunnerSashColor(DEFAULT_RUNNER_SASH_COLOR);
    }

    @Test
    public void createRaceData() throws Exception {
        int databaseSizeBeforeCreate = raceDataRepository.findAll().size();

        // Create the RaceData

        restRaceDataMockMvc.perform(post("/api/race-data")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(raceData)))
                .andExpect(status().isCreated());

        // Validate the RaceData in the database
        List<RaceData> raceData = raceDataRepository.findAll();
        assertThat(raceData).hasSize(databaseSizeBeforeCreate + 1);
        RaceData testRaceData = raceData.get(raceData.size() - 1);
        assertThat(testRaceData.getPetId()).isEqualTo(DEFAULT_PET_ID);
        assertThat(testRaceData.getPetName()).isEqualTo(DEFAULT_PET_NAME);
        assertThat(testRaceData.getPetCategory()).isEqualTo(DEFAULT_PET_CATEGORY);
        assertThat(testRaceData.getPetCategoryId()).isEqualTo(DEFAULT_PET_CATEGORY_ID);
        assertThat(testRaceData.getRunnerPostion()).isEqualTo(DEFAULT_RUNNER_POSTION);
        assertThat(testRaceData.getRunnerSashColor()).isEqualTo(DEFAULT_RUNNER_SASH_COLOR);
    }

    @Test
    public void getAllRaceData() throws Exception {
        // Initialize the database
        raceDataRepository.save(raceData);

        // Get all the raceData
        restRaceDataMockMvc.perform(get("/api/race-data?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(raceData.getId().toString())))
                .andExpect(jsonPath("$.[*].petId").value(hasItem(DEFAULT_PET_ID.toString())))
                .andExpect(jsonPath("$.[*].petName").value(hasItem(DEFAULT_PET_NAME.toString())))
                .andExpect(jsonPath("$.[*].petCategory").value(hasItem(DEFAULT_PET_CATEGORY.toString())))
                .andExpect(jsonPath("$.[*].petCategoryId").value(hasItem(DEFAULT_PET_CATEGORY_ID.toString())))
                .andExpect(jsonPath("$.[*].runnerPostion").value(hasItem(DEFAULT_RUNNER_POSTION)))
                .andExpect(jsonPath("$.[*].runnerSashColor").value(hasItem(DEFAULT_RUNNER_SASH_COLOR.toString())));
    }

    @Test
    public void getRaceData() throws Exception {
        // Initialize the database
        raceDataRepository.save(raceData);

        // Get the raceData
        restRaceDataMockMvc.perform(get("/api/race-data/{id}", raceData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(raceData.getId().toString()))
            .andExpect(jsonPath("$.petId").value(DEFAULT_PET_ID.toString()))
            .andExpect(jsonPath("$.petName").value(DEFAULT_PET_NAME.toString()))
            .andExpect(jsonPath("$.petCategory").value(DEFAULT_PET_CATEGORY.toString()))
            .andExpect(jsonPath("$.petCategoryId").value(DEFAULT_PET_CATEGORY_ID.toString()))
            .andExpect(jsonPath("$.runnerPostion").value(DEFAULT_RUNNER_POSTION))
            .andExpect(jsonPath("$.runnerSashColor").value(DEFAULT_RUNNER_SASH_COLOR.toString()));
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
        updatedRaceData.setId(raceData.getId());
        updatedRaceData.setPetId(UPDATED_PET_ID);
        updatedRaceData.setPetName(UPDATED_PET_NAME);
        updatedRaceData.setPetCategory(UPDATED_PET_CATEGORY);
        updatedRaceData.setPetCategoryId(UPDATED_PET_CATEGORY_ID);
        updatedRaceData.setRunnerPostion(UPDATED_RUNNER_POSTION);
        updatedRaceData.setRunnerSashColor(UPDATED_RUNNER_SASH_COLOR);

        restRaceDataMockMvc.perform(put("/api/race-data")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRaceData)))
                .andExpect(status().isOk());

        // Validate the RaceData in the database
        List<RaceData> raceData = raceDataRepository.findAll();
        assertThat(raceData).hasSize(databaseSizeBeforeUpdate);
        RaceData testRaceData = raceData.get(raceData.size() - 1);
        assertThat(testRaceData.getPetId()).isEqualTo(UPDATED_PET_ID);
        assertThat(testRaceData.getPetName()).isEqualTo(UPDATED_PET_NAME);
        assertThat(testRaceData.getPetCategory()).isEqualTo(UPDATED_PET_CATEGORY);
        assertThat(testRaceData.getPetCategoryId()).isEqualTo(UPDATED_PET_CATEGORY_ID);
        assertThat(testRaceData.getRunnerPostion()).isEqualTo(UPDATED_RUNNER_POSTION);
        assertThat(testRaceData.getRunnerSashColor()).isEqualTo(UPDATED_RUNNER_SASH_COLOR);
    }

    @Test
    public void deleteRaceData() throws Exception {
        // Initialize the database
        raceDataRepository.save(raceData);
        int databaseSizeBeforeDelete = raceDataRepository.findAll().size();

        // Get the raceData
        restRaceDataMockMvc.perform(delete("/api/race-data/{id}", raceData.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<RaceData> raceData = raceDataRepository.findAll();
        assertThat(raceData).hasSize(databaseSizeBeforeDelete - 1);
    }
}
