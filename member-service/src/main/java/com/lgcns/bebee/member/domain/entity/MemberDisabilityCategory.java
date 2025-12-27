package com.lgcns.bebee.member.domain.entity;

import com.lgcns.bebee.common.data.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "member_disability_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDisabilityCategory extends BaseTimeEntity {

    @EmbeddedId
    private MemberDisabilityCategoryId id;

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @MapsId("disabilityCategoryId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disability_category_id", nullable = false)
    private DisabilityCategory disabilityCategory;

    @Column(nullable = false, length = 1)
    private String level;

    @Column(nullable = false, length = 300)
    private String disabilityDescription;
}

