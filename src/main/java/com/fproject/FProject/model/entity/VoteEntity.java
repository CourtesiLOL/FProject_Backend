package com.fproject.FProject.model.entity;

import com.fproject.FProject.model.VoteId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "Vote")
public class VoteEntity {

    @EmbeddedId
    private VoteId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity userId;
    
    @ManyToOne
    @MapsId("electionId")
    @JoinColumn(name = "electionId", nullable = false)
    private ElectionEntity electionId;

}
