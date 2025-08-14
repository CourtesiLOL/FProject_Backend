package com.fproject.FProject.controller;

import java.util.Set;

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
import com.fproject.FProject.model.dto.HomeEventDTO;
import com.fproject.FProject.service.EventService;

@RestController
@RequestMapping("api/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/ounEvents")
    public ResponseEntity getMyOunEvents(@RequestHeader("JWT") String token) {
        return eventService.getMyOunEvents(token);
    }

    @PostMapping("/create")
    public ResponseEntity createEvent(
        @RequestHeader("JWT") String token, 
        @RequestBody EventDTO event) {
        return eventService.createEvent(token,event);
    }

    @PostMapping("/join/{sharecode}")
    public ResponseEntity joinEvent(
        @PathVariable String sharecode,
        @RequestHeader("JWT") String token) 
        {
        return eventService.joinEvent(token,sharecode);
    }
}
