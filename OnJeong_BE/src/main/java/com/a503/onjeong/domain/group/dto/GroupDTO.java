package com.a503.onjeong.domain.group.dto;

import com.a503.onjeong.domain.user.dto.UserDTO;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class GroupDTO {
    private Long groupId;
    private Long ownerId;
    private String name;
    private List<UserDTO> userList=new ArrayList<>();
    @Builder
    public GroupDTO(
            Long groupId,
            Long ownerId,
            String name,
            List<UserDTO> userList
    ) {
        this.groupId = groupId;
        this.name = name;
        this.ownerId = ownerId;
        this.userList = userList;
    }
}
