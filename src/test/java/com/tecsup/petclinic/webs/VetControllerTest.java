package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.services.VetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@DisplayName("Vet Controller Integration Tests")
public class VetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VetService vetService;

    private VetDTO testVetDTO;

    @BeforeEach
    void setUp() {
        testVetDTO = VetDTO.builder().id(1).firstName("James").lastName("Carter").build();
    }

    private static Vet vet(int id, String firstName, String lastName) {
        Vet vet = new Vet();
        vet.setId(id);
        vet.setFirstName(firstName);
        vet.setLastName(lastName);
        return vet;
    }

    @Test
    void shouldListVets() throws Exception {
        when(vetService.findAll()).thenReturn(List.of(vet(1, "James", "Carter")));

        mockMvc.perform(get("/vets").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("James")));
    }

    @Test
    void shouldReturnVetById() throws Exception {
        when(vetService.findById(1)).thenReturn(testVetDTO);

        mockMvc.perform(get("/vets/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("James")))
                .andExpect(jsonPath("$.lastName", is("Carter")));
    }

    @Test
    void shouldCreateVet() throws Exception {
        VetDTO newVet = VetDTO.builder().firstName("John").lastName("Smith").build();
        VetDTO createdVet = VetDTO.builder().id(2).firstName("John").lastName("Smith").build();

        when(vetService.create(any(VetDTO.class))).thenReturn(createdVet);

        mockMvc.perform(post("/vets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newVet)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.firstName", is("John")));
    }

    @Test
    void shouldUpdateVet() throws Exception {
        VetDTO updateVet = VetDTO.builder().id(1).firstName("UpdatedJohn").lastName("UpdatedSmith").build();

        doNothing().when(vetService).update(any(VetDTO.class));
        when(vetService.findById(1)).thenReturn(updateVet);

        mockMvc.perform(put("/vets/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateVet)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("UpdatedJohn")));
    }

    @Test
    void shouldDeleteVet() throws Exception {
        doNothing().when(vetService).delete(1);

        mockMvc.perform(delete("/vets/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
