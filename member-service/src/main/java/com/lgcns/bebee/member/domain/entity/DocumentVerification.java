package com.lgcns.bebee.member.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import com.lgcns.bebee.member.domain.entity.vo.DocumentStatus;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 문서 검증 엔티티
 */
@Entity
@Getter
@Table(name = "document_verification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DocumentVerification extends BaseTimeEntity {

    @Id
    @Tsid @Column(name = "document_verification_id")
    private Long id;

    @Column(nullable = false, length = 255)
    private String fileUrl;

    private Integer exifScore;

    private Integer ocrScore;

    private Integer forgeryScore;

    @Column(length = 10)
    private String systemFlag;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private DocumentStatus status = DocumentStatus.PENDING;

    @Column(length = 255)
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    /**
     * 문서 검증 생성 (정적 팩토리 메서드)
     * @param fileUrl 파일 URL
     * @param document 문서 엔티티
     * @return 생성된 DocumentVerification
     */
    public static DocumentVerification of(String fileUrl, Document document) {
        DocumentVerification verification = new DocumentVerification();
        verification.fileUrl = fileUrl;
        verification.document = document;
        verification.status = DocumentStatus.PENDING;
        return verification;
    }

    /**
     * 분석 결과 적용
     * @param exifScore EXIF 점수
     * @param ocrScore OCR 점수
     * @param forgeryScore 종합 위변조 점수
     * @param systemFlag 시스템 플래그 (LOW/MID/HIGH)
     */
    public void applyAnalysisResult(Integer exifScore, Integer ocrScore, Integer forgeryScore, String systemFlag) {
        this.exifScore = exifScore;
        this.ocrScore = ocrScore;
        this.forgeryScore = forgeryScore;
        this.systemFlag = systemFlag;
    }

    /**
     * 승인 처리
     */
    public void approve() {
        this.status = DocumentStatus.APPROVED;
    }

    /**
     * 거절 처리
     * @param reason 거절 사유
     */
    public void reject(String reason) {
        this.status = DocumentStatus.REJECTED;
        this.reason = reason;
    }
}

