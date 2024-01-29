package com.a503.onjeong.domain.group.dto;

import com.a503.onjeong.domain.user.User;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GroupUserListDTO {
    private Long groupId;
    private Long ownerId;
    private String name;
    private List<User> userList = new ArrayList<>();

    @Builder
    public GroupUserListDTO(
            Long groupId,
            Long ownerId,
            String name,
            List<User> userList
    ) {
        this.groupId = groupId;
        this.name = name;
        this.ownerId = ownerId;
        this.userList = userList;
    }
}
