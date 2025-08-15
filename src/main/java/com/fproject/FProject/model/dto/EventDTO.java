package com.fproject.FProject.model.dto;
import com.fproject.FProject.model.entity.EventEntity;
import java.util.Set;

import java.time.LocalDate;

public record EventDTO(String name, String description,Set<LocalDate> date) {
    
}
