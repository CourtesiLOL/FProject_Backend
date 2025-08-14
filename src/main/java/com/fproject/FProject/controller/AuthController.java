package com.fproject.FProject.controller;

import com.fproject.FProject.model.dto.requestBody.LoginUser;
import com.fproject.FProject.model.dto.requestBody.RegisterUser;
import com.fproject.FProject.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author javier
 */
@RestController
@CrossOrigin
@RequestMapping("api/auth") 
public class AuthController {

    private final AuthService authService;
    
    public AuthController( AuthService authService) {
        this.authService = authService;
    }
    
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginUser loginUser) {
        return authService.login(loginUser);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterUser regUser) {
        return authService.register(regUser);
    }

}
