package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.services.OwnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/owners")
@Slf4j
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping
    public ResponseEntity<List<Owner>> findAll() {
        log.info("Getting all owners");
        return ResponseEntity.ok(ownerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Owner> findById(@PathVariable Long id) {
        log.info("Getting owner with id: {}", id);
        try {
            return ResponseEntity.ok(ownerService.findById(id));
        } catch (IllegalArgumentException e) {
            log.error("Owner not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Owner> create(@RequestBody Owner owner) {
        log.info("Creating owner: {}", owner);
        Owner createdOwner = ownerService.create(owner);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOwner);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Owner> update(@PathVariable Long id, @RequestBody Owner owner) {
        log.info("Updating owner with id: {}", id);
        try {
            return ResponseEntity.ok(ownerService.update(id, owner));
        } catch (IllegalArgumentException e) {
            log.error("Owner not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deleting owner with id: {}", id);
        try {
            ownerService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Owner not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
