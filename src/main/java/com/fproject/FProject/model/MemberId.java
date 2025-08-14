package com.fproject.FProject.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class MemberId implements Serializable{

    private long eventId;
    private long userId;

    public MemberId() {
    }

    public MemberId(long eventId, long userId) {
        this.eventId = eventId;
        this.userId = userId;
    }
    
    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }


}
