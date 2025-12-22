package com.lgcns.bebee.match.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "`match`")
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

    public boolean isParticipant(Long memberId) {
        return this.helperId.equals(memberId) || this.disabledId.equals(memberId);
    }
}
