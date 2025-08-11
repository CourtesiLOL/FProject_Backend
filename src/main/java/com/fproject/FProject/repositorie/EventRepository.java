package com.fproject.FProject.repositorie;
import org.springframework.data.jpa.repository.JpaRepository;
import com.fproject.FProject.model.entity.EventEntity;

public interface EventRepository extends JpaRepository<EventEntity, Long>{

}