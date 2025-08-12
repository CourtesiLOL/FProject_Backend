package com.fproject.FProject.service;

import com.fproject.FProject.config.security.JwtTokenProvider;
import com.fproject.FProject.model.entity.EventEntity;
import com.fproject.FProject.model.entity.ImageEntity;
import com.fproject.FProject.model.entity.UserEntity;
import com.fproject.FProject.repositorie.EventRepository;
import com.fproject.FProject.repositorie.ImageRepository;
import com.fproject.FProject.repositorie.UserRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Value;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;
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
    
    public ImageService(
            ImageRepository imgRepo, 
            JwtTokenProvider jwtProvider, 
            EventRepository eventRepo, 
            UserRepository userRepo) 
    {    
        this.imgRepo = imgRepo;
        this.jwtProvider = jwtProvider;
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
        this.fileName = new StringBuilder();
    }
    
    public ResponseEntity addImage(String eventName, MultipartFile file, String token) {
        
        UserEntity user = userRepo.findByEmail(jwtProvider.getUsername(token));
        
        EventEntity event = eventRepo.findByOwnerAndName(user, eventName);
        if (event == null)
            return ResponseEntity.status(NOT_FOUND)
                    .body("ERROR: You have no event with that name");  
        
        try {
            Path directory = Path.of(uploadDir);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
            
            long count = imgRepo.count();
            
            if (count >= maxImg) 
                return ResponseEntity.status(TOO_MANY_REQUESTS)
                    .body("ERROR: Image limit reached"); 
            count++;
            
            fileName.setLength(0);
            fileName.append(directory.toAbsolutePath());
            fileName.append(File.separator);
            fileName.append("img-");
            fileName.append(event.getName());
            fileName.append("-");
            fileName.append(count);
            
            String imgName = fileName.toString();
            
            System.out.println("New name: "+imgName);
            
            directory = Path.of(imgName);
            Files.write(directory, file.getBytes());
            
            if (!Files.exists(directory)) 
                return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body("ERROR: The server can't save the image"); 
            
            var img = new ImageEntity();
            img.setEventId(event);
            img.setName(directory.toString());
            imgRepo.save(img);
            
            return ResponseEntity.ok("Image uploaded succesfully");   
        } catch(IOException ex) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image");
        }
    }
    
    
}
