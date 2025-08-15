package com.fproject.FProject.controller;

import com.fproject.FProject.service.ProfileService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author javier
 */

@RestController
@CrossOrigin
@RequestMapping("api/profile")
public class ProfileController {

    private final ProfileService profileService;
    
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }
    
    @GetMapping
    public ResponseEntity getInfo(@RequestHeader("JWT") String token) {
        return profileService.getInfo(token);
    }
    
    @PutMapping
    public ResponseEntity modifyName(@RequestHeader("JWT") String token, @Param("newName") String newName) {
        return profileService.modifyName(token, newName);
    }
    
}
