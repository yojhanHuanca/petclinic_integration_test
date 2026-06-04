package com.tecsup.petclinic.services;

import java.util.List;
import com.tecsup.petclinic.entities.Specialty;

/**
 * Service interface for Specialty entity defining business logic contract
 * 
 * @author jgomezm
 */
public interface SpecialtyService {

	/**
	 * Retrieve all specialties
	 * @return list of all specialties
	 */
	List<Specialty> findAll();

	/**
	 * Retrieve a specialty by ID
	 * @param id the specialty ID
	 * @return the specialty if found
	 * @throws IllegalArgumentException if specialty not found
	 */
	Specialty findById(Integer id);

	/**
	 * Create a new specialty
	 * @param specialty the specialty to create
	 * @return the created specialty with assigned ID
	 */
	Specialty create(Specialty specialty);

	/**
	 * Update an existing specialty
	 * @param id the specialty ID to update
	 * @param specialty the new specialty data
	 * @return the updated specialty
	 * @throws IllegalArgumentException if specialty not found
	 */
	Specialty update(Integer id, Specialty specialty);

	/**
	 * Delete a specialty by ID
	 * @param id the specialty ID to delete
	 * @throws IllegalArgumentException if specialty not found
	 */
	void delete(Integer id);
}
