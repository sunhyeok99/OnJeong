package com.a503.onjeong.domain.phonebook;

import com.a503.onjeong.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Phonebook {

    @EmbeddedId
    private PhonebookId phonebookId;

    @ManyToOne() //userId로
    private User user;

    @Column(name = "phonebook_name")
    private String phonebookName;

    @Column(name = "phonebook_num")
    private String phonebookNum;
}
