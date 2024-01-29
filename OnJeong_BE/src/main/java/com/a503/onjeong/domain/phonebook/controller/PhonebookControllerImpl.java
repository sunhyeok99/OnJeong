package com.a503.onjeong.domain.phonebook.controller;

import com.a503.onjeong.domain.phonebook.dto.PhonebookAllDTO;
import com.a503.onjeong.domain.phonebook.service.PhonebookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping("/phonebook")
@RestController
public class PhonebookControllerImpl implements PhonebookController {

    private final PhonebookService phonebookService;

    @Override
    @PostMapping("/save")
    public ResponseEntity<Void> phonebookSave(@RequestBody PhonebookAllDTO phonebookAllDTO) { //연락처 받기
        phonebookService.phonebookSave(phonebookAllDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
