package com.fproject.FProject.repositorie;

import com.fproject.FProject.model.MemberId;
import com.fproject.FProject.model.entity.MemberEntity;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author javier
 */
@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, MemberId> {
    MemberEntity findByMemberId(MemberId memberId);
    
    @Query("SELECT m FROM MemberEntity m WHERE m.memberId.userId = :userId")
    Set<MemberEntity> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT m FROM MemberEntity m WHERE m.memberId.eventId = :eventId")
    Set<MemberEntity> findAllByEventId(@Param("eventId") Long eventId);

}
