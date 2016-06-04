package chrislovecnm.k8s.gpmr.web.rest;

import chrislovecnm.k8s.gpmr.AbstractCassandraTest;
import chrislovecnm.k8s.gpmr.GpmrApp;
import chrislovecnm.k8s.gpmr.domain.Pet;
import chrislovecnm.k8s.gpmr.repository.PetRepository;
import chrislovecnm.k8s.gpmr.service.PetService;

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
 * Test class for the PetResource REST controller.
 *
 * @see PetResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GpmrApp.class)
@WebAppConfiguration
@IntegrationTest
public class PetResourceIntTest extends AbstractCassandraTest {


    private static final UUID DEFAULT_PET_ID = UUID.randomUUID();
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_PET_CATEGORY_NAME = "AAAAA";
    private static final String UPDATED_PET_CATEGORY_NAME = "BBBBB";

    private static final UUID DEFAULT_PET_CATEGORY_ID = UUID.randomUUID();
    private static final UUID UPDATED_PET_CATEGORY_ID = UUID.randomUUID();

    private static final Float DEFAULT_PET_SPEED = 1F;
    private static final Float UPDATED_PET_SPEED = 2F;

    @Inject
    private PetRepository petRepository;

    @Inject
    private PetService petService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPetMockMvc;

    private Pet pet;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PetResource petResource = new PetResource();
        ReflectionTestUtils.setField(petResource, "petService", petService);
        this.restPetMockMvc = MockMvcBuilders.standaloneSetup(petResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        petRepository.deleteAll();
        pet = new Pet();
        pet.setPetId(DEFAULT_PET_ID);
        pet.setName(DEFAULT_NAME);
        pet.setDescription(DEFAULT_DESCRIPTION);
        pet.setPetCategoryName(DEFAULT_PET_CATEGORY_NAME);
        pet.setPetCategoryId(DEFAULT_PET_CATEGORY_ID);
        pet.setPetSpeed(DEFAULT_PET_SPEED);
    }

    @Test
    public void createPet() throws Exception {
        int databaseSizeBeforeCreate = petRepository.findAll().size();

        // Create the Pet
        pet.setPetId(null);

        restPetMockMvc.perform(post("/api/pets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pet)))
                .andExpect(status().isCreated());

        // Validate the Pet in the database
        List<Pet> pets = petRepository.findAll();
        assertThat(pets).hasSize(databaseSizeBeforeCreate + 1);
        Pet testPet = pets.get(pets.size() - 1);
        assertThat(testPet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPet.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPet.getPetCategoryName()).isEqualTo(DEFAULT_PET_CATEGORY_NAME);
        assertThat(testPet.getPetCategoryId()).isEqualTo(DEFAULT_PET_CATEGORY_ID);
        assertThat(testPet.getPetSpeed()).isEqualTo(DEFAULT_PET_SPEED);
        pet.setPetId(DEFAULT_PET_ID);
    }

    @Test
    public void getAllPets() throws Exception {
        // Initialize the database
        petRepository.save(pet);

        // Get all the pets
        restPetMockMvc.perform(get("/api/pets?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].petId").value(hasItem(pet.getPetId().toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].petCategoryName").value(hasItem(DEFAULT_PET_CATEGORY_NAME.toString())))
                .andExpect(jsonPath("$.[*].petCategoryId").value(hasItem(DEFAULT_PET_CATEGORY_ID.toString())))
                .andExpect(jsonPath("$.[*].petSpeed").value(hasItem(DEFAULT_PET_SPEED.doubleValue())));
    }

    @Test
    public void getPet() throws Exception {
        // Initialize the database
        petRepository.save(pet);

        // Get the pet
        restPetMockMvc.perform(get("/api/pets/{id}", pet.getPetId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.petId").value(pet.getPetId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.petCategoryName").value(DEFAULT_PET_CATEGORY_NAME.toString()))
            .andExpect(jsonPath("$.petCategoryId").value(DEFAULT_PET_CATEGORY_ID.toString()))
            .andExpect(jsonPath("$.petSpeed").value(DEFAULT_PET_SPEED.doubleValue()));
    }

    @Test
    public void getNonExistingPet() throws Exception {
        // Get the pet
        restPetMockMvc.perform(get("/api/pets/{id}", UUID.randomUUID().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updatePet() throws Exception {
        // Initialize the database
        petService.save(pet);

        int databaseSizeBeforeUpdate = petRepository.findAll().size();

        // Update the pet
        Pet updatedPet = new Pet();
        updatedPet.setPetId(pet.getPetId());
        updatedPet.setName(UPDATED_NAME);
        updatedPet.setDescription(UPDATED_DESCRIPTION);
        updatedPet.setPetCategoryName(UPDATED_PET_CATEGORY_NAME);
        updatedPet.setPetCategoryId(UPDATED_PET_CATEGORY_ID);
        updatedPet.setPetSpeed(UPDATED_PET_SPEED);

        restPetMockMvc.perform(put("/api/pets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPet)))
                .andExpect(status().isOk());

        // Validate the Pet in the database
        List<Pet> pets = petRepository.findAll();
        assertThat(pets).hasSize(databaseSizeBeforeUpdate);
        Pet testPet = pets.get(pets.size() - 1);
        assertThat(testPet.getPetId()).isEqualTo(pet.getPetId());
        assertThat(testPet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPet.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPet.getPetCategoryName()).isEqualTo(UPDATED_PET_CATEGORY_NAME);
        assertThat(testPet.getPetCategoryId()).isEqualTo(UPDATED_PET_CATEGORY_ID);
        assertThat(testPet.getPetSpeed()).isEqualTo(UPDATED_PET_SPEED);
    }

    @Test
    public void deletePet() throws Exception {
        // Initialize the database
        petService.save(pet);

        int databaseSizeBeforeDelete = petRepository.findAll().size();

        // Get the pet
        restPetMockMvc.perform(delete("/api/pets/{id}", pet.getPetId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Pet> pets = petRepository.findAll();
        assertThat(pets).hasSize(databaseSizeBeforeDelete - 1);
    }
}
