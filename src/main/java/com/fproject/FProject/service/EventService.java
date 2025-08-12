package com.fproject.FProject.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fproject.FProject.config.security.JwtTokenProvider;
import com.fproject.FProject.model.dto.EventDTO;
import com.fproject.FProject.model.dto.HomeEventDTO;
import com.fproject.FProject.model.entity.ElectionEntity;
import com.fproject.FProject.model.entity.EventEntity;
import com.fproject.FProject.model.entity.UserEntity;
import com.fproject.FProject.repositorie.ElectionRepository;
import com.fproject.FProject.repositorie.EventRepository;
import com.fproject.FProject.repositorie.MemberRepository;
import com.fproject.FProject.repositorie.UserRepository;

@Service
public class EventService {

    private EventRepository eventRepository;
    private ElectionRepository electionRepository;
    private UserRepository userRepository;
    private MemberRepository memberRepository;
//    private Imagepository imageRepository;

    private JwtTokenProvider jwt;

    public ResponseEntity createEvent(EventDTO event){

        EventEntity eventNew = new EventEntity();
        ElectionEntity electionNew = new ElectionEntity();
        
        eventNew.setOwner(event.owner());
        eventNew.setName(event.name());
        eventNew.setDescription(event.description());
        eventNew.setShareCode("qwertyuiop");
        eventNew = eventRepository.save(eventNew);

        electionNew.setEventId(eventNew);
        electionNew.setCount(0);
       for (LocalDateTime item : event.date()) {
        electionNew.setDateTime(item);
        electionRepository.save(electionNew);
       }

        return ResponseEntity.ok(null);
    }

    public Set<HomeEventDTO> getMyEvents(String token){

        String mail = jwt.getUsername(token);
        UserEntity user = userRepository.findByEmail(mail);
        
        Set<EventEntity> events = eventRepository.findAllByOwner(user);

        Set<HomeEventDTO> DTOs = null;
        if (!events.isEmpty()) {   
            for (EventEntity event : events) {
                HomeEventDTO DTO = new HomeEventDTO(event.getName(), event.getDescription(), null, null, null);
                
                //DTO.images(imageRepository.findAllBy);
                
                DTO.elections(electionRepository.findAllByEvent(event));

                DTO.members(memberRepository.findAllByEvent(event));

                DTOs.add(DTO);
            }



        }




        return DTOs;
    }
}
