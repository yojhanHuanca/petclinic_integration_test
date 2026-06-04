package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.repositories.VetRepository;
import com.tecsup.petclinic.services.VetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for VetController
 * Tests all CRUD operations and error scenarios using WebMvcTest
 * 
 * @author jgomezm
 */
@WebMvcTest(VetController.class)
@DisplayName("Vet Controller Integration Tests")
class VetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VetService vetService;

    @Autowired
    private ObjectMapper objectMapper;

    private VetDTO testVetDTO;
    private Vet testVet;

    @BeforeEach
    void setUp() {
        // Setup test data
        testVet = new Vet();
        testVet.setId(1);
        testVet.setFirstName("James");
        testVet.setLastName("Carter");

        testVetDTO = VetDTO.builder()
                .id(1)
                .firstName("James")
                .lastName("Carter")
                .build();
    }

    // ============ TESTS FOR GET ALL VETS ============

    @Test
    @DisplayName("Should return all vets with status 200")
    void testGetAllVets() throws Exception {
        List<Vet> vetList = new ArrayList<>();
        vetList.add(testVet);
        when(vetService.findAll()).thenReturn(vetList);

        mockMvc.perform(get("/vets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(vetService, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return vets in JSON format")
    void testGetAllVetsJsonFormat() throws Exception {
        List<Vet> vetList = new ArrayList<>();
        vetList.add(testVet);
        when(vetService.findAll()).thenReturn(vetList);

        MvcResult result = mockMvc.perform(get("/vets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).contains("James").contains("Carter");
    }

    @Test
    @DisplayName("Should return empty list when no vets exist")
    void testGetAllVetsEmpty() throws Exception {
        when(vetService.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/vets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // ============ TESTS FOR GET VET BY ID ============

    @Test
    @DisplayName("Should return vet with status 200 when id exists")
    void testGetVetById() throws Exception {
        when(vetService.findById(1)).thenReturn(testVetDTO);

        mockMvc.perform(get("/vets/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.firstName", equalTo("James")))
                .andExpect(jsonPath("$.lastName", equalTo("Carter")));

        verify(vetService, times(1)).findById(1);
    }

    @Test
    @DisplayName("Should return 404 when vet id does not exist")
    void testGetVetByIdNotFound() throws Exception {
        when(vetService.findById(9999))
                .thenThrow(new IllegalArgumentException("Vet not found with id: 9999"));

        mockMvc.perform(get("/vets/9999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should verify vet details are correct")
    void testGetVetByIdDetails() throws Exception {
        when(vetService.findById(1)).thenReturn(testVetDTO);

        MvcResult result = mockMvc.perform(get("/vets/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        VetDTO responseVet = objectMapper.readValue(content, VetDTO.class);

        assertThat(responseVet.getId()).isNotNull();
        assertThat(responseVet.getFirstName()).isEqualTo("James");
        assertThat(responseVet.getLastName()).isEqualTo("Carter");
    }

    // ============ TESTS FOR CREATE VET ============

    @Test
    @DisplayName("Should create a new vet and return status 201")
    void testCreateVet() throws Exception {
        VetDTO newVetDTO = VetDTO.builder()
                .firstName("John")
                .lastName("Smith")
                .build();
        
        VetDTO createdVetDTO = VetDTO.builder()
                .id(2)
                .firstName("John")
                .lastName("Smith")
                .build();
        
        when(vetService.create(ArgumentMatchers.any(VetDTO.class))).thenReturn(createdVetDTO);

        mockMvc.perform(post("/vets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newVetDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", equalTo("John")))
                .andExpect(jsonPath("$.lastName", equalTo("Smith")));

        verify(vetService, times(1)).create(ArgumentMatchers.any(VetDTO.class));
    }

    @Test
    @DisplayName("Should return created vet with generated id")
    void testCreateVetWithId() throws Exception {
        VetDTO newVetDTO = VetDTO.builder()
                .firstName("John")
                .lastName("Smith")
                .build();
        
        VetDTO createdVetDTO = VetDTO.builder()
                .id(2)
                .firstName("John")
                .lastName("Smith")
                .build();
        
        when(vetService.create(ArgumentMatchers.any(VetDTO.class))).thenReturn(createdVetDTO);

        MvcResult result = mockMvc.perform(post("/vets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newVetDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        VetDTO responseVet = objectMapper.readValue(content, VetDTO.class);

        assertThat(responseVet.getId()).isNotNull().isEqualTo(2);
        assertThat(responseVet.getFirstName()).isEqualTo("John");
    }

    // ============ TESTS FOR UPDATE VET ============

    @Test
    @DisplayName("Should update an existing vet and return status 200")
    void testUpdateVet() throws Exception {
        VetDTO updateVetDTO = VetDTO.builder()
                .id(1)
                .firstName("UpdatedJohn")
                .lastName("UpdatedSmith")
                .build();

        VetDTO updatedVetDTO = VetDTO.builder()
                .id(1)
                .firstName("UpdatedJohn")
                .lastName("UpdatedSmith")
                .build();

        doNothing().when(vetService).update(ArgumentMatchers.any(VetDTO.class));
        when(vetService.findById(1)).thenReturn(updatedVetDTO);

        mockMvc.perform(put("/vets/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateVetDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo("UpdatedJohn")))
                .andExpect(jsonPath("$.lastName", equalTo("UpdatedSmith")));

        verify(vetService, times(1)).update(ArgumentMatchers.any(VetDTO.class));
    }

    @Test
    @DisplayName("Should return 404 when trying to update non-existent vet")
    void testUpdateVetNotFound() throws Exception {
        VetDTO updateVetDTO = VetDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .build();

        doThrow(new IllegalArgumentException("Vet not found with id: 9999"))
                .when(vetService).update(ArgumentMatchers.any(VetDTO.class));

        mockMvc.perform(put("/vets/9999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateVetDTO)))
                .andExpect(status().isNotFound());
    }

    // ============ TESTS FOR DELETE VET ============

    @Test
    @DisplayName("Should delete vet and return status 204")
    void testDeleteVet() throws Exception {
        doNothing().when(vetService).delete(1);

        mockMvc.perform(delete("/vets/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(vetService, times(1)).delete(1);
    }

    @Test
    @DisplayName("Should return 404 when trying to delete non-existent vet")
    void testDeleteVetNotFound() throws Exception {
        doThrow(new IllegalArgumentException("Vet not found with id: 9999"))
                .when(vetService).delete(9999);

        mockMvc.perform(delete("/vets/9999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // ============ INTEGRATION TESTS ============

    @Test
    @DisplayName("Should execute complete CRUD workflow")
    void testCompleteCrudWorkflow() throws Exception {
        // CREATE
        VetDTO newVetDTO = VetDTO.builder()
                .firstName("John")
                .lastName("Smith")
                .build();
        
        VetDTO createdVetDTO = VetDTO.builder()
                .id(2)
                .firstName("John")
                .lastName("Smith")
                .build();
        
        when(vetService.create(ArgumentMatchers.any(VetDTO.class))).thenReturn(createdVetDTO);

        MvcResult createResult = mockMvc.perform(post("/vets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newVetDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        String createContent = createResult.getResponse().getContentAsString();
        VetDTO createdVet = objectMapper.readValue(createContent, VetDTO.class);
        Integer createdId = createdVet.getId();

        // READ
        when(vetService.findById(createdId)).thenReturn(createdVetDTO);
        
        mockMvc.perform(get("/vets/" + createdId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo("John")));

        // UPDATE
        VetDTO updateVet = VetDTO.builder()
                .id(createdId)
                .firstName("UpdatedJohn")
                .lastName("UpdatedSmith")
                .build();

        VetDTO updatedVetDTO = VetDTO.builder()
                .id(createdId)
                .firstName("UpdatedJohn")
                .lastName("UpdatedSmith")
                .build();

        doNothing().when(vetService).update(ArgumentMatchers.any(VetDTO.class));
        when(vetService.findById(createdId)).thenReturn(updatedVetDTO);

        mockMvc.perform(put("/vets/" + createdId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateVet)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo("UpdatedJohn")));

        // DELETE
        doNothing().when(vetService).delete(createdId);
        
        mockMvc.perform(delete("/vets/" + createdId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // VERIFY DELETION
        when(vetService.findById(createdId))
                .thenThrow(new IllegalArgumentException("Vet not found with id: " + createdId));
        
        mockMvc.perform(get("/vets/" + createdId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should handle multiple vets correctly")
    void testMultipleVets() throws Exception {
        List<Vet> vetList = new ArrayList<>();
        vetList.add(testVet);
        
        Vet vet2 = new Vet();
        vet2.setId(2);
        vet2.setFirstName("Alice");
        vet2.setLastName("Johnson");
        vetList.add(vet2);
        
        Vet vet3 = new Vet();
        vet3.setId(3);
        vet3.setFirstName("Bob");
        vet3.setLastName("Williams");
        vetList.add(vet3);

        when(vetService.findAll()).thenReturn(vetList);

        mockMvc.perform(get("/vets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        verify(vetService, times(1)).findAll();
    }
}