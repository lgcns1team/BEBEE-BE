package com.lgcns.bebee.match.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Match extends BaseTimeEntity {
    @Id
    @Tsid
    private Long match_id;

    @Column(nullable = false)
    private Long helper_id;

    @Column(nullable = false)
    private Long disabled_id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Long post_id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private Long chat_room_id;
}
