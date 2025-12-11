package com.lgcns.bebee.member.presentation.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 문서 업로드 응답 DTO
 */
@Getter
@RequiredArgsConstructor
public class DocumentUploadResDTO {

    private final Long verificationId;
    private final String fileUrl;
    private final Integer forgeryScore;
    private final String systemFlag;
    private final String status;

    /**
     * 정적 팩토리 메서드
     */
    public static DocumentUploadResDTO of(
            Long verificationId,
            String fileUrl,
            Integer forgeryScore,
            String systemFlag,
            String status
    ) {
        return new DocumentUploadResDTO(verificationId, fileUrl, forgeryScore, systemFlag, status);
    }
}
