package com.fproject.FProject.service;

import com.fproject.FProject.config.security.JwtTokenProvider;
import com.fproject.FProject.config.security.SecureEncript;
import com.fproject.FProject.model.dto.JwtDTO;
import com.fproject.FProject.model.dto.requestBody.LoginUser;
import com.fproject.FProject.model.dto.requestBody.RegisterUser;
import com.fproject.FProject.model.entity.UserEntity;
import com.fproject.FProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import static org.springframework.http.HttpStatus.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;



/**
 *
 * @author javier
 */
@Service
public class AuthService {

    @Value("${min.password.length}")
    private int minPasslen;
    @Value("${max.password.length}")
    private int maxPasslen;
    
    private final UserRepository userRepo;
    private final SecureEncript secureEncript;
    private final JwtTokenProvider jwtProvider;

    public AuthService(UserRepository userRepo, SecureEncript encript, JwtTokenProvider jwtProvider) {
        this.userRepo = userRepo;
        this.secureEncript = encript;
        this.jwtProvider = jwtProvider;
    }

    public ResponseEntity login(LoginUser loginUser) {
        var user = userRepo.findByEmail(loginUser.email());
        if (user == null) return ResponseEntity.status(UNAUTHORIZED).body("User or Password incorrect");
        
        
        if (!secureEncript.validatePassword(loginUser.password(), user.getPassword())) {
            return ResponseEntity.status(UNAUTHORIZED).body("User or Password incorrect");
        }
        
        //TO-DO Implement generation token
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        
        return ResponseEntity.ok(new JwtDTO(
                jwtProvider.generateToken(auth)
        ));
    }

    public ResponseEntity register(RegisterUser regUser) {
        if (userRepo.findByEmail(regUser.email()) != null) {
            return ResponseEntity
                    .status(CONFLICT)
                    .body(null);
        }
        
        if (!validPasswordLength(regUser.password())) 
            return ResponseEntity.status(UNPROCESSABLE_ENTITY).body("Invalid password format");
        
        var newUser = new UserEntity();
        newUser.setName(regUser.name());
        newUser.setEmail(regUser.email());
        newUser.setPassword(secureEncript.encript(regUser.password()));
        boolean response = userRepo.save(newUser) != null;
        
        if (response) return ResponseEntity.ok("Successfully registered user");
        else return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("Error: can't register the new user");
    }

    private boolean validPasswordLength(String password) {
        if (password == null) return false;
        
        int length = password.length();
        String lower = password.toLowerCase();
        
        //Cumple la longitud
        if (length > maxPasslen || length < minPasslen) return false;
        
        // Comprobar patrones comunes de SQL Injection
        if (lower.contains("select") ||
            lower.contains("insert") ||
            lower.contains("update") ||
            lower.contains("delete") ||
            lower.contains("drop") ||
            lower.contains("union") ||
            lower.contains("--") ||
            lower.contains(";") ||
            lower.contains("'") ||
            lower.contains("\"") ||
            lower.contains("/*") ||
            lower.contains("*/")) {
            return false; // Contiene patrones de SQL Injection
        }    
        
        // Verificar que tenga al menos una mayúscula, un número y un carácter especial
        if (!password.matches(".*[A-Z].*")) return false; // al menos una mayúscula
        if (!password.matches(".*[0-9].*")) return false; // al menos un número
        if (!password.matches(".*[^a-zA-Z0-9].*")) return false; // al menos un carácter especial

        
        return true;
    }

}
