package com.fproject.FProject.model.entity;

import com.fproject.FProject.model.VotingId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 *
 * @author javier
 */
@Entity
@Table(name = "Voting")
public class VotingEntity {
    
    @EmbeddedId
    private VotingId id;
    
    @ManyToOne
    @MapsId("eventId")
    @JoinColumn(name = "eventId", nullable = false)
    private EventEntity eventId;
    
    @ManyToOne
    @MapsId("owner")
    @JoinColumn(name = "owner", nullable = false)
    private UserEntity owner;
    
    @Column(nullable = false)
    private LocalDateTime dateTime;
    
    @Column(nullable = false)
    private int count;
    
    public VotingEntity() {
        
    }

    public VotingId getId() {
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

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setCount(int count) {
        this.count = count;
    }
    
    
    
}
