package com.fproject.FProject.service;

import com.fproject.FProject.config.security.JwtTokenProvider;
import com.fproject.FProject.model.dto.UserDTO;
import com.fproject.FProject.model.entity.UserEntity;
import com.fproject.FProject.repository.UserRepository;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 * @author javier
 */
@Service
public class ProfileService {

    private final UserRepository userRepo;
    private final JwtTokenProvider jwt;

    public ProfileService(UserRepository userRepo, JwtTokenProvider jwt) {
        this.userRepo = userRepo;
        this.jwt = jwt;
    }
    
    
    public ResponseEntity getInfo(String token) {
        
        
        System.out.println("Username: "+jwt.getUsername(token));
        UserEntity user = userRepo.findByEmail(
                jwt.getUsername(token)
        );
        
        return ResponseEntity.ok(UserDTO.ofEntity(user));
        
    }
    
    public ResponseEntity modifyName(String token, String name) {
        
        
        if (name == null) return 
             ResponseEntity.status(BAD_REQUEST).body("ERROR: newName is missing");

        UserEntity user = userRepo.findByEmail(
                 jwt.getUsername(token)
        );

        if (user.getName().equals(name)) return 
            ResponseEntity.status(CONFLICT).body("ERROR: This is your current name");
        
        user.setName(name);
        userRepo.save(user);
        
        return ResponseEntity.ok("Your name is upadted");
    }
    
}
