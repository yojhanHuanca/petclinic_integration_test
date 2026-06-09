package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.dtos.VetDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Sql({"/schema.sql", "/data.sql"})
@DisplayName("Vet Controller Integration Tests")
public class VetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldListVets() throws Exception {
        mockMvc.perform(get("/vets").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("James")))
                .andExpect(jsonPath("$[0].lastName", is("Carter")));
    }

    @Test
    void shouldReturnVetById() throws Exception {
        mockMvc.perform(get("/vets/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("James")))
                .andExpect(jsonPath("$.lastName", is("Carter")));
    }

    @Test
    void shouldCreateVet() throws Exception {
        VetDTO newVet = VetDTO.builder().firstName("John").lastName("Smith").build();

        mockMvc.perform(post("/vets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newVet)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Smith")));
    }

    @Test
    void shouldUpdateVet() throws Exception {
        VetDTO updateVet = VetDTO.builder().firstName("UpdatedJohn").lastName("UpdatedSmith").build();

        mockMvc.perform(put("/vets/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateVet)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("UpdatedJohn")))
                .andExpect(jsonPath("$.lastName", is("UpdatedSmith")));
    }

    @Test
    void shouldDeleteVet() throws Exception {
        VetDTO newVet = VetDTO.builder().firstName("Delete").lastName("Me").build();

        String createResponse = mockMvc.perform(post("/vets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newVet)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        int vetId = objectMapper.readTree(createResponse).get("id").asInt();

        mockMvc.perform(delete("/vets/" + vetId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/vets/" + vetId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
