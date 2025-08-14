package com.fproject.FProject.service;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fproject.FProject.config.security.JwtTokenProvider;
import com.fproject.FProject.model.dto.EventDTO;
import com.fproject.FProject.model.dto.HomeEventDTO;
import com.fproject.FProject.model.dto.ImageDTO;
import com.fproject.FProject.model.entity.ElectionEntity;
import com.fproject.FProject.model.entity.EventEntity;
import com.fproject.FProject.model.entity.UserEntity;
import com.fproject.FProject.repositorie.ElectionRepository;
import com.fproject.FProject.repositorie.EventRepository;
import com.fproject.FProject.repositorie.UserRepository;
import com.fproject.FProject.model.entity.ImageEntity;
import java.time.LocalDate;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.CONFLICT;

@Service
public class EventService {

    private EventRepository eventRepository;
    private ElectionRepository electionRepository;
    private UserRepository userRepository;
    private JwtTokenProvider jwt;

    public EventService(EventRepository eventRepository, ElectionRepository electionRepository,
            UserRepository userRepository, JwtTokenProvider jwt) {
        this.eventRepository = eventRepository;
        this.electionRepository = electionRepository;
        this.userRepository = userRepository;
        this.jwt = jwt;
    }

    public ResponseEntity createEvent(String token, EventDTO event) {

        String mail = jwt.getUsername(token);
        UserEntity user = userRepository.findByEmail(mail);

        if (eventRepository.findByOwnerAndName(user, event.name()) != null)
            return ResponseEntity.status(CONFLICT).body(null);

        EventEntity eventNew = new EventEntity();
        

        eventNew.setOwner(user);
        eventNew.setName(event.name());
        eventNew.setDescription(event.description());
        eventNew.setShareCode("qwertyuiop");
        eventNew = eventRepository.save(eventNew);

        
        ElectionEntity electionNew;
        for (LocalDate item : event.date()) {
            if (item == null)
                continue;
            System.out.println("Evento: "+item.toString());
            electionNew = new ElectionEntity();
            electionNew.setEventId(eventNew);
            electionNew.setCount(0);
            electionNew.setDateTime(item);
            electionRepository.save(electionNew);
        }

        return ResponseEntity.ok(null);
    }

    public ResponseEntity getMyEvents(String token) {

        String mail = jwt.getUsername(token);
        UserEntity user = userRepository.findByEmail(mail);
        
        //Set<EventEntity> events = eventRepository.findAllByOwner(user);
        Set<EventEntity> events = user.getEvents();
        System.out.println(events.size());
        events.size();
        if (!events.isEmpty()) {
            Set<HomeEventDTO> DTOs = new LinkedHashSet();
            
            for (EventEntity event : events) {

                Set<ImageDTO> images = new LinkedHashSet();
                for (ImageEntity e : event.getImages()) {
                    images.add(ImageDTO.ofEntity(e));
                }
                
                
                // DTO.images(imageRepository.findAllBy);
                HomeEventDTO DTO = new HomeEventDTO(
                        event.getName(),
                        event.getDescription(),
                        images,
                        event.getElection(),
                        event.getMembers());

                DTOs.add(DTO);
            }
            

            return ResponseEntity.ok(DTOs);
        }
        return ResponseEntity.status(NOT_FOUND).body(null);
    }
}
