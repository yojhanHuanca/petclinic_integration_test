package com.tecsup.petclinic.services;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.tecsup.petclinic.entities.Specialty;
import com.tecsup.petclinic.repositories.SpecialtyRepository;

/**
 * Implementation of SpecialtyService providing CRUD operations with transaction handling
 * 
 * @author jgomezm
 */
@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class SpecialtyServiceImpl implements SpecialtyService {

	private final SpecialtyRepository specialtyRepository;

	@Override
	@Transactional(readOnly = true)
	public List<Specialty> findAll() {
		log.info("Finding all specialties");
		return specialtyRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}

	@Override
	@Transactional(readOnly = true)
	public Specialty findById(Integer id) {
		log.info("Finding specialty with id: {}", id);
		return specialtyRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Specialty not found with id: " + id));
	}

	@Override
	public Specialty create(Specialty specialty) {
		log.info("Creating specialty with name: {}", specialty.getName());
		return specialtyRepository.save(specialty);
	}

	@Override
	public Specialty update(Integer id, Specialty specialty) {
		log.info("Updating specialty with id: {}", id);
		Specialty existingSpecialty = findById(id);
		existingSpecialty.setName(specialty.getName());
		return specialtyRepository.save(existingSpecialty);
	}

	@Override
	public void delete(Integer id) {
		log.info("Deleting specialty with id: {}", id);
		if (!specialtyRepository.existsById(id)) {
			throw new IllegalArgumentException("Specialty not found with id: " + id);
		}
		specialtyRepository.deleteById(id);
	}
}
