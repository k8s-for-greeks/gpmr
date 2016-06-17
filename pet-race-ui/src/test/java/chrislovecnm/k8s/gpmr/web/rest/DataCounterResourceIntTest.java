package chrislovecnm.k8s.gpmr.web.rest;

import chrislovecnm.k8s.gpmr.AbstractCassandraTest;
import chrislovecnm.k8s.gpmr.GpmrApp;
import chrislovecnm.k8s.gpmr.domain.DataCounter;
import chrislovecnm.k8s.gpmr.repository.DataCounterRepository;

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
 * Test class for the DataCounterResource REST controller.
 *
 * @see DataCounterResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GpmrApp.class)
@WebAppConfiguration
@IntegrationTest
public class DataCounterResourceIntTest extends AbstractCassandraTest {

    private static final String DEFAULT_VTYPE = "AAAAA";

    private static final Long DEFAULT_VALUE = 1L;
    private static final Long UPDATED_VALUE = 2L;

    @Inject
    private DataCounterRepository dataCounterRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDataCounterMockMvc;

    private DataCounter dataCounter;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DataCounterResource dataCounterResource = new DataCounterResource();
        ReflectionTestUtils.setField(dataCounterResource, "dataCounterRepository", dataCounterRepository);
        this.restDataCounterMockMvc = MockMvcBuilders.standaloneSetup(dataCounterResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        dataCounterRepository.deleteAll();
        dataCounter = new DataCounter();
        dataCounter.setVtype(DEFAULT_VTYPE);
        dataCounter.setValue(DEFAULT_VALUE);
    }

    @Test
    public void createDataCounter() throws Exception {
        int databaseSizeBeforeCreate = dataCounterRepository.findAll().size();

        // Create the DataCounter

        restDataCounterMockMvc.perform(post("/api/data-counters")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dataCounter)))
                .andExpect(status().isCreated());

        // Validate the DataCounter in the database
        List<DataCounter> dataCounters = dataCounterRepository.findAll();
        assertThat(dataCounters).hasSize(databaseSizeBeforeCreate + 1);
        DataCounter testDataCounter = dataCounters.get(dataCounters.size() - 1);
        assertThat(testDataCounter.getVtype()).isEqualTo(DEFAULT_VTYPE);
        assertThat(testDataCounter.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    public void getAllDataCounters() throws Exception {
        // Initialize the database
        dataCounterRepository.save(dataCounter);

        // Get all the dataCounters
        restDataCounterMockMvc.perform(get("/api/data-counters?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].vtype").value(hasItem(DEFAULT_VTYPE.toString())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    public void getDataCounter() throws Exception {
        // Initialize the database
        dataCounterRepository.save(dataCounter);

        // Get the dataCounter
        restDataCounterMockMvc.perform(get("/api/data-counters/{id}", dataCounter.getVtype()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.vtype").value(DEFAULT_VTYPE.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    public void getNonExistingDataCounter() throws Exception {
        // Get the dataCounter
        restDataCounterMockMvc.perform(get("/api/data-counters/{id}", UUID.randomUUID().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateDataCounter() throws Exception {
        // Initialize the database
        dataCounterRepository.save(dataCounter);
        int databaseSizeBeforeUpdate = dataCounterRepository.findAll().size();

        // Update the dataCounter
        DataCounter updatedDataCounter = new DataCounter();
        updatedDataCounter.setValue(UPDATED_VALUE);

        restDataCounterMockMvc.perform(put("/api/data-counters")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDataCounter)))
                .andExpect(status().isOk());

        // Validate the DataCounter in the database
        List<DataCounter> dataCounters = dataCounterRepository.findAll();
        assertThat(dataCounters).hasSize(databaseSizeBeforeUpdate);
        DataCounter testDataCounter = dataCounters.get(dataCounters.size() - 1);
        assertThat(testDataCounter.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    public void deleteDataCounter() throws Exception {
        // Initialize the database
        dataCounterRepository.save(dataCounter);
        int databaseSizeBeforeDelete = dataCounterRepository.findAll().size();

        // Get the dataCounter
        restDataCounterMockMvc.perform(delete("/api/data-counters/{id}", dataCounter.getVtype())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<DataCounter> dataCounters = dataCounterRepository.findAll();
        assertThat(dataCounters).hasSize(databaseSizeBeforeDelete - 1);
    }
}
