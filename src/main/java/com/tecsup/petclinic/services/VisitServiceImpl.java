package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VisitDTO;
import com.tecsup.petclinic.entities.Visit;
import com.tecsup.petclinic.entities.Pet;
import com.tecsup.petclinic.repositories.VisitRepository;
import com.tecsup.petclinic.repositories.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;
    private final PetRepository petRepository;

    @Override
    @Transactional(readOnly = true)
    public List<VisitDTO> findAll() {
        return visitRepository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public VisitDTO findById(Long id) {
        return visitRepository.findById(id).map(this::mapToDto)
                .orElseThrow(() -> new IllegalArgumentException("Visit not found"));
    }

    @Override
    public VisitDTO create(VisitDTO visitDTO) {
        Visit visit = new Visit();
        visit.setDescription(visitDTO.getDescription());
        visit.setVisitDate(visitDTO.getVisitDate());
        visit.setVetId(visitDTO.getVetId());
        visit.setCost(visitDTO.getCost());
        Pet pet = petRepository.findById(visitDTO.getPetId())
                .orElseThrow(() -> new IllegalArgumentException("Pet not found"));
        visit.setPet(pet);
        return mapToDto(visitRepository.save(visit));
    }

    @Override
    public VisitDTO update(Long id, VisitDTO visitDTO) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Visit not found"));
        visit.setDescription(visitDTO.getDescription());
        visit.setVisitDate(visitDTO.getVisitDate());
        visit.setVetId(visitDTO.getVetId());
        visit.setCost(visitDTO.getCost());
        return mapToDto(visitRepository.save(visit));
    }

    @Override
    public void delete(Long id) {
        visitRepository.deleteById(id);
    }

    @Override
    public List<VisitDTO> findByPetId(Integer petId) {
        return visitRepository.findByPetId(petId).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<VisitDTO> findByVetId(Integer vetId) {
        return visitRepository.findByVetId(vetId).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public Double calculateTotalCostByPet(Integer petId) {
        return visitRepository.findByPetId(petId).stream()
                .map(Visit::getCost)
                .filter(cost -> cost != null)
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    @Override
    public List<VisitDTO> findByDateBetween(LocalDate from, LocalDate to) {
        return visitRepository.findByVisitDateBetween(from, to).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<VisitDTO> findByVetIdAndDateBetween(Integer vetId, LocalDate from, LocalDate to) {
        return visitRepository.findByVetIdAndVisitDateBetween(vetId, from, to).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public Long countByPetId(Integer petId) { return (long) visitRepository.findByPetId(petId).size(); }

    private VisitDTO mapToDto(Visit visit) {
        return VisitDTO.builder().id(visit.getId()).description(visit.getDescription())
                .visitDate(visit.getVisitDate()).petId(visit.getPet().getId())
                .vetId(visit.getVetId()).cost(visit.getCost()).build();
    }
}
