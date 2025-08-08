package com.fproject.FProject.model.entity;

import com.fproject.FProject.model.OptionId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "Option")
public class OptionEntity {
     
    @EmbeddedId
    private OptionId id;
    
    @ManyToOne
    @MapsId("eventId")
    @JoinColumn(name = "eventId", nullable = false)
    private EventEntity eventId;
    
    @Column(nullable = false)
    private LocalDateTime dateTime;
    
    @Column(nullable = false)
    private int count;
    
    public OptionEntity() {
        
    }

    public OptionId getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public int getCount() {
        return count;
    }

    public void setEventId(EventEntity eventId) {
        this.eventId = eventId;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setCount(int count) {
        this.count = count;
    }
    
    
    
}
