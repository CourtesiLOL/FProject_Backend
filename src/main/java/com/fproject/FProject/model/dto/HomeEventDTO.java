package com.fproject.FProject.model.dto;

import java.util.List;
import java.util.Set;

import org.hibernate.event.spi.EventEngine;

import com.fproject.FProject.model.entity.ElectionEntity;
import com.fproject.FProject.model.entity.EventEntity;
import com.fproject.FProject.model.entity.ImageEntity;
import com.fproject.FProject.model.entity.MemberEntity;

public record HomeEventDTO(long id, String name,String description, Set<ImageDTO> images,
Set<ElectionEntity> elections, Set<MemberEntity> members){

}
