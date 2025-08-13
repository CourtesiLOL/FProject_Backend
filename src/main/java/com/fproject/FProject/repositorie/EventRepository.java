package com.fproject.FProject.repositorie;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fproject.FProject.model.dto.EventDTO;
import com.fproject.FProject.model.entity.EventEntity;
import com.fproject.FProject.model.entity.UserEntity;

public interface EventRepository extends JpaRepository<EventEntity, Long>{

    Set<EventEntity> findAllByOwner(UserEntity owner);
    EventEntity findByOwnerAndName(UserEntity owner, String name);

}
