package com.a503.onjeong.domain.group.controller;

import com.a503.onjeong.domain.group.dto.GroupDTO;
import com.a503.onjeong.domain.group.dto.GroupUserListDTO;
import com.a503.onjeong.domain.group.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/group")
@RestController
public class GroupControllerImpl implements GroupController {
    private final GroupService groupService;

    @GetMapping("/list")
    public ResponseEntity<List<GroupDTO>> groupList(@RequestParam(value = "userId") Long userId) {
        List<GroupDTO> groupList = groupService.groupList(userId);
        return new ResponseEntity<>(groupList, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> groupCreate(@RequestBody GroupUserListDTO groupUserListDTO) {
        groupService.groupCreate(groupUserListDTO);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> groupDelete(@RequestParam(value = "groupId") Long groupId) {
        groupService.groupDelete(groupId);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Void> groupUpdate(@RequestBody GroupUserListDTO groupUserListDTO) {
        groupService.groupUpdate(groupUserListDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
