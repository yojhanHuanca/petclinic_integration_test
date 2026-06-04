package com.tecsup.petclinic.webs;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.entities.Specialty;

/**
 * Integration tests for SpecialtyController covering all CRUD operations
 * 
 * @author jgomezm
 */
@AutoConfigureMockMvc
@SpringBootTest
@Sql({ "/schema.sql", "/data.sql" })
public class SpecialtyControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * Test: GET /specialties - Should return all specialties
	 * Expected: 200 OK with list containing first specialty as "radiology"
	 */
	@Test
	void shouldFindAllSpecialties() throws Exception {
		mockMvc.perform(get("/specialties").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].name", is("radiology")));
	}

	/**
	 * Test: GET /specialties/1 - Should return specialty by ID
	 * Expected: 200 OK with complete specialty record
	 */
	@Test
	void shouldFindSpecialtyById() throws Exception {
		mockMvc.perform(get("/specialties/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("radiology")));
	}

	/**
	 * Test: POST /specialties - Should create new specialty
	 * Expected: 201 CREATED with newly created specialty and assigned ID
	 */
	@Test
	void shouldCreateSpecialty() throws Exception {
		Specialty newSpecialty = new Specialty();
		newSpecialty.setName("ophthalmology");

		mockMvc.perform(post("/specialties").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newSpecialty)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.name", is("ophthalmology")));
	}

	/**
	 * Test: PUT /specialties/1 - Should update existing specialty
	 * Expected: 200 OK with updated specialty name
	 */
	@Test
	void shouldUpdateSpecialty() throws Exception {
		Specialty updateSpecialty = new Specialty();
		updateSpecialty.setName("Updated Radiology");

		mockMvc.perform(put("/specialties/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateSpecialty)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("Updated Radiology")));
	}

	/**
	 * Test: DELETE /specialties - Should delete specialty and verify 404 on retrieval
	 * Expected: 204 NO CONTENT on delete, then 404 NOT FOUND on retrieval
	 */
	@Test
	void shouldDeleteSpecialty() throws Exception {
		// Create a new specialty
		Specialty newSpecialty = new Specialty();
		newSpecialty.setName("cardiology");

		String createResponse = mockMvc.perform(post("/specialties").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newSpecialty)))
				.andExpect(status().isCreated())
				.andReturn()
				.getResponse()
				.getContentAsString();

		Specialty createdSpecialty = objectMapper.readValue(createResponse, Specialty.class);
		Integer specialtyId = createdSpecialty.getId();

		// Delete the specialty
		mockMvc.perform(delete("/specialties/" + specialtyId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

		// Verify specialty is deleted
		mockMvc.perform(get("/specialties/" + specialtyId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}
}
