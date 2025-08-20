package com.fproject.FProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fproject.FProject.model.entity.EventEntity;
import com.fproject.FProject.model.entity.UserEntity;
import java.util.Set;
import java.util.List;


public interface EventRepository extends JpaRepository<EventEntity, Long>{

    Set<EventEntity> findAllByOwner(UserEntity owner);
    EventEntity findByOwnerAndId(UserEntity owner, long id);
    EventEntity findByOwnerAndName(UserEntity owner, String name);
    //EventEntity findBySharecode(String sharecode);
    EventEntity findBySharecode(String sharecode);
    EventEntity findByName(String name);
}
