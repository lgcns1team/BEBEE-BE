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
@Table(name = "member_help_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberHelpCategoryEntity {

    @EmbeddedId
    private MemberHelpCategoryId id;

    @MapsId("helpCategoryId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "help_category_id", nullable = false)
    private HelpCategoryEntity helpCategory;

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
