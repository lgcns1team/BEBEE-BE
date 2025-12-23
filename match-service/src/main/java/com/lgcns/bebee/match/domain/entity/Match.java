package com.lgcns.bebee.match.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "`match`")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Match extends BaseTimeEntity {
    @Id
    @Tsid
    private Long matchId;

    @Column(nullable = false)
    private Long helperId;

    @Column(nullable = false)
    private Long disabledId;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private Long chatRoomId;

    @Column(nullable = false, unique = true)
    private Long agreementId;

    public boolean isParticipant(Long memberId) {
        return this.helperId.equals(memberId) || this.disabledId.equals(memberId);
    }

    public static Match create(
            Long helperId,
            Long disabledId,
            Long postId,
            String title,
            Long chatRoomId,
            Long agreementId
    ) {
        Match match = new Match();
        match.helperId = helperId;
        match.disabledId = disabledId;
        match.postId = postId;
        match.title = title;
        match.chatRoomId = chatRoomId;
        match.agreementId = agreementId;

        return match;
    }
}
