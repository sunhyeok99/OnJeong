package com.a503.onjeong.domain.phonebook.service;

import com.a503.onjeong.domain.phonebook.Phonebook;
import com.a503.onjeong.domain.phonebook.dto.PhonebookDTO;
import com.a503.onjeong.domain.phonebook.repository.PhonebookRepository;
import com.a503.onjeong.domain.user.User;
import com.a503.onjeong.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class PhonebookServiceImpl implements PhonebookService {

    private final UserRepository userRepository;
    private final PhonebookRepository phonebookRepository;

    @Override
    public void phonebookList(PhonebookDTO phonebookDTO) { //연락처에서 유저들만 phonebook db에 저장
        Long userId = phonebookDTO.getUserId(); //유저 id
        Map<String, String> phonebook = phonebookDTO.getPhonebook(); //<전화번호, 이름>

        List<String> phoneNumList = new ArrayList<>(phonebook.keySet());//전화번호 리스트
        List<User> userList = userRepository.findByPhoneBook(phoneNumList); //연락처에 있는 유저객체가 담긴 리스트

        for (User user : userList) {
            for (String phonebookNum : phoneNumList) { 
                if (user.getPhoneNumber().equals(phonebookNum)) { //연락처 이름과 매칭
                    Long friendId = user.getId();
                    //db 저장
                    phonebookRepository.save(new Phonebook(userId, friendId, phonebookNum, phonebook.get(phonebookNum)));
                }
            }
        }
    }
}
