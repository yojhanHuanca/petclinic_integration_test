package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.VisitDTO;
import com.tecsup.petclinic.services.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @GetMapping
    public ResponseEntity<List<VisitDTO>> findAll(
            @RequestParam(required = false) Integer petId,
            @RequestParam(required = false) Integer vetId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        
        if (petId != null) return ResponseEntity.ok(visitService.findByPetId(petId));
        if (vetId != null && from != null && to != null) 
            return ResponseEntity.ok(visitService.findByVetIdAndDateBetween(vetId, from, to));
        if (vetId != null) return ResponseEntity.ok(visitService.findByVetId(vetId));
        if (from != null && to != null) return ResponseEntity.ok(visitService.findByDateBetween(from, to));
        
        return ResponseEntity.ok(visitService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitDTO> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(visitService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VisitDTO create(@RequestBody VisitDTO visitDTO) {
        return visitService.create(visitDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VisitDTO> update(@PathVariable Long id, @RequestBody VisitDTO visitDTO) {
        try {
            return ResponseEntity.ok(visitService.update(id, visitDTO));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            visitService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/pet/{petId}/total-cost")
    public ResponseEntity<Double> getTotalCost(@PathVariable Integer petId) {
        return ResponseEntity.ok(visitService.calculateTotalCostByPet(petId));
    }

    @GetMapping("/pet/{petId}/count")
    public ResponseEntity<Long> getCountByPet(@PathVariable Integer petId) {
        return ResponseEntity.ok(visitService.countByPetId(petId));
    }
}
