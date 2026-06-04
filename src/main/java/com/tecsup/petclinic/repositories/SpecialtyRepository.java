package com.tecsup.petclinic.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tecsup.petclinic.entities.Specialty;

/**
 * Repository for Specialty entity providing CRUD operations
 * 
 * @author jgomezm
 */
@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, Integer> {
}
