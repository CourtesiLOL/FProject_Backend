package com.fproject.FProject.model.dto;

import com.fproject.FProject.model.entity.ElectionEntity;
import com.fproject.FProject.model.entity.MemberEntity;
import java.util.Set;

/**
 *
 * @author javier
 */
public record FullEventDTO(long id,String name,String description, Set<ImageDTO> images,
Set<ElectionEntity> elections, Set<MemberEntity> members, String shareCode) {

}
