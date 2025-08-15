package com.fproject.FProject.service;

import java.security.SecureRandom;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fproject.FProject.config.security.JwtTokenProvider;
import com.fproject.FProject.model.MemberId;
import com.fproject.FProject.model.dto.EventDTO;
import com.fproject.FProject.model.dto.FullEventDTO;
import com.fproject.FProject.model.dto.HomeEventDTO;
import com.fproject.FProject.model.dto.ImageDTO;
import com.fproject.FProject.model.entity.ElectionEntity;
import com.fproject.FProject.model.entity.EventEntity;
import com.fproject.FProject.model.entity.UserEntity;
import com.fproject.FProject.repositorie.ElectionRepository;
import com.fproject.FProject.repositorie.EventRepository;
import com.fproject.FProject.repositorie.MemberRepository;
import com.fproject.FProject.repositorie.UserRepository;
import com.fproject.FProject.model.entity.ImageEntity;
import com.fproject.FProject.model.entity.MemberEntity;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.CONFLICT;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final ElectionRepository electionRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwt;

    @Value("${characters.for.code}")
    private String charactersSC;

    @Value("${shareCode.character.long}")
    private byte maxLongSC;



    public EventService(EventRepository eventRepository, ElectionRepository electionRepository,
            UserRepository userRepository, MemberRepository memberRepository, JwtTokenProvider jwt) {
        this.eventRepository = eventRepository;
        this.electionRepository = electionRepository;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
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
        eventNew.setSharecode(generatorSC());
        eventNew = eventRepository.save(eventNew);

        ElectionEntity electionNew;
        for (LocalDate item : event.date()) {
            if (item == null)
                continue;
            System.out.println("Evento: " + item.toString());
            electionNew = new ElectionEntity();
            electionNew.setEventId(eventNew);
            electionNew.setCount(0);
            electionNew.setDateTime(item);
            electionRepository.save(electionNew);
        }

        return ResponseEntity.ok(null);
    }

    public ResponseEntity getEvent(String token, String eventName) {

        String mail = jwt.getUsername(token);
        UserEntity user = userRepository.findByEmail(mail);

        EventEntity event = eventRepository.findByOwnerAndName(user, eventName);
        if (event == null) return ResponseEntity.status(NOT_FOUND).body(null);
        
        Set<ImageDTO> images = new LinkedHashSet();
        for (ImageEntity e : event.getImages()) {
            images.add(ImageDTO.ofEntity(e));
        }
        
        var response = new FullEventDTO(
                       event.getName(),
                       event.getDescription(),
                       images,
                       event.getElection(),
                       memberRepository.findAllByEventId(event.getId()),
                       event.getSharecode()
        );
        
        return ResponseEntity.ok(response);
    }
    
    public ResponseEntity getMyOunEvents(String token) {

        String mail = jwt.getUsername(token);
        UserEntity user = userRepository.findByEmail(mail);

        Set<EventEntity> events = user.getEvents();
        if (!events.isEmpty()) {
            Set<HomeEventDTO> DTOs = new LinkedHashSet();
            Set<ImageDTO> images = new LinkedHashSet();
            
            for (EventEntity event : events) {
                images.clear();
                for (ImageEntity e : event.getImages()) {
                    images.add(ImageDTO.ofEntity(e));
                }
                HomeEventDTO DTO = new HomeEventDTO(
                        event.getName(),
                        event.getDescription(),
                        images,
                        event.getElection(),
                        memberRepository.findAllByEventId(event.getId())
                        );

                DTOs.add(DTO);
            }

            return ResponseEntity.ok(DTOs);
        }
        return ResponseEntity.status(NOT_FOUND).body(null);
    }

    public ResponseEntity getMyMemberEvents(String token) {
        
        String mail = jwt.getUsername(token);
        UserEntity user = userRepository.findByEmail(mail);
        Set<MemberEntity> members = memberRepository.findAllByUserId(user.getId());

        if (members == null || members.isEmpty()) return ResponseEntity.ok(null);
        
        Set<EventEntity> events = new LinkedHashSet();
        Set<HomeEventDTO> DTOs = new LinkedHashSet();
        Set<ImageDTO> images = new LinkedHashSet();
        
        for (MemberEntity member : members) {
            EventEntity event = eventRepository.findById(member.getMemberId().getEventId()).get();
            images.clear();
                for (ImageEntity e : event.getImages()) {
                    images.add(ImageDTO.ofEntity(e));
                }
            DTOs.add(new HomeEventDTO(
                event.getName(),
                event.getDescription(),
                images,
                event.getElection(),
                memberRepository.findAllByEventId(event.getId())
            ));

        }
        return ResponseEntity.ok(DTOs);
    }

    public ResponseEntity joinEvent(String token, String sharecode) {
        UserEntity user = userRepository.findByEmail(jwt.getUsername(token));
        EventEntity event = eventRepository.findBySharecode(sharecode);

        if (event == null) {
            return ResponseEntity.status(NOT_FOUND).body("ERROR: not event found");
        }

        if (user.getId() == event.getOwner()) {
            return ResponseEntity.status(NOT_FOUND).body("ERROR: You are the owner");
        }

        MemberId memId = new MemberId(event.getId(), user.getId());
        if (memberRepository.findByMemberId(memId) != null) {
            return ResponseEntity.status(CONFLICT).body("ERROR: this user is already member");
        }

        MemberEntity member = new MemberEntity();
        member.setMemberId(memId);
        memberRepository.save(member);
        System.out.println("Joiner to the event");
        
        return ResponseEntity.ok(null);
    }

    
    
    private final String generatorSC() {

        SecureRandom random = new SecureRandom();
        StringBuilder codigo = new StringBuilder(maxLongSC);
        for (int i = 0; i < maxLongSC; i++) {
            int indice = random.nextInt(charactersSC.length());
            codigo.append(charactersSC.charAt(indice));
        }
        return codigo.toString();
    }
}
