package com.lgcns.bebee.chat.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_chatroom_members",
            columnNames = {"member1_id", "member2_id"}
        )
    }
)
public class Chatroom extends BaseTimeEntity {
    @Id @Tsid
    @Column(name = "chatroom_id")
    private Long id;

    @Column(name = "member1_id", nullable = false)
    private Long member1Id;

    @Column(name = "member2_id", nullable = false)
    private Long member2Id;

    @Column(length = 50)
    private String title;

    public static Chatroom create(Long member1Id, Long member2Id){
        Chatroom chatroom = new Chatroom();
        chatroom.member1Id = member1Id;
        chatroom.member2Id = member2Id;

        // 게시글 제목을 title로 설정

        return chatroom;
    }
}