package com.fproject.FProject.model.dto;
import java.time.LocalDateTime;
import java.util.Set;

public record EventDTO(String owner,String name, String description,Set<LocalDateTime> date) {}
