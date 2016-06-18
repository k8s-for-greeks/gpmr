package chrislovecnm.k8s.gpmr.web.rest;

import chrislovecnm.k8s.gpmr.AbstractCassandraTest;
import chrislovecnm.k8s.gpmr.GpmrApp;
import chrislovecnm.k8s.gpmr.domain.Metric;
import chrislovecnm.k8s.gpmr.repository.MetricRepository;

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
 * Test class for the MetricResource REST controller.
 *
 * @see MetricResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GpmrApp.class)
@WebAppConfiguration
@IntegrationTest
public class MetricResourceIntTest extends AbstractCassandraTest {


    private static final UUID DEFAULT_METRIC_ID = UUID.randomUUID();
    private static final UUID UPDATED_METRIC_ID = UUID.randomUUID();

    private static final Integer DEFAULT_CONNECTION_ERRORS = 1;
    private static final Integer UPDATED_CONNECTION_ERRORS = 2;

    private static final Integer DEFAULT_WRITE_TIMEOUTS = 1;
    private static final Integer UPDATED_WRITE_TIMEOUTS = 2;

    private static final Integer DEFAULT_READ_TIMEOUTS = 1;
    private static final Integer UPDATED_READ_TIMEOUTS = 2;

    private static final Integer DEFAULT_UNAVAILABLES = 1;
    private static final Integer UPDATED_UNAVAILABLES = 2;

    private static final Integer DEFAULT_OTHER_ERRORS = 1;
    private static final Integer UPDATED_OTHER_ERRORS = 2;

    private static final Integer DEFAULT_RETRIES = 1;
    private static final Integer UPDATED_RETRIES = 2;

    private static final Integer DEFAULT_IGNORES = 1;
    private static final Integer UPDATED_IGNORES = 2;

    private static final Integer DEFAULT_KNOWN_HOSTS = 1;
    private static final Integer UPDATED_KNOWN_HOSTS = 2;

    private static final Integer DEFAULT_CONNECTED_TO = 1;
    private static final Integer UPDATED_CONNECTED_TO = 2;

    private static final Integer DEFAULT_OPEN_CONNECTIONS = 1;
    private static final Integer UPDATED_OPEN_CONNECTIONS = 2;

    private static final Integer DEFAULT_REQ_COUNT = 1;
    private static final Integer UPDATED_REQ_COUNT = 2;

    private static final Double DEFAULT_REQ_MIN_LATENCY = 1D;
    private static final Double UPDATED_REQ_MIN_LATENCY = 2D;

    private static final Double DEFAULT_REQ_MAX_LATENCY = 1D;
    private static final Double UPDATED_REQ_MAX_LATENCY = 2D;

    private static final Double DEFAULT_REQ_MEAN_LATENCY = 1D;
    private static final Double UPDATED_REQ_MEAN_LATENCY = 2D;

    private static final Double DEFAULT_REQ_STDEV = 1D;
    private static final Double UPDATED_REQ_STDEV = 2D;

    private static final Double DEFAULT_REQ_MEDIAN = 1D;
    private static final Double UPDATED_REQ_MEDIAN = 2D;

    private static final Double DEFAULT_REQ_75_PERCENTILE = 1D;
    private static final Double UPDATED_REQ_75_PERCENTILE = 2D;

    private static final Double DEFAULT_REQ_97_PERCENTILE = 1D;
    private static final Double UPDATED_REQ_97_PERCENTILE = 2D;

    private static final Double DEFAULT_REQ_98_PERCENTILE = 1D;
    private static final Double UPDATED_REQ_98_PERCENTILE = 2D;

    private static final Double DEFAULT_REQ_99_PERCENTILE = 1D;
    private static final Double UPDATED_REQ_99_PERCENTILE = 2D;

    private static final Double DEFAULT_REQ_999_PERCENTILE = 1D;
    private static final Double UPDATED_REQ_999_PERCENTILE = 2D;

    private static final Date DEFAULT_DATE_CREATED = new Date();
    private static final Date UPDATED_DATE_CREATED = new Date();

    @Inject
    private MetricRepository metricRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMetricMockMvc;

