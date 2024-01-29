package com.a503.onjeong.domain.usergroup.service;

import com.a503.onjeong.domain.group.Group;
import com.a503.onjeong.domain.group.dto.GroupDTO;
import com.a503.onjeong.domain.group.repository.GroupRepository;
import com.a503.onjeong.domain.user.User;
import com.a503.onjeong.domain.user.dto.UserDTO;
import com.a503.onjeong.domain.usergroup.UserGroup;
import com.a503.onjeong.domain.usergroup.repository.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserGroupServiceImpl implements UserGroupService {
    private final UserGroupRepository userGroupRepository;
    private final GroupRepository groupRepository;

    @Override
    @Transactional
    public void userGroupCreate(List<User> userList, Long groupId) {
        Optional<Group> group = groupRepository.findById(groupId);
        Group group1 = group.orElseThrow();

        for (User user : userList) {
            UserGroup userGroup = UserGroup.builder()
                    .userId(user.getId())
                    .groupId(groupId)
                    .group(group1)
                    .user(user)
                    .build();

            userGroupRepository.save(userGroup);
        }
    }

    @Override
    public void userGroupDelete(Long groupId) {
        userGroupRepository.deleteAllByGroupId(groupId);
    }

    @Override
    public void userGroupUpdate(GroupDTO groupDTO) {
        Optional<List<UserGroup>> list = userGroupRepository.findAllByGroupId(groupDTO.getGroupId());

        List<UserDTO> userList = groupDTO.getUserList();
        h:
        for (UserGroup userGroup : list.orElseThrow()) { //지금 있는 그룹에 멤버들
            for (UserDTO user : userList) {//새로운 멤버들
                if (userGroup.getUser().getId() == user.getUserId()) continue h;

            }
        }
    }

    @Override
    public List<GroupDTO> findAllByGroupId(Optional<List<Group>> groupList) {
        //그룹 리스트에서 group을 빼서 usergroup에서 user
        List<GroupDTO> groupDTOList = new ArrayList<>();
        for (Group group : groupList.orElseThrow()) {
            Optional<List<UserGroup>> userList = userGroupRepository.findAllByGroupId(group.getId());
            List<UserDTO> userDTOList = new ArrayList<>();
            for (UserGroup userGroup : userList.orElseThrow()) {
                User user = userGroup.getUser();
                UserDTO userDTO = UserDTO.builder()
                        .userId(user.getId())
                        .name(user.getName())
                        .phoneNumber(user.getPhoneNumber())
                        .build();
                userDTOList.add(userDTO);
            }
            GroupDTO groupDTO = GroupDTO.builder()
                            .userList(userDTOList)
                            .groupId(group.getId())
                            .name(group.getName())
                            .build();
            groupDTOList.add(groupDTO);

        }
        return groupDTOList;
    }
}
