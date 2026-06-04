package com.tecsup.petclinic.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitDTO {

    private Long id;
    private LocalDate visitDate;
    private String description;
    private Integer petId;
    private Integer vetId;
    private Double cost;
}