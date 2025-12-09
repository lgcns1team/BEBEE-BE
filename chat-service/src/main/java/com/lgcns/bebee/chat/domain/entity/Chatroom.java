package com.lgcns.bebee.chat.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chatroom extends BaseTimeEntity {
    @Id @Tsid
    @Column(name = "chatroom_id")
    private Long id;

    @Column(length = 50)
    private String title;
}
