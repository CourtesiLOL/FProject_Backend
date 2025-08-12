package com.fproject.FProject.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 *
 * @author javier
 */
@Entity
@Table(name = "Image")
public class ImageEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generación automática del ID
    private Long id; // Campo ID
    
    @ManyToOne
    @JoinColumn(name = "eventId", nullable = false)
    private EventEntity eventId;
    
    @Column(nullable = false)
    private String name;
    
    public ImageEntity() {
        
    }

    public EventEntity getEventId() {
        return eventId;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return this.id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public void setEventId(EventEntity eventId) {
        this.eventId = eventId;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}