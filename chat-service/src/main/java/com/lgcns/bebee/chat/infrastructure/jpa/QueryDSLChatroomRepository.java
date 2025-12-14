package com.lgcns.bebee.chat.infrastructure.jpa;

import com.lgcns.bebee.chat.domain.entity.Chatroom;
import com.lgcns.bebee.chat.infrastructure.jpa.dto.ChatroomSearchCond;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.lgcns.bebee.chat.domain.entity.QChatroom.chatroom;

@Repository
@RequiredArgsConstructor
public class QueryDSLChatroomRepository{
    private final JPAQueryFactory queryFactory;

    public Optional<Chatroom> findChatroomWithMembers(ChatroomSearchCond condition) {
        Chatroom result = queryFactory
                .selectFrom(chatroom)
                .leftJoin(chatroom.member1).fetchJoin()
                .leftJoin(chatroom.member2).fetchJoin()
                .where(
                        chatroomIdEq(condition.chatroomId()),
                        memberIdsEq(condition.member1Id(), condition.member2Id())
                )
                .fetchOne();

        return Optional.ofNullable(result);
    }

    /**
     * chatroomId 조건
     */
    private BooleanExpression chatroomIdEq(Long chatroomId) {
        return chatroomId != null ? chatroom.id.eq(chatroomId) : null;
    }

    /**
     * member1Id, member2Id 조건 (둘 다 null이 아닐 때만 적용)
     */
    private BooleanExpression memberIdsEq(Long member1Id, Long member2Id) {
        if (member1Id == null || member2Id == null) {
            return null;
        }
        return chatroom.member1.id.eq(member1Id)
                .and(chatroom.member2.id.eq(member2Id));
    }
}