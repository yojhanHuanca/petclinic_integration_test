package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.repositories.OwnerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;

    public OwnerServiceImpl(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Override
    public List<Owner> findAll() {
        log.info("Finding all owners");
        return ownerRepository.findAll();
    }

    @Override
    public Owner findById(Long id) {
        log.info("Finding owner with id: {}", id);
        return ownerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found with id: " + id));
    }

    @Override
    public Owner create(Owner owner) {
        log.info("Creating owner: {}", owner);
        return ownerRepository.save(owner);
    }

    @Override
    public Owner update(Long id, Owner owner) {
        log.info("Updating owner with id: {}", id);
        Optional<Owner> existingOwner = ownerRepository.findById(id);
        if (existingOwner.isEmpty()) {
            throw new IllegalArgumentException("Owner not found with id: " + id);
        }
        Owner currentOwner = existingOwner.get();
        currentOwner.setFirstName(owner.getFirstName());
        currentOwner.setLastName(owner.getLastName());
        currentOwner.setAddress(owner.getAddress());
        currentOwner.setCity(owner.getCity());
        currentOwner.setTelephone(owner.getTelephone());
        return ownerRepository.save(currentOwner);
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting owner with id: {}", id);
        if (!ownerRepository.existsById(id)) {
            throw new IllegalArgumentException("Owner not found with id: " + id);
        }
        ownerRepository.deleteById(id);
    }
}
