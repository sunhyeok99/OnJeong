package com.a503.onjeong.domain.phonebook;

import com.a503.onjeong.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Phonebook {

    @EmbeddedId
    private PhonebookId phonebookId;

    @ManyToOne(fetch=FetchType.LAZY) //userId로
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "phonebook_name")
    private String phonebookName;

    @Column(name = "phonebook_num")
    private String phonebookNum;
}
