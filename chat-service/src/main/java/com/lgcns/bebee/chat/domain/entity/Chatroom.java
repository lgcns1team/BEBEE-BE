package com.lgcns.bebee.chat.domain.entity;

import com.lgcns.bebee.common.data.domain.BaseTimeEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    private Long postId;

    @Column(length = 50)
    private String title;

    @Column(length = 31)
    private String lastMessage;

    @OneToMany(mappedBy = "chatroom", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ChatroomHelpCategory> chatroomHelpCategories = new ArrayList<>();

    public static Chatroom create(MemberSync member1, MemberSync member2){
        Chatroom chatroom = new Chatroom();
        chatroom.member1 = member1;
        chatroom.member2 = member2;

        return chatroom;
    }

    /**
     * 게시글을 연결하고 도움 카테고리를 설정합니다.
     *
     * @param postId 게시글 ID
     * @param postTitle 게시글 제목
     * @param helpCategories 도움 카테고리 목록
     */
    public void linkPost(Long postId, String postTitle, List<HelpCategorySync> helpCategories){
        this.postId = postId;
        this.title = postTitle;

        this.chatroomHelpCategories.clear();

        if(postId != null){
            helpCategories.forEach(helpCategory -> {
                ChatroomHelpCategory chatroomHelpCategory = ChatroomHelpCategory.create(this, helpCategory);
                this.chatroomHelpCategories.add(chatroomHelpCategory);
            });
        }
    }

    public void updateLastMessage(String lastMessage){
        this.lastMessage = lastMessage;
    }
}