    private Metric metric;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MetricResource metricResource = new MetricResource();
        ReflectionTestUtils.setField(metricResource, "metricRepository", metricRepository);
        this.restMetricMockMvc = MockMvcBuilders.standaloneSetup(metricResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        metricRepository.deleteAll();
        metric = new Metric();
        metric.setConnectionErrors(DEFAULT_CONNECTION_ERRORS);
        metric.setWriteTimeouts(DEFAULT_WRITE_TIMEOUTS);
        metric.setReadTimeouts(DEFAULT_READ_TIMEOUTS);
        metric.setUnavailables(DEFAULT_UNAVAILABLES);
        metric.setOtherErrors(DEFAULT_OTHER_ERRORS);
        metric.setRetries(DEFAULT_RETRIES);
        metric.setIgnores(DEFAULT_IGNORES);
        metric.setKnownHosts(DEFAULT_KNOWN_HOSTS);
        metric.setConnectedTo(DEFAULT_CONNECTED_TO);
        metric.setOpenConnections(DEFAULT_OPEN_CONNECTIONS);
        metric.setReqCount(DEFAULT_REQ_COUNT);
        metric.setReqMinLatency(DEFAULT_REQ_MIN_LATENCY);
        metric.setReqMaxLatency(DEFAULT_REQ_MAX_LATENCY);
        metric.setReqMeanLatency(DEFAULT_REQ_MEAN_LATENCY);
        metric.setReqStdev(DEFAULT_REQ_STDEV);
        metric.setReqMedian(DEFAULT_REQ_MEDIAN);
        metric.setReq75percentile(DEFAULT_REQ_75_PERCENTILE);
        metric.setReq97percentile(DEFAULT_REQ_97_PERCENTILE);
        metric.setReq98percentile(DEFAULT_REQ_98_PERCENTILE);
        metric.setReq99percentile(DEFAULT_REQ_99_PERCENTILE);
        metric.setReq999percentile(DEFAULT_REQ_999_PERCENTILE);
        metric.setDateCreated(DEFAULT_DATE_CREATED);
    }

