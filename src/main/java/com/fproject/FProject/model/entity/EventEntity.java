package com.fproject.FProject.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "Event")
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "owner", nullable = false)
    private UserEntity owner;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String shareCode;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "eventId", cascade = CascadeType.ALL)
    private Set<ImageEntity> images;

    @OneToMany(mappedBy = "eventId", cascade = CascadeType.ALL)
    private Set<ElectionEntity> election;

    @OneToMany(mappedBy = "eventId", cascade = CascadeType.ALL)
    private Set<MemberEntity> members;

    @OneToMany(mappedBy = "eventId", cascade = CascadeType.ALL)
    private Set<CommentEntity> comments;

    public EventEntity() {

    }

    public long getId() {
        return id;
    }

    public long getOwner() {
        return owner.getId();
    }

    public String getName() {
        return name;
    }

    public String getShareCode() {
        return shareCode;
    }

    public String getDescription() {
        return description;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public Set<ImageEntity> getImages() {
        return images;
    }

    public Set<ElectionEntity> getElection() {
        return election;
    }

    public Set<MemberEntity> getMembers() {
        return members;
    }

    public Set<CommentEntity> getComments() {
        return comments;
    }

}
