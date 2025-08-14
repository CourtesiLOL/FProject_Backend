package com.fproject.FProject.repositorie;

import com.fproject.FProject.model.MemberId;
import com.fproject.FProject.model.entity.MemberEntity;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author javier
 */
@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, MemberId> {
    MemberEntity findByMemberId(MemberId memberId);
    Set<MemberEntity> findAllByUserId(Long userId);
    Set<MemberEntity> findAllByEventId(Long eventId);
}
