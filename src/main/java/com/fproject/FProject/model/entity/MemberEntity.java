package com.fproject.FProject.model.entity;

import com.fproject.FProject.model.MemberId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "Member")
public class MemberEntity {

    @EmbeddedId
    private MemberId id;

    @ManyToOne
    @MapsId("eventId")
    @JoinColumn(name = "eventId", nullable = false)
    private EventEntity eventId;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity userId;
}
