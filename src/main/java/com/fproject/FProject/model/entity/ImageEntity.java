package com.fproject.FProject.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    
    //if not work change to OneToMany
    @Id
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

    public void setEventId(EventEntity eventId) {
        this.eventId = eventId;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
