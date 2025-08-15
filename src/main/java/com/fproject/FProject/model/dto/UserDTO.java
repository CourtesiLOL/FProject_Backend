package com.fproject.FProject.model.dto;

import com.fproject.FProject.model.entity.UserEntity;

public record UserDTO(String name, String email){
    public static UserDTO ofEntity(UserEntity user) {
        return new UserDTO(user.getName(), user.getEmail());
    }
}
