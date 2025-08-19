package com.fproject.FProject.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fproject.FProject.model.dto.EventDTO;
import com.fproject.FProject.model.entity.ElectionEntity;
import com.fproject.FProject.service.EventService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
@CrossOrigin
@RequestMapping("api/event")
public class EventController {

    @Autowired
    private EventService eventService;

    
    @GetMapping("/{eventId}")
    public ResponseEntity getEvent(@RequestHeader("JWT") String token, @PathVariable long eventId) {
        return eventService.getEvent(token, eventId);
    }
    
    @GetMapping("/oun-events")
    public ResponseEntity getMyOunEvents(@RequestHeader("JWT") String token) {
        return eventService.getMyOunEvents(token);
    }

    @GetMapping("/member-events")
    public ResponseEntity getMyMemberEvents(@RequestHeader("JWT") String token) {
        return eventService.getMyMemberEvents(token);
    }

    @PostMapping("/create")
    public ResponseEntity createEvent(
        @RequestHeader("JWT") String token, 
        @RequestBody EventDTO event) {
        return eventService.createEvent(token,event);
    }
    
    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity deleteEvent(
        @RequestHeader("JWT") String token, 
        @PathVariable long eventId
    ) {
        return eventService.delecteEvent(token, eventId);
    }

    @PostMapping("/join/{sharecode}")
    public ResponseEntity joinEvent(
        @PathVariable String sharecode,
        @RequestHeader("JWT") String token) 
        {
        return eventService.joinEvent(token,sharecode);
    }

    @PostMapping("/vote-by-id/{id}")
    public ResponseEntity voteElectionId(
        @RequestHeader("JWT") String token,
        @PathVariable("id") long id
        ) {
        return eventService.voteElectionId(token,id);
    }

    //----------------
    @PostMapping("/vote-by-election")
    public ResponseEntity voteElection(
        @RequestHeader("JWT") String token,
        @RequestBody ElectionEntity election
        ) {
        return eventService.voteElection(token,election);
    }
    
}
