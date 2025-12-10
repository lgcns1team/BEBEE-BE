package com.lgcns.bebee.member.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "document")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Document extends BaseTimeEntity {

    @Id
    @Tsid
    private Long documentId;

    @Column(nullable = false, length = 20)
    private String targetRole;

    @Column(nullable = false, length = 30, unique = true)
    private String docCode;

    @Column(nullable = false, length = 50)
    private String docNameKo;

    @Column(nullable = false, length = 255)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
}

