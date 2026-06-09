package com.tecsup.petclinic.webs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql({ "/schema.sql", "/data.sql" })
public class VisitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testFindAllVisits() throws Exception {
        mockMvc.perform(get("/visits"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].description", is("rabies shot")));
    }

    @Test
    void testFindVisitById() throws Exception {
        mockMvc.perform(get("/visits/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.petId", is(7)))
                .andExpect(jsonPath("$.vetId", is(2)))
                .andExpect(jsonPath("$.cost", is(45.0)));
    }

    @Test
    void testCreateVisit() throws Exception {
        mockMvc.perform(post("/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"Nueva Visita\", \"petId\":1, \"vetId\":1, \"visitDate\":\"2024-05-20\", \"cost\":80.0}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.description", is("Nueva Visita")))
                .andExpect(jsonPath("$.petId", is(1)))
                .andExpect(jsonPath("$.vetId", is(1)))
                .andExpect(jsonPath("$.cost", is(80.0)));
    }

    @Test
    void testUpdateVisit() throws Exception {
        mockMvc.perform(put("/visits/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"Updated Visit\", \"petId\":7, \"vetId\":3, \"visitDate\":\"2024-06-01\", \"cost\":95.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("Updated Visit")))
                .andExpect(jsonPath("$.vetId", is(3)))
                .andExpect(jsonPath("$.cost", is(95.0)));
    }

    @Test
    void testUpdateVisitNotFound() throws Exception {
        mockMvc.perform(put("/visits/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"Missing Visit\", \"petId\":7, \"vetId\":3, \"visitDate\":\"2024-06-01\", \"cost\":95.0}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteVisit() throws Exception {
        String createResponse = mockMvc.perform(post("/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"Delete Visit\", \"petId\":1, \"vetId\":1, \"visitDate\":\"2024-07-10\", \"cost\":50.0}"))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Number visitId = com.jayway.jsonpath.JsonPath.read(createResponse, "$.id");

        mockMvc.perform(delete("/visits/" + visitId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/visits/" + visitId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testTotalCostByPet() throws Exception {
        mockMvc.perform(get("/visits/pet/7/total-cost").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("320.0"));
    }
}
