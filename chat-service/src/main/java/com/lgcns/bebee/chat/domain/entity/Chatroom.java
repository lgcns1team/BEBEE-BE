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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member1_id")
    private MemberSync member1;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member2_id")
    private MemberSync member2;

    @Column(length = 50)
    private String title;

    public static Chatroom create(MemberSync member1, MemberSync member2){
        Chatroom chatroom = new Chatroom();
        chatroom.member1 = member1;
        chatroom.member2 = member2;

        // 게시글 제목을 title로 설정

        return chatroom;
    }
}