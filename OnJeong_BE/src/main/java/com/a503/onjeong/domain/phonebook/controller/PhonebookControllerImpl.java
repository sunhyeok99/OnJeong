package com.a503.onjeong.domain.phonebook.controller;

import com.a503.onjeong.domain.phonebook.dto.PhonebookDTO;
import com.a503.onjeong.domain.phonebook.service.PhonebookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/phonebook")
@RestController
public class PhonebookControllerImpl implements PhonebookController{

    private final PhonebookService phonebookService;

    @Override
    @PostMapping("/list")
    public void phonebookList(PhonebookDTO phonebookDTO) {
        phonebookService.phonebookList(phonebookDTO);
    }
}
