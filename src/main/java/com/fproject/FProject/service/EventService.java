package com.fproject.FProject.service;

import java.security.SecureRandom;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fproject.FProject.config.security.JwtTokenProvider;
import com.fproject.FProject.model.MemberId;
import com.fproject.FProject.model.VoteId;
import com.fproject.FProject.model.dto.EventDTO;
import com.fproject.FProject.model.dto.FullEventDTO;
import com.fproject.FProject.model.dto.HomeEventDTO;
import com.fproject.FProject.model.dto.ImageDTO;
import com.fproject.FProject.model.entity.ElectionEntity;
import com.fproject.FProject.model.entity.EventEntity;
import com.fproject.FProject.model.entity.UserEntity;
import com.fproject.FProject.model.entity.VoteEntity;
import com.fproject.FProject.repositorie.ElectionRepository;
import com.fproject.FProject.repositorie.EventRepository;
import com.fproject.FProject.repositorie.MemberRepository;
import com.fproject.FProject.repositorie.UserRepository;
import com.fproject.FProject.repositorie.VoteRespository;
import com.fproject.FProject.model.entity.ImageEntity;
import com.fproject.FProject.model.entity.MemberEntity;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final ElectionRepository electionRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final VoteRespository voteR;
    private final JwtTokenProvider jwt;

    @Value("${characters.for.code}")
    private String charactersSC;

    @Value("${shareCode.character.long}")
    private byte maxLongSC;



    public EventService(EventRepository eventRepository, ElectionRepository electionRepository,
            UserRepository userRepository, MemberRepository memberRepository,VoteRespository voteR, JwtTokenProvider jwt) {
        this.eventRepository = eventRepository;
        this.electionRepository = electionRepository;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.voteR = voteR;
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

    public ResponseEntity getEvent(String token, long eventId) {
        String mail = jwt.getUsername(token);
        UserEntity user = userRepository.findByEmail(mail);
        Optional<EventEntity> eventOpt = eventRepository.findById(eventId);
        
        if (eventOpt.isEmpty()) 
            return ResponseEntity.status(NOT_FOUND).body("This event not exist");
        
        var event = eventOpt.get();
        
        if (event.getOwner() == user.getId())
            return ResponseEntity.ok(makeFullEventDto(event, true));

        MemberEntity member = memberRepository.findByMemberId(new MemberId(event.getId(), user.getId()));
        if (member != null) 
            return ResponseEntity.ok(makeFullEventDto(event, false));
        
        return ResponseEntity.status(NOT_FOUND).body("This event no exist");
        
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
                        event.getId(),
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
                event.getId(),
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

    public ResponseEntity voteElectionId(String token,long eId){
        
        UserEntity user = userRepository.findByEmail(jwt.getUsername(token));
        ElectionEntity election = electionRepository.findById(eId).get();
        
        if (electionRepository.findById(eId).isEmpty()) {
            return ResponseEntity.status(NOT_FOUND).body("ERROR: The election not exist");
        }

        var eventOp = eventRepository.findById(election.getEventId());
        if (eventOp.isEmpty()) {
            return ResponseEntity.status(NOT_FOUND).body("ERROR: This event not exist");
        }
        EventEntity event = eventOp.get();
        if (event.getOwner() == user.getId())
            return vote(election, user);

        MemberEntity member = memberRepository.findByMemberId(new MemberId(event.getId(), user.getId()));
        if (member != null) 
            return vote(election, user);
        
        return ResponseEntity.status(UNAUTHORIZED).body("ERROR: Your not a member");
    }

    public ResponseEntity voteElection(String token,ElectionEntity election){
        UserEntity user = userRepository.findByEmail(jwt.getUsername(token));

        VoteId veId = new VoteId(user.getId(),election.getId());
        VoteEntity ve = new VoteEntity();
        ve.setVoteId(veId);

        // Ya a votado para esa elecion
        if (voteR.findById(veId).get().getVoteId() == ve.getVoteId()) {
            return ResponseEntity.status(NOT_IMPLEMENTED).body("ERROR: You already voted");
        }

        voteR.save(ve);
        election.setCount(voteR.countVoteInElection(election.getId()));
        electionRepository.save(election);

        return ResponseEntity.ok(null);
    }
    
    private FullEventDTO makeFullEventDto(EventEntity event, boolean owner) {
        Set<ImageDTO> images = new LinkedHashSet();
        for (ImageEntity e : event.getImages()) {
            images.add(ImageDTO.ofEntity(e));
        }

        return new FullEventDTO(
           event.getId(),
           event.getName(),
           event.getDescription(),
           images,
           event.getElection(),
           memberRepository.findAllByEventId(event.getId()),
           owner ? event.getSharecode() : null
        );

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
    
    private final ResponseEntity vote(ElectionEntity election, UserEntity user) {
        
        VoteId veId = new VoteId(user.getId(),election.getId());

        // Ya a votado para esa elecion
        Optional<VoteEntity> ve = voteR.findById(veId);
        
        if (ve.isPresent()) {
            voteR.deleteById(veId);
            //return ResponseEntity.status(CONFLICT).body("ERROR: You already voted");
        } else {  
            var vote = new VoteEntity();
            vote.setVoteId(veId);
            
            voteR.save(vote);
        }

        
        election.setCount(voteR.countVoteInElection(election.getId()));
        electionRepository.save(election);
        
        return ResponseEntity.ok("Voted");
    }
} 

