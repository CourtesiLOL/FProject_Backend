package com.fproject.FProject.model.dto;

import com.fproject.FProject.model.entity.ImageEntity;

/**
 *
 * @author javier
 */
public record ImageDTO (String name){
    
    public static ImageDTO ofEntity(ImageEntity img) {
        if (img == null) return null;
        return new ImageDTO(img.getName());
    }
    
}
