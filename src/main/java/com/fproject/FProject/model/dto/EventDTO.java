package com.fproject.FProject.model.dto;
import java.util.Set;

import java.time.LocalDate;

public record EventDTO(String name, String description, Set<LocalDate> date) {

}
