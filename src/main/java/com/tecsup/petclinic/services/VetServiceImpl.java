package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.repositories.VetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for Vet entity
 * @author jgomezm
 */
@Service
@Slf4j
public class VetServiceImpl implements VetService {

    private final VetRepository vetRepository;

    public VetServiceImpl(VetRepository vetRepository) {
        this.vetRepository = vetRepository;
    }

    /**
     * Find all vets
     * @return list of vets
     */
    @Override
    public List<Vet> findAll() {
        log.info("Finding all vets");
        return vetRepository.findAll();
    }

    /**
     * Find vet by id
     * @param id vet id
     * @return VetDTO
     */
    @Override
    public VetDTO findById(Integer id) {
        log.info("Finding vet with id: {}", id);
        Optional<Vet> vet = vetRepository.findById(id);
        if (vet.isPresent()) {
            return mapToDto(vet.get());
        }
        throw new IllegalArgumentException("Vet not found with id: " + id);
    }

    /**
     * Create a new vet
     * @param vetDTO vet data transfer object
     * @return VetDTO
     */
    @Override
    public VetDTO create(VetDTO vetDTO) {
        log.info("Creating new vet: {}", vetDTO);
        Vet newVet = new Vet();
        newVet.setFirstName(vetDTO.getFirstName());
        newVet.setLastName(vetDTO.getLastName());
        Vet savedVet = vetRepository.save(newVet);
        return mapToDto(savedVet);
    }

    /**
     * Update an existing vet
     * @param vetDTO vet data transfer object
     */
    @Override
    public void update(VetDTO vetDTO) {
        log.info("Updating vet with id: {}", vetDTO.getId());
        Optional<Vet> vet = vetRepository.findById(vetDTO.getId());
        if (vet.isPresent()) {
            Vet vetToUpdate = vet.get();
            vetToUpdate.setFirstName(vetDTO.getFirstName());
            vetToUpdate.setLastName(vetDTO.getLastName());
            vetRepository.save(vetToUpdate);
        } else {
            throw new IllegalArgumentException("Vet not found with id: " + vetDTO.getId());
        }
    }

    /**
     * Delete a vet by id
     * @param id vet id
     */
    @Override
    public void delete(Integer id) {
        log.info("Deleting vet with id: {}", id);
        if (vetRepository.existsById(id)) {
            vetRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Vet not found with id: " + id);
        }
    }

    /**
     * Map Vet entity to VetDTO
     * @param vet vet entity
     * @return VetDTO
     */
    private VetDTO mapToDto(Vet vet) {
        return VetDTO.builder()
                .id(vet.getId())
                .firstName(vet.getFirstName())
                .lastName(vet.getLastName())
                .build();
    }
}
