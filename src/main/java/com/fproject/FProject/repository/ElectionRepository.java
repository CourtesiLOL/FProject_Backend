package com.fproject.FProject.repository;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fproject.FProject.model.entity.ElectionEntity;
import com.fproject.FProject.model.entity.EventEntity;

public interface ElectionRepository extends JpaRepository<ElectionEntity, Long>{

   // Set<ElectionEntity> findAllByEvent(EventEntity eventId);
}