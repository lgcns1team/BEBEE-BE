package com.lgcns.bebee.member.presentation.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 문서 거절 요청 DTO
 */
@Getter
@NoArgsConstructor
public class DocumentRejectReqDTO {

    private String reason;

    /**
     * 정적 팩토리 메서드
     */
    public static DocumentRejectReqDTO of(String reason) {
        DocumentRejectReqDTO dto = new DocumentRejectReqDTO();
        dto.reason = reason;
        return dto;
    }
}
