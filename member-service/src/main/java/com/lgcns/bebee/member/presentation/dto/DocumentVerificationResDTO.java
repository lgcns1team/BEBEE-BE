package com.lgcns.bebee.member.presentation.dto;

import com.lgcns.bebee.member.domain.entity.DocumentVerification;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 문서 검증 조회 응답 DTO
 */
@Getter
@RequiredArgsConstructor
public class DocumentVerificationResDTO {

    private final Long verificationId;
    private final String fileUrl;
    private final Integer exifScore;
    private final Integer ocrScore;
    private final Integer forgeryScore;
    private final String systemFlag;
    private final String status;
    private final String reason;

    /**
     * 엔티티에서 DTO로 변환
     * @param entity DocumentVerification 엔티티
     * @return DocumentVerificationResDTO
     */
    public static DocumentVerificationResDTO from(DocumentVerification entity) {
        return new DocumentVerificationResDTO(
                entity.getDocumentVerificationId(),
                entity.getFileUrl(),
                entity.getExifScore(),
                entity.getOcrScore(),
                entity.getForgeryScore(),
                entity.getSystemFlag(),
                entity.getStatus().name(),
                entity.getReason()
        );
    }
}
