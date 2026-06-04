package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VisitDTO;
import java.time.LocalDate;
import java.util.List;

public interface VisitService {
    List<VisitDTO> findAll();
    VisitDTO findById(Long id);
    VisitDTO create(VisitDTO visitDTO);
    VisitDTO update(Long id, VisitDTO visitDTO);
    void delete(Long id);
    List<VisitDTO> findByPetId(Integer petId);
    List<VisitDTO> findByVetId(Integer vetId);
    Double calculateTotalCostByPet(Integer petId);
    List<VisitDTO> findByDateBetween(LocalDate from, LocalDate to);
    List<VisitDTO> findByVetIdAndDateBetween(Integer vetId, LocalDate from, LocalDate to);
    Long countByPetId(Integer petId);
}