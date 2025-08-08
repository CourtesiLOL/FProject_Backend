package com.fproject.FProject.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class OptionId {

    private long eventId;

    public OptionId() {

    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

}
