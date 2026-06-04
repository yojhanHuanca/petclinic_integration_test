package com.tecsup.petclinic.webs;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.tecsup.petclinic.entities.Specialty;
import com.tecsup.petclinic.services.SpecialtyService;

/**
 * REST controller exposing Specialty CRUD endpoints
 * 
 * @author jgomezm
 */
@RestController
@RequestMapping("/specialties")
@AllArgsConstructor
@Slf4j
public class SpecialtyController {

	private final SpecialtyService specialtyService;

	/**
	 * GET all specialties
	 * @return ResponseEntity with list of specialties and 200 OK status
	 */
	@GetMapping
	public ResponseEntity<List<Specialty>> findAll() {
		log.info("GET /specialties - Find all specialties");
		List<Specialty> specialties = specialtyService.findAll();
		return ResponseEntity.ok(specialties);
	}

	/**
	 * GET specialty by ID
	 * @param id the specialty ID
	 * @return ResponseEntity with specialty and 200 OK, or 404 NOT FOUND
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Specialty> findById(@PathVariable Integer id) {
		log.info("GET /specialties/{} - Find specialty by ID", id);
		try {
			Specialty specialty = specialtyService.findById(id);
			return ResponseEntity.ok(specialty);
		} catch (IllegalArgumentException e) {
			log.warn("Specialty not found with id: {}", id);
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * POST create new specialty
	 * @param specialty the specialty to create
	 * @return ResponseEntity with created specialty and 201 CREATED status
	 */
	@PostMapping
	public ResponseEntity<Specialty> create(@RequestBody Specialty specialty) {
		log.info("POST /specialties - Create specialty with name: {}", specialty.getName());
		Specialty createdSpecialty = specialtyService.create(specialty);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdSpecialty);
	}

	/**
	 * PUT update existing specialty
	 * @param id the specialty ID to update
	 * @param specialty the new specialty data
	 * @return ResponseEntity with updated specialty and 200 OK, or 404 NOT FOUND
	 */
	@PutMapping("/{id}")
	public ResponseEntity<Specialty> update(@PathVariable Integer id, @RequestBody Specialty specialty) {
		log.info("PUT /specialties/{} - Update specialty", id);
		try {
			Specialty updatedSpecialty = specialtyService.update(id, specialty);
			return ResponseEntity.ok(updatedSpecialty);
		} catch (IllegalArgumentException e) {
			log.warn("Specialty not found with id: {}", id);
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * DELETE specialty by ID
	 * @param id the specialty ID to delete
	 * @return ResponseEntity with 204 NO CONTENT, or 404 NOT FOUND
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		log.info("DELETE /specialties/{} - Delete specialty", id);
		try {
			specialtyService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (IllegalArgumentException e) {
			log.warn("Specialty not found with id: {}", id);
			return ResponseEntity.notFound().build();
		}
	}
}
