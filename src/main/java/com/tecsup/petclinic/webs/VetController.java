package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.services.VetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Vet entity
 * @author jgomezm
 */
@RestController
@RequestMapping("/vets")
@Slf4j
public class VetController {

    private final VetService vetService;

    public VetController(VetService vetService) {
        this.vetService = vetService;
    }

    /**
     * Get all vets
     * @return list of vets
     */
    @GetMapping
    public ResponseEntity<List<Vet>> findAll() {
        log.info("Getting all vets");
        List<Vet> vets = vetService.findAll();
        return ResponseEntity.ok(vets);
    }

    /**
     * Get vet by id
     * @param id vet id
     * @return VetDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<VetDTO> findById(@PathVariable Integer id) {
        log.info("Getting vet with id: {}", id);
        try {
            VetDTO vetDTO = vetService.findById(id);
            return ResponseEntity.ok(vetDTO);
        } catch (IllegalArgumentException e) {
            log.error("Vet not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Create a new vet
     * @param vetDTO vet data transfer object
     * @return created VetDTO
     */
    @PostMapping
    public ResponseEntity<VetDTO> create(@RequestBody VetDTO vetDTO) {
        log.info("Creating new vet: {}", vetDTO);
        VetDTO createdVet = vetService.create(vetDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVet);
    }

    /**
     * Update an existing vet
     * @param id vet id
     * @param vetDTO vet data transfer object
     * @return updated VetDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<VetDTO> update(@PathVariable Integer id, @RequestBody VetDTO vetDTO) {
        log.info("Updating vet with id: {}", id);
        try {
            vetDTO.setId(id);
            vetService.update(vetDTO);
            VetDTO updatedVet = vetService.findById(id);
            return ResponseEntity.ok(updatedVet);
        } catch (IllegalArgumentException e) {
            log.error("Vet not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete a vet
     * @param id vet id
     * @return response entity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.info("Deleting vet with id: {}", id);
        try {
            vetService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Vet not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
