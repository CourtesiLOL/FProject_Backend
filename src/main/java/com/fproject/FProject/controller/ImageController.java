package com.fproject.FProject.controller;

import com.fproject.FProject.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author javier
 */
@RestController
@CrossOrigin
@RequestMapping("api/image")
public class ImageController {
    
    private final ImageService imgService;
    
    public ImageController(ImageService imgService) {
        this.imgService = imgService;
    }
    
    @PostMapping("/{eventName}")
    public ResponseEntity addImage(
            @PathVariable String eventName, 
            @RequestPart("imgFile") MultipartFile imgFile, 
            @RequestHeader("JWT") String token
    ) {
        
        System.out.println("Info file");
        System.out.println("---------");
        System.out.println("Content Type: "+imgFile.getContentType());
        System.out.println("Name: "+imgFile.getName());
        System.out.println("Original File name: "+imgFile.getOriginalFilename());
        System.out.println("filename: "+imgFile.getResource().getFilename());;
        for (String x : imgFile.getOriginalFilename().split(".")) {
            System.out.println(x);
        }
        
        return imgService.addImage(eventName, imgFile, token);
        
    }
       
    @GetMapping("/{imageName}")
    public ResponseEntity getImage(
            @PathVariable String imageName,
            @RequestHeader("JWT") String token)
    {
        //Tengo que mirar como mandar bien el archivo de vuelta
        return imgService.getImage(imageName, token);
    }
    
}