    @Test
    public void createMetric() throws Exception {
        int databaseSizeBeforeCreate = metricRepository.findAll().size();

        // Create the Metric

        restMetricMockMvc.perform(post("/api/metrics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(metric)))
                .andExpect(status().isCreated());

        // Validate the Metric in the database
        List<Metric> metrics = metricRepository.findAll();
        assertThat(metrics).hasSize(databaseSizeBeforeCreate + 1);
        Metric testMetric = metrics.get(metrics.size() - 1);
        assertThat(testMetric.getMetricId()).isEqualTo(DEFAULT_METRIC_ID);
        assertThat(testMetric.getConnectionErrors()).isEqualTo(DEFAULT_CONNECTION_ERRORS);
        assertThat(testMetric.getWriteTimeouts()).isEqualTo(DEFAULT_WRITE_TIMEOUTS);
        assertThat(testMetric.getReadTimeouts()).isEqualTo(DEFAULT_READ_TIMEOUTS);
        assertThat(testMetric.getUnavailables()).isEqualTo(DEFAULT_UNAVAILABLES);
        assertThat(testMetric.getOtherErrors()).isEqualTo(DEFAULT_OTHER_ERRORS);
        assertThat(testMetric.getRetries()).isEqualTo(DEFAULT_RETRIES);
        assertThat(testMetric.getIgnores()).isEqualTo(DEFAULT_IGNORES);
        assertThat(testMetric.getKnownHosts()).isEqualTo(DEFAULT_KNOWN_HOSTS);
        assertThat(testMetric.getConnectedTo()).isEqualTo(DEFAULT_CONNECTED_TO);
        assertThat(testMetric.getOpenConnections()).isEqualTo(DEFAULT_OPEN_CONNECTIONS);
        assertThat(testMetric.getReqCount()).isEqualTo(DEFAULT_REQ_COUNT);
        assertThat(testMetric.getReqMinLatency()).isEqualTo(DEFAULT_REQ_MIN_LATENCY);
        assertThat(testMetric.getReqMaxLatency()).isEqualTo(DEFAULT_REQ_MAX_LATENCY);
        assertThat(testMetric.getReqMeanLatency()).isEqualTo(DEFAULT_REQ_MEAN_LATENCY);
        assertThat(testMetric.getReqStdev()).isEqualTo(DEFAULT_REQ_STDEV);
        assertThat(testMetric.getReqMedian()).isEqualTo(DEFAULT_REQ_MEDIAN);
        assertThat(testMetric.getReq75percentile()).isEqualTo(DEFAULT_REQ_75_PERCENTILE);
        assertThat(testMetric.getReq97percentile()).isEqualTo(DEFAULT_REQ_97_PERCENTILE);
        assertThat(testMetric.getReq98percentile()).isEqualTo(DEFAULT_REQ_98_PERCENTILE);
        assertThat(testMetric.getReq99percentile()).isEqualTo(DEFAULT_REQ_99_PERCENTILE);
        assertThat(testMetric.getReq999percentile()).isEqualTo(DEFAULT_REQ_999_PERCENTILE);
        assertThat(testMetric.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
    }

    @Test
    public void getAllMetrics() throws Exception {
        // Initialize the database
        metricRepository.save(metric);

        // Get all the metrics
        restMetricMockMvc.perform(get("/api/metrics?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].metricId").value(hasItem(metric.getMetricId().toString())))
                .andExpect(jsonPath("$.[*].connectionErrors").value(hasItem(DEFAULT_CONNECTION_ERRORS)))
                .andExpect(jsonPath("$.[*].writeTimeouts").value(hasItem(DEFAULT_WRITE_TIMEOUTS)))
                .andExpect(jsonPath("$.[*].readTimeouts").value(hasItem(DEFAULT_READ_TIMEOUTS)))
                .andExpect(jsonPath("$.[*].unavailables").value(hasItem(DEFAULT_UNAVAILABLES)))
                .andExpect(jsonPath("$.[*].otherErrors").value(hasItem(DEFAULT_OTHER_ERRORS)))
                .andExpect(jsonPath("$.[*].retries").value(hasItem(DEFAULT_RETRIES)))
                .andExpect(jsonPath("$.[*].ignores").value(hasItem(DEFAULT_IGNORES)))
                .andExpect(jsonPath("$.[*].knownHosts").value(hasItem(DEFAULT_KNOWN_HOSTS)))
                .andExpect(jsonPath("$.[*].connectedTo").value(hasItem(DEFAULT_CONNECTED_TO)))
                .andExpect(jsonPath("$.[*].openConnections").value(hasItem(DEFAULT_OPEN_CONNECTIONS)))
                .andExpect(jsonPath("$.[*].reqCount").value(hasItem(DEFAULT_REQ_COUNT)))
                .andExpect(jsonPath("$.[*].reqMinLatency").value(hasItem(DEFAULT_REQ_MIN_LATENCY.doubleValue())))
                .andExpect(jsonPath("$.[*].reqMaxLatency").value(hasItem(DEFAULT_REQ_MAX_LATENCY.doubleValue())))
                .andExpect(jsonPath("$.[*].reqMeanLatency").value(hasItem(DEFAULT_REQ_MEAN_LATENCY.doubleValue())))
                .andExpect(jsonPath("$.[*].reqStdev").value(hasItem(DEFAULT_REQ_STDEV.doubleValue())))
                .andExpect(jsonPath("$.[*].reqMedian").value(hasItem(DEFAULT_REQ_MEDIAN.doubleValue())))
                .andExpect(jsonPath("$.[*].req75percentile").value(hasItem(DEFAULT_REQ_75_PERCENTILE.doubleValue())))
                .andExpect(jsonPath("$.[*].req97percentile").value(hasItem(DEFAULT_REQ_97_PERCENTILE.doubleValue())))
                .andExpect(jsonPath("$.[*].req98percentile").value(hasItem(DEFAULT_REQ_98_PERCENTILE.doubleValue())))
                .andExpect(jsonPath("$.[*].req99percentile").value(hasItem(DEFAULT_REQ_99_PERCENTILE.doubleValue())))
                .andExpect(jsonPath("$.[*].req999percentile").value(hasItem(DEFAULT_REQ_999_PERCENTILE.doubleValue())))
                .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.getTime())));
    }

