package com.lgcns.bebee.member.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "member_disability_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDisabilityCategoryEntity {

    @EmbeddedId
    private MemberDisabilityCategoryId id;

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @MapsId("disabilityCategoryId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disability_category_id", nullable = false)
    private DisabilityCategoryEntity disabilityCategory;

    @Column(nullable = false, length = 1)
    private String level;

    @Column(nullable = false, length = 300)
    private String disabilityDescription;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
