package com.fproject.FProject.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fproject.FProject.config.security.JwtTokenProvider;
import com.fproject.FProject.model.MemberId;
import com.fproject.FProject.model.dto.EventDTO;
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

import org.springframework.beans.factory.annotation.Value;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.CONFLICT;

@Service
public class EventService {

    private EventRepository eventRepository;
    private ElectionRepository electionRepository;
    private UserRepository userRepository;
    private MemberRepository memberRepository;
    private JwtTokenProvider jwt;

    @Value("${characters.for.code}")
    private String charactersSC;

    @Value("${shareCode.character.long}")
    private byte maxLongSC;

    private String generatorSC() {

        SecureRandom random = new SecureRandom();
        StringBuilder codigo = new StringBuilder(maxLongSC);
        for (int i = 0; i < maxLongSC; i++) {
            int indice = random.nextInt(charactersSC.length());
            codigo.append(charactersSC.charAt(indice));
        }
        return codigo.toString();
    }

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
        for (LocalDateTime item : event.date()) {
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

    public ResponseEntity getMyOunEvents(String token) {

        String mail = jwt.getUsername(token);
        UserEntity user = userRepository.findByEmail(mail);

        // Set<EventEntity> events = eventRepository.findAllByOwner(user);
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
                HomeEventDTO DTO = new HomeEventDTO(
                        event.getName(),
                        event.getDescription(),
                        images,
                        event.getElection(),
                        null
                        );

                DTOs.add(DTO);
            }

            return ResponseEntity.ok(DTOs);
        }
        return ResponseEntity.status(NOT_FOUND).body(null);
    }

    public ResponseEntity getMyEvents(String token) {
        String mail = jwt.getUsername(token);
        UserEntity user = userRepository.findByEmail(mail);

        // Set<EventEntity> events = eventRepository.findAllByOwner(user);
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
                HomeEventDTO DTO = new HomeEventDTO(
                        event.getName(),
                        event.getDescription(),
                        images,
                        event.getElection(),
                        null
                        );

                DTOs.add(DTO);
            }

            return ResponseEntity.ok(DTOs);
        }
        return ResponseEntity.status(NOT_FOUND).body(null);
    }

    public ResponseEntity joinEvent(String token, String sharecode) {
        UserEntity user = userRepository.findByEmail(jwt.getUsername(token));
        EventEntity event = eventRepository.findBySharecode(sharecode);

        if (event == null) {
            return ResponseEntity.status(NOT_FOUND).body("ERROR: not event found");
        }
        MemberId memId = new MemberId(event.getId(), user.getId());
        if (memberRepository.findByMemberId(memId) != null) {
            return ResponseEntity.status(CONFLICT).body("ERROR: this user is already member");
        }

        MemberEntity member = new MemberEntity();
        member.setMemberId(memId);
        memberRepository.save(member);
        
        return ResponseEntity.ok(null);
    }

}
