package com.tecsup.petclinic.repositories;

import com.tecsup.petclinic.entities.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findByPetId(Integer petId);
    List<Visit> findByVetId(Integer vetId);
    List<Visit> findByVisitDateBetween(LocalDate start, LocalDate end);
    List<Visit> findByVetIdAndVisitDateBetween(Integer vetId, LocalDate start, LocalDate end);
}