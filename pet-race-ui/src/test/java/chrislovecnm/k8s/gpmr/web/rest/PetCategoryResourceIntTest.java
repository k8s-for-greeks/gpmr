package chrislovecnm.k8s.gpmr.web.rest;

import chrislovecnm.k8s.gpmr.AbstractCassandraTest;
import chrislovecnm.k8s.gpmr.GpmrApp;
import chrislovecnm.k8s.gpmr.domain.PetCategory;
import chrislovecnm.k8s.gpmr.repository.PetCategoryRepository;
import chrislovecnm.k8s.gpmr.service.PetCategoryService;

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
 * Test class for the PetCategoryResource REST controller.
 *
 * @see PetCategoryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GpmrApp.class)
@WebAppConfiguration
@IntegrationTest
public class PetCategoryResourceIntTest extends AbstractCassandraTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private PetCategoryRepository petCategoryRepository;

    @Inject
    private PetCategoryService petCategoryService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPetCategoryMockMvc;

    private PetCategory petCategory;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PetCategoryResource petCategoryResource = new PetCategoryResource();
        ReflectionTestUtils.setField(petCategoryResource, "petCategoryService", petCategoryService);
        this.restPetCategoryMockMvc = MockMvcBuilders.standaloneSetup(petCategoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        petCategoryRepository.deleteAll();
        petCategory = new PetCategory();
        petCategory.setName(DEFAULT_NAME);
    }

    @Test
    public void createPetCategory() throws Exception {
        int databaseSizeBeforeCreate = petCategoryRepository.findAll().size();

        // Create the PetCategory

        restPetCategoryMockMvc.perform(post("/api/pet-categories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(petCategory)))
                .andExpect(status().isCreated());

        // Validate the PetCategory in the database
        List<PetCategory> petCategories = petCategoryRepository.findAll();
        assertThat(petCategories).hasSize(databaseSizeBeforeCreate + 1);
        PetCategory testPetCategory = petCategories.get(petCategories.size() - 1);
        assertThat(testPetCategory.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    public void getAllPetCategories() throws Exception {
        // Initialize the database
        petCategoryRepository.save(petCategory);

        // Get all the petCategories
        restPetCategoryMockMvc.perform(get("/api/pet-categories?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(petCategory.getId().toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    public void getPetCategory() throws Exception {
        // Initialize the database
        petCategoryRepository.save(petCategory);

        // Get the petCategory
        restPetCategoryMockMvc.perform(get("/api/pet-categories/{id}", petCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(petCategory.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    public void getNonExistingPetCategory() throws Exception {
        // Get the petCategory
        restPetCategoryMockMvc.perform(get("/api/pet-categories/{id}", UUID.randomUUID().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updatePetCategory() throws Exception {
        // Initialize the database
        petCategoryService.save(petCategory);

        int databaseSizeBeforeUpdate = petCategoryRepository.findAll().size();

        // Update the petCategory
        PetCategory updatedPetCategory = new PetCategory();
        updatedPetCategory.setId(petCategory.getId());
        updatedPetCategory.setName(UPDATED_NAME);

        restPetCategoryMockMvc.perform(put("/api/pet-categories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPetCategory)))
                .andExpect(status().isOk());

        // Validate the PetCategory in the database
        List<PetCategory> petCategories = petCategoryRepository.findAll();
        assertThat(petCategories).hasSize(databaseSizeBeforeUpdate);
        PetCategory testPetCategory = petCategories.get(petCategories.size() - 1);
        assertThat(testPetCategory.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    public void deletePetCategory() throws Exception {
        // Initialize the database
        petCategoryService.save(petCategory);

        int databaseSizeBeforeDelete = petCategoryRepository.findAll().size();

        // Get the petCategory
        restPetCategoryMockMvc.perform(delete("/api/pet-categories/{id}", petCategory.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PetCategory> petCategories = petCategoryRepository.findAll();
        assertThat(petCategories).hasSize(databaseSizeBeforeDelete - 1);
    }
}
