package com.a503.onjeong.domain.phonebook;

import com.a503.onjeong.domain.user.User;
import com.a503.onjeong.domain.user.UserType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@NoArgsConstructor
@DynamicUpdate  //바뀐 컬럼만 수정
public class Phonebook {

    @EmbeddedId //복합키 매핑(userId+freindId)
    private PhonebookId phonebookId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id" ,insertable = false, updatable = false)
    private User user;

    @Column(name = "phonebook_num")
    private String phonebookNum;

    @Column(name = "phonebook_name")
    private String phonebookName;
    @Builder
    public Phonebook(
            Long userId,
            Long friendId,
            String phonebookNum,
            String phonebookName
    ) {
        this.phonebookId=new PhonebookId(userId,friendId);
        this.phonebookNum = phonebookNum;
        this.phonebookName = phonebookName;
    }
}
