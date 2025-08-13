package com.fproject.FProject.model.dto;
import java.time.LocalDateTime;
import java.util.Set;

import com.fproject.FProject.model.entity.UserEntity;

public record EventDTO(String name, String description,Set<LocalDateTime> date) {

}
