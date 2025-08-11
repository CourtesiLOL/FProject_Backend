package com.fproject.FProject.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class VoteId {

    private long userId;
    private long electionId;

    public VoteId() {
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
