package com.fproject.FProject.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public class VoteId implements Serializable{

    private long userId;
    private long electionId;

    public VoteId() {
    }

    public VoteId(long userId, long electionId) {
        this.userId = userId;
        this.electionId = electionId;
    }

    public long getelectionId() {
        return electionId;
    }

    public void setelectionId(long electionId) {
        this.electionId = electionId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

}
