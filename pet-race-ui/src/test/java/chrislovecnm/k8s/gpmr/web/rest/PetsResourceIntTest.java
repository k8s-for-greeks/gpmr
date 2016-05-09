package chrislovecnm.k8s.gpmr.web.rest;

import chrislovecnm.k8s.gpmr.AbstractCassandraTest;
import chrislovecnm.k8s.gpmr.GpmrApp;
import chrislovecnm.k8s.gpmr.domain.Pets;
import chrislovecnm.k8s.gpmr.repository.PetsRepository;
import chrislovecnm.k8s.gpmr.service.PetsService;

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
import java.math.BigDecimal;;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PetsResource REST controller.
 *
 * @see PetsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GpmrApp.class)
@WebAppConfiguration
@IntegrationTest
public class PetsResourceIntTest extends AbstractCassandraTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_PET_CATEGORY = "AAAAA";
    private static final String UPDATED_PET_CATEGORY = "BBBBB";

    private static final UUID DEFAULT_PET_CATEGORY_ID = UUID.randomUUID();
    private static final UUID UPDATED_PET_CATEGORY_ID = UUID.randomUUID();

    private static final BigDecimal DEFAULT_PET_SPEED = new BigDecimal(1);
    private static final BigDecimal UPDATED_PET_SPEED = new BigDecimal(2);

    @Inject
    private PetsRepository petsRepository;

    @Inject
    private PetsService petsService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPetsMockMvc;

    private Pets pets;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PetsResource petsResource = new PetsResource();
        ReflectionTestUtils.setField(petsResource, "petsService", petsService);
        this.restPetsMockMvc = MockMvcBuilders.standaloneSetup(petsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        petsRepository.deleteAll();
        pets = new Pets();
        pets.setName(DEFAULT_NAME);
        pets.setPetCategory(DEFAULT_PET_CATEGORY);
        pets.setPetCategoryId(DEFAULT_PET_CATEGORY_ID);
        pets.setPetSpeed(DEFAULT_PET_SPEED);
    }

    @Test
    public void createPets() throws Exception {
        int databaseSizeBeforeCreate = petsRepository.findAll().size();

        // Create the Pets

        restPetsMockMvc.perform(post("/api/pets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pets)))
                .andExpect(status().isCreated());

        // Validate the Pets in the database
        List<Pets> pets = petsRepository.findAll();
        assertThat(pets).hasSize(databaseSizeBeforeCreate + 1);
        Pets testPets = pets.get(pets.size() - 1);
        assertThat(testPets.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPets.getPetCategory()).isEqualTo(DEFAULT_PET_CATEGORY);
        assertThat(testPets.getPetCategoryId()).isEqualTo(DEFAULT_PET_CATEGORY_ID);
        assertThat(testPets.getPetSpeed()).isEqualTo(DEFAULT_PET_SPEED);
    }

    @Test
    public void getAllPets() throws Exception {
        // Initialize the database
        petsRepository.save(pets);

        // Get all the pets
        restPetsMockMvc.perform(get("/api/pets?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(pets.getId().toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].petCategory").value(hasItem(DEFAULT_PET_CATEGORY.toString())))
                .andExpect(jsonPath("$.[*].petCategoryId").value(hasItem(DEFAULT_PET_CATEGORY_ID.toString())))
                .andExpect(jsonPath("$.[*].petSpeed").value(hasItem(DEFAULT_PET_SPEED.intValue())));
    }

    @Test
    public void getPets() throws Exception {
        // Initialize the database
        petsRepository.save(pets);

        // Get the pets
        restPetsMockMvc.perform(get("/api/pets/{id}", pets.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(pets.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.petCategory").value(DEFAULT_PET_CATEGORY.toString()))
            .andExpect(jsonPath("$.petCategoryId").value(DEFAULT_PET_CATEGORY_ID.toString()))
            .andExpect(jsonPath("$.petSpeed").value(DEFAULT_PET_SPEED.intValue()));
    }

    @Test
    public void getNonExistingPets() throws Exception {
        // Get the pets
        restPetsMockMvc.perform(get("/api/pets/{id}", UUID.randomUUID().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updatePets() throws Exception {
        // Initialize the database
        petsService.save(pets);

        int databaseSizeBeforeUpdate = petsRepository.findAll().size();

        // Update the pets
        Pets updatedPets = new Pets();
        updatedPets.setId(pets.getId());
        updatedPets.setName(UPDATED_NAME);
        updatedPets.setPetCategory(UPDATED_PET_CATEGORY);
        updatedPets.setPetCategoryId(UPDATED_PET_CATEGORY_ID);
        updatedPets.setPetSpeed(UPDATED_PET_SPEED);

        restPetsMockMvc.perform(put("/api/pets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPets)))
                .andExpect(status().isOk());

        // Validate the Pets in the database
        List<Pets> pets = petsRepository.findAll();
        assertThat(pets).hasSize(databaseSizeBeforeUpdate);
        Pets testPets = pets.get(pets.size() - 1);
        assertThat(testPets.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPets.getPetCategory()).isEqualTo(UPDATED_PET_CATEGORY);
        assertThat(testPets.getPetCategoryId()).isEqualTo(UPDATED_PET_CATEGORY_ID);
        assertThat(testPets.getPetSpeed()).isEqualTo(UPDATED_PET_SPEED);
    }

    @Test
    public void deletePets() throws Exception {
        // Initialize the database
        petsService.save(pets);

        int databaseSizeBeforeDelete = petsRepository.findAll().size();

        // Get the pets
        restPetsMockMvc.perform(delete("/api/pets/{id}", pets.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Pets> pets = petsRepository.findAll();
        assertThat(pets).hasSize(databaseSizeBeforeDelete - 1);
    }
}
