package com.fproject.FProject.repositorie;

import com.fproject.FProject.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author javier
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{
    UserEntity findByEmail(String email);
}
