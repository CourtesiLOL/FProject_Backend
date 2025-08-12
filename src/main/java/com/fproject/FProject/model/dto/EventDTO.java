package com.fproject.FProject.model.dto;
import java.time.LocalDateTime;
import java.util.Set;

import com.fproject.FProject.model.entity.UserEntity;

public record EventDTO(UserEntity owner,String name, String description,Set<LocalDateTime> date) {

    public Set<LocalDateTime> getDates(){
        return date;
    }
}
