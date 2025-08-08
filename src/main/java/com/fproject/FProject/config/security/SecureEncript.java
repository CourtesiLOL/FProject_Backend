package com.fproject.FProject.config.security;

import com.fproject.FProject.model.dto.LoginUser;
import com.fproject.FProject.model.dto.RegisterUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 *
 * @author javier
 */
@Component
public class SecureEncript {

    private final PasswordEncoder passwordEncoder;
    
    public SecureEncript(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    
    public String encript(String passw) {
        passw = passwordEncoder.encode(passw);
        return passw;
    }

    public boolean validatePassword(String rawPassword, String encriptedPassword) {
        return passwordEncoder.matches(rawPassword, encriptedPassword);
    }
    
}
