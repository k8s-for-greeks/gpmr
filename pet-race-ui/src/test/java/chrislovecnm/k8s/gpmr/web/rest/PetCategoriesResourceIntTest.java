package chrislovecnm.k8s.gpmr.web.rest;

import chrislovecnm.k8s.gpmr.AbstractCassandraTest;
import chrislovecnm.k8s.gpmr.GpmrApp;
import chrislovecnm.k8s.gpmr.domain.PetCategories;
import chrislovecnm.k8s.gpmr.repository.PetCategoriesRepository;
import chrislovecnm.k8s.gpmr.service.PetCategoriesService;

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
 * Test class for the PetCategoriesResource REST controller.
 *
 * @see PetCategoriesResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GpmrApp.class)
@WebAppConfiguration
@IntegrationTest
public class PetCategoriesResourceIntTest extends AbstractCassandraTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private PetCategoriesRepository petCategoriesRepository;

    @Inject
    private PetCategoriesService petCategoriesService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPetCategoriesMockMvc;

    private PetCategories petCategories;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PetCategoriesResource petCategoriesResource = new PetCategoriesResource();
        ReflectionTestUtils.setField(petCategoriesResource, "petCategoriesService", petCategoriesService);
        this.restPetCategoriesMockMvc = MockMvcBuilders.standaloneSetup(petCategoriesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        petCategoriesRepository.deleteAll();
        petCategories = new PetCategories();
        petCategories.setName(DEFAULT_NAME);
    }

    @Test
    public void createPetCategories() throws Exception {
        int databaseSizeBeforeCreate = petCategoriesRepository.findAll().size();

        // Create the PetCategories

        restPetCategoriesMockMvc.perform(post("/api/pet-categories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(petCategories)))
                .andExpect(status().isCreated());

        // Validate the PetCategories in the database
        List<PetCategories> petCategories = petCategoriesRepository.findAll();
        assertThat(petCategories).hasSize(databaseSizeBeforeCreate + 1);
        PetCategories testPetCategories = petCategories.get(petCategories.size() - 1);
        assertThat(testPetCategories.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    public void getAllPetCategories() throws Exception {
        // Initialize the database
        petCategoriesRepository.save(petCategories);

        // Get all the petCategories
        restPetCategoriesMockMvc.perform(get("/api/pet-categories?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(petCategories.getId().toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    public void getPetCategories() throws Exception {
        // Initialize the database
        petCategoriesRepository.save(petCategories);

        // Get the petCategories
        restPetCategoriesMockMvc.perform(get("/api/pet-categories/{id}", petCategories.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(petCategories.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    public void getNonExistingPetCategories() throws Exception {
        // Get the petCategories
        restPetCategoriesMockMvc.perform(get("/api/pet-categories/{id}", UUID.randomUUID().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updatePetCategories() throws Exception {
        // Initialize the database
        petCategoriesService.save(petCategories);

        int databaseSizeBeforeUpdate = petCategoriesRepository.findAll().size();

        // Update the petCategories
        PetCategories updatedPetCategories = new PetCategories();
        updatedPetCategories.setId(petCategories.getId());
        updatedPetCategories.setName(UPDATED_NAME);

        restPetCategoriesMockMvc.perform(put("/api/pet-categories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPetCategories)))
                .andExpect(status().isOk());

        // Validate the PetCategories in the database
        List<PetCategories> petCategories = petCategoriesRepository.findAll();
        assertThat(petCategories).hasSize(databaseSizeBeforeUpdate);
        PetCategories testPetCategories = petCategories.get(petCategories.size() - 1);
        assertThat(testPetCategories.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    public void deletePetCategories() throws Exception {
        // Initialize the database
        petCategoriesService.save(petCategories);

        int databaseSizeBeforeDelete = petCategoriesRepository.findAll().size();

        // Get the petCategories
        restPetCategoriesMockMvc.perform(delete("/api/pet-categories/{id}", petCategories.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PetCategories> petCategories = petCategoriesRepository.findAll();
        assertThat(petCategories).hasSize(databaseSizeBeforeDelete - 1);
    }
}
