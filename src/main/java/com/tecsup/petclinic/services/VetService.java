package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetDTO;

import java.util.List;

public interface VetService {

    List<VetDTO> findAll();

    VetDTO findById(Integer id);

    VetDTO create(VetDTO vetDTO);

    void update(VetDTO vetDTO);

    void delete(Integer id);

}
