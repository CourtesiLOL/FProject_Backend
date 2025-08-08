package com.fproject.FProject.model.dto;
import java.util.Set;

import com.fproject.FProject.model.entity.ImageEntity;
import com.fproject.FProject.model.entity.MemberEntity;
import com.fproject.FProject.model.entity.OptionEntity;

public record PageEvent(String owner,String name, String description, 
Set<ImageEntity> images,Set<OptionEntity> opstions, Set<MemberEntity> members) {}
