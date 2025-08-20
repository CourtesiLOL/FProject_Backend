package com.fproject.FProject.service;

import static com.fproject.FProject.Utils.generatorSC;
import com.fproject.FProject.config.security.JwtTokenProvider;
import com.fproject.FProject.model.MemberId;
import com.fproject.FProject.model.entity.EventEntity;
import com.fproject.FProject.model.entity.ImageEntity;
import com.fproject.FProject.model.entity.MemberEntity;
import com.fproject.FProject.model.entity.UserEntity;
import com.fproject.FProject.repository.EventRepository;
import com.fproject.FProject.repository.ImageRepository;
import com.fproject.FProject.repository.MemberRepository;
import com.fproject.FProject.repository.UserRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Value;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;
import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author javier
 */
@Service
public class ImageService {

    @Value("${updload-dir}")
    private String uploadDir;
    @Value("${max-img-event}")
    private byte maxImg;
    
    private final ImageRepository imgRepo;
    private final StringBuilder fileName;
    private final JwtTokenProvider jwtProvider;
    private final EventRepository eventRepo;
    private final UserRepository userRepo;
    private final MemberRepository memberRepo;
    
    public ImageService(
            ImageRepository imgRepo,
            JwtTokenProvider jwtProvider,
            EventRepository eventRepo,
            UserRepository userRepo,
            MemberRepository memberRepo)
    {    
        this.imgRepo = imgRepo;
        this.jwtProvider = jwtProvider;
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
        this.memberRepo = memberRepo;
        this.fileName = new StringBuilder();
    }
    
    public ResponseEntity addImage(long eventId, MultipartFile file, String token) {
        
        if (!file.getContentType().equals("image/webp")) 
            return ResponseEntity.status(UNSUPPORTED_MEDIA_TYPE).body("ERROR: This format is not suported");
        
        UserEntity user = userRepo.findByEmail(jwtProvider.getUsername(token));
        EventEntity event = eventRepo.findByOwnerAndId(user, eventId);
        if (event == null)
            return ResponseEntity.status(NOT_FOUND)
                    .body("ERROR: You have no event with that name");  
        
        try {
            Path directory = Path.of(uploadDir);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
            
            long count = imgRepo.countByEventId(event);
            
            if (count >= maxImg) 
                return ResponseEntity.status(TOO_MANY_REQUESTS)
                    .body("ERROR: Image limit reached"); 
            count++;
            
            fileName.setLength(0);
            fileName.append("img-");
            fileName.append(event.getName());
            fileName.append("-");
            fileName.append(count);
            fileName.append(generatorSC(8));
            fileName.append(".webp");
            
            String imgName = fileName.toString();
            
            System.out.println("New name: "+imgName);
            System.out.println("path: "+directory);
            System.out.println(directory + imgName);
            
            directory = Path.of(directory + File.separator + imgName);
            Files.write(directory, file.getBytes());
            
            if (!Files.exists(directory)) 
                return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body("ERROR: The server can't save the image"); 
            
            var img = new ImageEntity();
            
            img.setEventId(event);
            img.setName(imgName);
            imgRepo.save(img);

            return ResponseEntity.ok("Image uploaded succesfully");   
        } catch(IOException ex) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image");
        }
    }
    
    public ResponseEntity getImage(String imageName, String token) {
           
        ImageEntity img = imgRepo.findByName(imageName);
        if (img == null) return ResponseEntity.status(NOT_FOUND).body(null);
        
        EventEntity imgEvent = img.getEventId();
        UserEntity user = userRepo.findByEmail(jwtProvider.getUsername(token));
        
        MemberEntity mem = memberRepo.findByMemberId(new MemberId(
                imgEvent.getId(),
                user.getId()
        ));
            
        
        if (mem != null || imgEvent.getOwner() == user.getId()) {
            try {
                
                Path imgPath = Path.of(uploadDir + File.separator +img.getName());
                if (!Files.exists(imgPath)) {
                    System.out.println("Error: this file not exist");
                    return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(null);
                }
                    
                return ResponseEntity.ok(Files.readAllBytes(imgPath));
                
            } catch (IOException ex) {
                System.out.println("Error: IOException");
                return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(null);
            }
        }
        
        return ResponseEntity.status(NOT_FOUND).body(null);
    }
    
}
