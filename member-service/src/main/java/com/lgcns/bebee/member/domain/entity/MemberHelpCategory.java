package com.lgcns.bebee.member.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "member_help_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberHelpCategory extends BaseTimeEntity {

    @EmbeddedId
    private MemberHelpCategoryId id;

    @MapsId("helpCategoryId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "help_category_id", nullable = false)
    private HelpCategory helpCategory;

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
}

