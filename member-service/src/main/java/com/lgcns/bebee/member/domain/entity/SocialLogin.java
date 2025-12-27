package com.lgcns.bebee.member.domain.entity;

import com.lgcns.bebee.common.data.domain.BaseTimeEntity;
import com.lgcns.bebee.member.domain.entity.vo.SocialProvider;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "social_login",
        uniqueConstraints = @UniqueConstraint(columnNames = {"provider", "provider_user_id"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialLogin extends BaseTimeEntity {

    @Id
    @Tsid
    private Long socialLoginId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SocialProvider provider;

    @Column(nullable = false, length = 100)
    private String providerUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
}

