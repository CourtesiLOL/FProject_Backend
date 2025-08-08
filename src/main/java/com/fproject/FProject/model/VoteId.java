package com.fproject.FProject.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class VoteId {

    private long userId;
    private long optionId;

    public VoteId() {
    }

    public long getOptionId() {
        return optionId;
    }

    public void setOptionId(long optionId) {
        this.optionId = optionId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

}
