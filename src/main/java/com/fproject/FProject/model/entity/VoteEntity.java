package com.fproject.FProject.model.entity;

import com.fproject.FProject.model.VoteId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Vote")
public class VoteEntity {

    @EmbeddedId
    private VoteId voteId;

    public VoteId getVoteId() {
        return voteId;
    }

    public void setVoteId(VoteId voteId) {
        this.voteId = voteId;
    }

}
