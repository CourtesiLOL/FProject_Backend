package com.fproject.FProject.repositorie;

import com.fproject.FProject.model.MemberId;
import com.fproject.FProject.model.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author javier
 */
@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    MemberEntity findById(MemberId id);
}
