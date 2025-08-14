package com.fproject.FProject.model.entity;

import java.io.Serializable;

import com.fproject.FProject.model.MemberId;

import jakarta.persistence.CascadeType;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Member")
public class MemberEntity{

    @EmbeddedId
    private MemberId memberId;

    /*
     * 
     @ManyToOne
     @MapsId("eventId")
     @JoinColumn(name = "eventId", nullable = false)
    private EventEntity eventId;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity userId;
     */

    public void setMemberId(MemberId memberId) {
        this.memberId = memberId;
    }

    public MemberId getMemberId() {
        return memberId;
    }
/*
    public void setEventId(EventEntity eventId) {
        this.eventId = eventId;
    }

    public void setUserId(UserEntity userId) {
        this.userId = userId;
    }


	public EventEntity getEventId() {
		return eventId;
	}

	public UserEntity getUserId() {
		return userId;
	} */
}
