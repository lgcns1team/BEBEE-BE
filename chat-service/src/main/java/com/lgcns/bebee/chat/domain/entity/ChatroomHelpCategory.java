package com.lgcns.bebee.chat.domain.entity;

import com.lgcns.bebee.common.data.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

/**
 * Chatroom과 HelpCategorySync 간의 N:M 관계를 풀기 위한 중간 테이블 엔티티
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chatroom_help_category")
public class ChatroomHelpCategory extends BaseTimeEntity {

    @EmbeddedId
    private ChatroomHelpCategoryId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("chatroomId")
    @JoinColumn(name = "chatroom_id", nullable = false)
    private Chatroom chatroom;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("helpCategorySyncId")
    @JoinColumn(name = "help_category_id", nullable = false)
    private HelpCategorySync helpCategory;

    public static ChatroomHelpCategory create(Chatroom chatroom, HelpCategorySync helpCategory) {
        ChatroomHelpCategory entity = new ChatroomHelpCategory();
        entity.id = new ChatroomHelpCategoryId(chatroom.getId(), helpCategory.getId());
        entity.chatroom = chatroom;
        entity.helpCategory = helpCategory;
        return entity;
    }

    /**
     * Chatroom과 HelpCategorySync의 복합 키
     */
    @Embeddable
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class ChatroomHelpCategoryId implements Serializable {
        @Column(name = "chatroom_id")
        private Long chatroomId;

        @Column(name = "help_category_sync_id")
        private Long helpCategorySyncId;
    }
}
