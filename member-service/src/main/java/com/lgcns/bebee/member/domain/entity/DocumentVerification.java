package com.lgcns.bebee.member.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import com.lgcns.bebee.member.domain.entity.vo.DocumentStatus;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "document_verification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DocumentVerification extends BaseTimeEntity {

    @Id
    @Tsid
    private Long documentVerificationId;

    @Column(nullable = false, length = 255)
    private String fileUrl;

    private Integer exifScore;

    private Integer ocrScore;

    private Integer forgeryScore;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private DocumentStatus status = DocumentStatus.PENDING;

    @Column(length = 255)
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;
}

