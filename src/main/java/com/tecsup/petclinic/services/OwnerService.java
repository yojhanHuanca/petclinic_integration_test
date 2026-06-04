package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Owner;

import java.util.List;

public interface OwnerService {

    List<Owner> findAll();

    Owner findById(Long id);

    Owner create(Owner owner);

    Owner update(Long id, Owner owner);

    void delete(Long id);
}
