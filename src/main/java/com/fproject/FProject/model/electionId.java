package com.fproject.FProject.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class electionId {

    private long electionId;
    private long eventId;

    public electionId() {

    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public long getelectionId() {
        return electionId;
    }

    public void setelectionId(long electionId) {
        this.electionId = electionId;
    }

    

}
