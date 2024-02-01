package com.a503.onjeong.domain.phonebook.controller;

import com.a503.onjeong.domain.phonebook.dto.PhonebookAllDTO;
import com.a503.onjeong.domain.phonebook.dto.PhonebookDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public interface PhonebookController {
    @PostMapping("/save")
    ResponseEntity<Void> phonebookSave(@RequestBody PhonebookAllDTO phonebookAllDTO);

    @GetMapping("/list")
    ResponseEntity<List<PhonebookDTO>> phonebookList(
            Long userId, @RequestParam(required = false) Long groupId);
}
