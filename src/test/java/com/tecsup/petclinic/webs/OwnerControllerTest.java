package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.entities.Owner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Sql({"/schema.sql", "/data.sql"})
public class OwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldFindAllOwners() throws Exception {
        mockMvc.perform(get("/owners").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("George")))
                .andExpect(jsonPath("$[0].lastName", is("Franklin")));
    }

    @Test
    void shouldFindOwnerById() throws Exception {
        mockMvc.perform(get("/owners/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("George")))
                .andExpect(jsonPath("$.lastName", is("Franklin")))
                .andExpect(jsonPath("$.city", is("Madison")));
    }

    @Test
    void shouldCreateOwner() throws Exception {
        Owner newOwner = new Owner();
        newOwner.setFirstName("Ana");
        newOwner.setLastName("Gomez");
        newOwner.setAddress("123 Test Ave");
        newOwner.setCity("Lima");
        newOwner.setTelephone("999888777");

        mockMvc.perform(post("/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newOwner)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.firstName", is("Ana")))
                .andExpect(jsonPath("$.lastName", is("Gomez")));
    }

    @Test
    void shouldUpdateOwner() throws Exception {
        Owner updateOwner = new Owner();
        updateOwner.setFirstName("Updated");
        updateOwner.setLastName("Owner");
        updateOwner.setAddress("Updated Address");
        updateOwner.setCity("Updated City");
        updateOwner.setTelephone("555123456");

        mockMvc.perform(put("/owners/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateOwner)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Updated")))
                .andExpect(jsonPath("$.telephone", is("555123456")));
    }

    @Test
    void shouldDeleteOwner() throws Exception {
        Owner newOwner = new Owner();
        newOwner.setFirstName("Delete");
        newOwner.setLastName("Me");
        newOwner.setAddress("Delete St");
        newOwner.setCity("TestCity");
        newOwner.setTelephone("555000111");

        ResultActions result = mockMvc.perform(post("/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newOwner)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber());

        String content = result.andReturn().getResponse().getContentAsString();
        Long id = objectMapper.readTree(content).get("id").asLong();

        mockMvc.perform(delete("/owners/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/owners/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
