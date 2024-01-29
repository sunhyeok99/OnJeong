package com.a503.onjeong.domain.group;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name="meet")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @Column(name = "group_name")
    private String name;

    @Column(name = "owner_id")
    private Long ownerId;


    @Builder
    public Group(
            String name,
            Long ownerId
    ){
        this.name=name;
        this.ownerId=ownerId;
    }
}
