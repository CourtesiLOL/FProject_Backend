package com.fproject.FProject.repositorie;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fproject.FProject.model.entity.EventEntity;
import com.fproject.FProject.model.entity.UserEntity;
import java.util.Set;

public interface EventRepository extends JpaRepository<EventEntity, Long>{

    Set<EventEntity> findAllByOwner(UserEntity owner);
    EventEntity findByOwnerAndName(UserEntity owner, String name);

}