package com.lgcns.bebee.member.domain.entity;

import com.lgcns.bebee.member.domain.entity.vo.DocumentStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "document_verification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DocumentVerificationEntity {

    @Id
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
    private DocumentEntity document;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
