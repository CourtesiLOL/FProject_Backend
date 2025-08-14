package com.fproject.FProject.model.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "Election")
public class ElectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "eventId", nullable = false)
    private EventEntity eventId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private int count;

    // ----------------------------------------------

    @OneToMany(mappedBy = "electionId", cascade = CascadeType.ALL)
    private Set<VoteEntity> vote;

    // ----------------------------------------------
    public ElectionEntity() {

    }

    public long getId() {
        return id;
    }

    public LocalDate getDateTime() {
        return date;
    }

    public int getCount() {
        return count;
    }

    public void setEventId(EventEntity eventId) {
        this.eventId = eventId;
    }

    public void setDateTime(LocalDate date) {
        this.date = date;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
