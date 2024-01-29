package com.a503.onjeong.domain.phonebook.service;

import com.a503.onjeong.domain.phonebook.Phonebook;
import com.a503.onjeong.domain.phonebook.dto.PhonebookAllDTO;
import com.a503.onjeong.domain.phonebook.repository.PhonebookRepository;
import com.a503.onjeong.domain.user.User;
import com.a503.onjeong.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PhonebookServiceImpl implements PhonebookService {

    private final UserRepository userRepository;
    private final PhonebookRepository phonebookRepository;

    @Override
    @Transactional
    public void phonebookSave(PhonebookAllDTO phonebookAllDTO) { //연락처에서 유저들만 phonebook db에 저장
        Long userId = phonebookAllDTO.getUserId(); //유저 id
        Map<String, String> phonebook = phonebookAllDTO.getPhonebook(); //<전화번호, 이름>

        List<String> phoneNumList = new ArrayList<>(phonebook.keySet());//전화번호 리스트
        List<User> userList = userRepository.findByPhoneBook(phoneNumList); //연락처에 있는 유저객체가 담긴 리스트

        for (User user : userList) {
            //db 저장
            Phonebook phonebook1= Phonebook.builder()
                    .phonebookNum(user.getPhoneNumber())
                    .user(user)
                    .phonebookName(phonebook.get(user.getPhoneNumber()))
                    .userId(userId)
                    .friendId(user.getId())
                    .build();
            phonebookRepository.save(phonebook1);
        }
    }
}
