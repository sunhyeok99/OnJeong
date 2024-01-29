package com.a503.onjeong.domain.usergroup.service;

import com.a503.onjeong.domain.group.Group;
import com.a503.onjeong.domain.group.dto.GroupDTO;
import com.a503.onjeong.domain.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public interface UserGroupService {

    @Transactional
    void userGroupCreate(List<User> userList, Long groupId);

    @Transactional
    void userGroupDelete(Long groupId);

    @Transactional
    void userGroupUpdate(GroupDTO groupDTO);

    List<GroupDTO> findAllByGroupId(Optional<List<Group>> groupList);

}
