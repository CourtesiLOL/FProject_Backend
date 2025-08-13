package com.fproject.FProject.repositorie;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fproject.FProject.model.MemberId;
import com.fproject.FProject.model.entity.EventEntity;
import com.fproject.FProject.model.entity.MemberEntity;

public interface MemberRepository extends JpaRepository<MemberEntity, MemberId>{
    //Set<MemberEntity> findAllByEvent(EventEntity eventId);
}