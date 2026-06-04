package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.entities.Vet;

import java.util.List;

public interface VetService {

    List<Vet> findAll();

    VetDTO findById(Integer id);

    VetDTO create(VetDTO vetDTO);

    void update(VetDTO vetDTO);

    void delete(Integer id);

}