    @Test
    public void getMetric() throws Exception {
        // Initialize the database
        metricRepository.save(metric);

        // Get the metric
        restMetricMockMvc.perform(get("/api/metrics/{id}", metric.getMetricId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.metricId").value(metric.getMetricId().toString()))
            .andExpect(jsonPath("$.connectionErrors").value(DEFAULT_CONNECTION_ERRORS))
            .andExpect(jsonPath("$.writeTimeouts").value(DEFAULT_WRITE_TIMEOUTS))
            .andExpect(jsonPath("$.readTimeouts").value(DEFAULT_READ_TIMEOUTS))
            .andExpect(jsonPath("$.unavailables").value(DEFAULT_UNAVAILABLES))
            .andExpect(jsonPath("$.otherErrors").value(DEFAULT_OTHER_ERRORS))
            .andExpect(jsonPath("$.retries").value(DEFAULT_RETRIES))
            .andExpect(jsonPath("$.ignores").value(DEFAULT_IGNORES))
            .andExpect(jsonPath("$.knownHosts").value(DEFAULT_KNOWN_HOSTS))
            .andExpect(jsonPath("$.connectedTo").value(DEFAULT_CONNECTED_TO))
            .andExpect(jsonPath("$.openConnections").value(DEFAULT_OPEN_CONNECTIONS))
            .andExpect(jsonPath("$.reqCount").value(DEFAULT_REQ_COUNT))
            .andExpect(jsonPath("$.reqMinLatency").value(DEFAULT_REQ_MIN_LATENCY.doubleValue()))
            .andExpect(jsonPath("$.reqMaxLatency").value(DEFAULT_REQ_MAX_LATENCY.doubleValue()))
            .andExpect(jsonPath("$.reqMeanLatency").value(DEFAULT_REQ_MEAN_LATENCY.doubleValue()))
            .andExpect(jsonPath("$.reqStdev").value(DEFAULT_REQ_STDEV.doubleValue()))
            .andExpect(jsonPath("$.reqMedian").value(DEFAULT_REQ_MEDIAN.doubleValue()))
            .andExpect(jsonPath("$.req75percentile").value(DEFAULT_REQ_75_PERCENTILE.doubleValue()))
            .andExpect(jsonPath("$.req97percentile").value(DEFAULT_REQ_97_PERCENTILE.doubleValue()))
            .andExpect(jsonPath("$.req98percentile").value(DEFAULT_REQ_98_PERCENTILE.doubleValue()))
            .andExpect(jsonPath("$.req99percentile").value(DEFAULT_REQ_99_PERCENTILE.doubleValue()))
            .andExpect(jsonPath("$.req999percentile").value(DEFAULT_REQ_999_PERCENTILE.doubleValue()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.getTime()));
    }

    @Test
    public void getNonExistingMetric() throws Exception {
        // Get the metric
        restMetricMockMvc.perform(get("/api/metrics/{id}", UUID.randomUUID().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateMetric() throws Exception {
        // Initialize the database
        metricRepository.save(metric);
        int databaseSizeBeforeUpdate = metricRepository.findAll().size();

        // Update the metric
        Metric updatedMetric = new Metric();
        updatedMetric.setMetricId(metric.getMetricId());
        updatedMetric.setConnectionErrors(UPDATED_CONNECTION_ERRORS);
        updatedMetric.setWriteTimeouts(UPDATED_WRITE_TIMEOUTS);
        updatedMetric.setReadTimeouts(UPDATED_READ_TIMEOUTS);
        updatedMetric.setUnavailables(UPDATED_UNAVAILABLES);
        updatedMetric.setOtherErrors(UPDATED_OTHER_ERRORS);
        updatedMetric.setRetries(UPDATED_RETRIES);
        updatedMetric.setIgnores(UPDATED_IGNORES);
        updatedMetric.setKnownHosts(UPDATED_KNOWN_HOSTS);
        updatedMetric.setConnectedTo(UPDATED_CONNECTED_TO);
        updatedMetric.setOpenConnections(UPDATED_OPEN_CONNECTIONS);
        updatedMetric.setReqCount(UPDATED_REQ_COUNT);
        updatedMetric.setReqMinLatency(UPDATED_REQ_MIN_LATENCY);
        updatedMetric.setReqMaxLatency(UPDATED_REQ_MAX_LATENCY);
        updatedMetric.setReqMeanLatency(UPDATED_REQ_MEAN_LATENCY);
        updatedMetric.setReqStdev(UPDATED_REQ_STDEV);
        updatedMetric.setReqMedian(UPDATED_REQ_MEDIAN);
        updatedMetric.setReq75percentile(UPDATED_REQ_75_PERCENTILE);
        updatedMetric.setReq97percentile(UPDATED_REQ_97_PERCENTILE);
        updatedMetric.setReq98percentile(UPDATED_REQ_98_PERCENTILE);
        updatedMetric.setReq99percentile(UPDATED_REQ_99_PERCENTILE);
        updatedMetric.setReq999percentile(UPDATED_REQ_999_PERCENTILE);
        updatedMetric.setDateCreated(UPDATED_DATE_CREATED);

        restMetricMockMvc.perform(put("/api/metrics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMetric)))
                .andExpect(status().isOk());

        // Validate the Metric in the database
        List<Metric> metrics = metricRepository.findAll();
        assertThat(metrics).hasSize(databaseSizeBeforeUpdate);
        Metric testMetric = metrics.get(metrics.size() - 1);
        assertThat(testMetric.getMetricId()).isEqualTo(UPDATED_METRIC_ID);
        assertThat(testMetric.getConnectionErrors()).isEqualTo(UPDATED_CONNECTION_ERRORS);
        assertThat(testMetric.getWriteTimeouts()).isEqualTo(UPDATED_WRITE_TIMEOUTS);
        assertThat(testMetric.getReadTimeouts()).isEqualTo(UPDATED_READ_TIMEOUTS);
        assertThat(testMetric.getUnavailables()).isEqualTo(UPDATED_UNAVAILABLES);
        assertThat(testMetric.getOtherErrors()).isEqualTo(UPDATED_OTHER_ERRORS);
        assertThat(testMetric.getRetries()).isEqualTo(UPDATED_RETRIES);
        assertThat(testMetric.getIgnores()).isEqualTo(UPDATED_IGNORES);
        assertThat(testMetric.getKnownHosts()).isEqualTo(UPDATED_KNOWN_HOSTS);
        assertThat(testMetric.getConnectedTo()).isEqualTo(UPDATED_CONNECTED_TO);
        assertThat(testMetric.getOpenConnections()).isEqualTo(UPDATED_OPEN_CONNECTIONS);
        assertThat(testMetric.getReqCount()).isEqualTo(UPDATED_REQ_COUNT);
        assertThat(testMetric.getReqMinLatency()).isEqualTo(UPDATED_REQ_MIN_LATENCY);
        assertThat(testMetric.getReqMaxLatency()).isEqualTo(UPDATED_REQ_MAX_LATENCY);
        assertThat(testMetric.getReqMeanLatency()).isEqualTo(UPDATED_REQ_MEAN_LATENCY);
        assertThat(testMetric.getReqStdev()).isEqualTo(UPDATED_REQ_STDEV);
        assertThat(testMetric.getReqMedian()).isEqualTo(UPDATED_REQ_MEDIAN);
        assertThat(testMetric.getReq75percentile()).isEqualTo(UPDATED_REQ_75_PERCENTILE);
        assertThat(testMetric.getReq97percentile()).isEqualTo(UPDATED_REQ_97_PERCENTILE);
        assertThat(testMetric.getReq98percentile()).isEqualTo(UPDATED_REQ_98_PERCENTILE);
        assertThat(testMetric.getReq99percentile()).isEqualTo(UPDATED_REQ_99_PERCENTILE);
        assertThat(testMetric.getReq999percentile()).isEqualTo(UPDATED_REQ_999_PERCENTILE);
        assertThat(testMetric.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
    }

    @Test
    public void deleteMetric() throws Exception {
        // Initialize the database
        metricRepository.save(metric);
        int databaseSizeBeforeDelete = metricRepository.findAll().size();

        // Get the metric
        restMetricMockMvc.perform(delete("/api/metrics/{id}", metric.getMetricId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Metric> metrics = metricRepository.findAll();
        assertThat(metrics).hasSize(databaseSizeBeforeDelete - 1);
    }
}
