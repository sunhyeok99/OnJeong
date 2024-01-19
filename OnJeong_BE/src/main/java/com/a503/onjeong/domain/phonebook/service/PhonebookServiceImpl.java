package com.a503.onjeong.domain.phonebook.service;

import com.a503.onjeong.domain.phonebook.dto.PhonebookDTO;
import com.a503.onjeong.domain.phonebook.repository.PhonebookRepository;
import com.a503.onjeong.domain.user.User;
import com.a503.onjeong.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class PhonebookServiceImpl implements PhonebookService {


    private UserRepository userRepository;
    private PhonebookRepository phonebookRepository;

    @Override
    public void phonebookList(PhonebookDTO phonebookDTO) {
        Long userId = phonebookDTO.getUserId();
        //dto에서 map 꺼내<전화번호, 이름> => 전화번호 값 리스트로 만들어
        Map<String, String> phonebook = phonebookDTO.getPhonebook();
        Set<String> phoneNumList = phonebook.keySet();
        //이제 userrepository에서 찾기
        List<User> userList =new ArrayList<>();
//                userRepository.findPhoneBookUser(phoneNumList);
        //User의 phoneNum과 phonebook의 키 값이 같은 거 찾아서 이름 찾아!!!!
        for (User user : userList) {
            for (String phonebookNum : phoneNumList) {
                if (user.getPhoneNumber().equals(phonebookNum)){//일치
                    //userId, friendId=user.getUserId,phoneNum=phonebookNum,phonebookName=phonebook.get(phonebookNum)
                    //phonebookrepo에서 persist

                }
            }
        }
    }
}
