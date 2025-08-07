package com.fproject.FProject.service;

import com.fproject.FProject.model.dto.LoginUser;
import com.fproject.FProject.model.dto.RegisterUser;
import com.fproject.FProject.model.entity.UserEntity;
import com.fproject.FProject.repositorie.UserRepository;
import org.springframework.http.ResponseEntity;
import static org.springframework.http.HttpStatus.*;
import org.springframework.stereotype.Service;

/**
 *
 * @author javier
 */
@Service
public class AuthService {

    private final UserRepository userRepo;

    public AuthService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public ResponseEntity login(LoginUser loginUser) {
        var user = userRepo.findByEmail(loginUser.email());
        if (user == null) return ResponseEntity.status(UNAUTHORIZED).body("User or Password incorrect");
        
        if (!user.getPassword().equals(loginUser.password())) {
            return ResponseEntity.status(UNAUTHORIZED).body("User or Password incorrect");
        }
        
        //TO-DO Implement generation token
        
        return ResponseEntity.ok("Successfully registered user: future token");
    }

    public ResponseEntity register(RegisterUser regUser) {
        if (userRepo.findByEmail(regUser.email()) != null) {
            return ResponseEntity
                    .status(CONFLICT)
                    .body(null);
        }
        
        var newUser = new UserEntity();
        newUser.setName(regUser.name());
        newUser.setEmail(regUser.email());
        
        //TO-DO Implement password encoder
        newUser.setPassword(regUser.password());
        boolean response = userRepo.save(newUser) != null;
        
        if (response) return ResponseEntity.ok("Successfully registered user");
        else return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("Error: can't register the new user");
    }

}
