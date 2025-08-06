package com.fproject.FProject.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class VotingId {

    private long eventId;
    private long owner;

    public VotingId() {

    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public long getOwner() {
        return owner;
    }

    public void setOwner(long owner) {
        this.owner = owner;
    }

}
