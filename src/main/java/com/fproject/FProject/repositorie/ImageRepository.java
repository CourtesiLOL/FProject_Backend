package com.fproject.FProject.repositorie;

import com.fproject.FProject.model.entity.EventEntity;
import com.fproject.FProject.model.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author javier
 */
@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    
    // Método para contar imágenes por el ID del evento
    long countByEventId(EventEntity eventId);
    
}
