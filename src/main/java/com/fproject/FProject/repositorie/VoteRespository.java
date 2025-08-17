package com.fproject.FProject.repositorie;

import com.fproject.FProject.model.VoteId;
import com.fproject.FProject.model.entity.VoteEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface VoteRespository extends JpaRepository<VoteEntity, VoteId> {
        
    @Query("SELECT COUNT(v) FROM VoteEntity v WHERE v.voteId.electionId = :electionId")
    int countVoteInElection(@Param("electionId") Long electionId);
}
