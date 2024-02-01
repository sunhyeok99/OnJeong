package com.a503.onjeong.domain.phonebook.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class PhonebookDTO {
    private Long freindId;
    private Long userId;
    private String phonebookNum;
    private String phonebookName;
    private int isChecked=0;


    @Builder
    public PhonebookDTO(
            Long userId,
            Long freindId,
            String phonebookNum,
            String phonebookName,
            int isChecked

    ) {
        this.userId = userId;
        this.freindId = freindId;
        this.phonebookNum = phonebookNum;
        this.phonebookName = phonebookName;
        this.isChecked=isChecked;
    }
}
