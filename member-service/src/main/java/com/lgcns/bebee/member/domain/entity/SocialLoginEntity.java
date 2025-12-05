package com.lgcns.bebee.member.domain.entity;

import com.lgcns.bebee.member.domain.entity.vo.SocialProvider;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "social_login",
        uniqueConstraints = @UniqueConstraint(columnNames = {"provider", "provider_user_id"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialLoginEntity {

    @Id
    private Long socialLoginId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SocialProvider provider;

    @Column(nullable = false, length = 100)
    private String providerUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

