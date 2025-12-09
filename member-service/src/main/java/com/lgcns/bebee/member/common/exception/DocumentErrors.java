package com.lgcns.bebee.member.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 문서 관련 에러 정의
 */
@Getter
@RequiredArgsConstructor
public enum DocumentErrors {

    NOT_FOUND("DOCUMENT_001", "문서를 찾을 수 없습니다."),
    ALREADY_PROCESSED("DOCUMENT_002", "이미 처리된 문서입니다."),
    INVALID_FILE_TYPE("DOCUMENT_003", "지원하지 않는 파일 형식입니다."),
    FILE_UPLOAD_FAILED("DOCUMENT_004", "파일 업로드에 실패했습니다."),
    OCR_FAILED("DOCUMENT_005", "OCR 처리에 실패했습니다."),
    EXIF_FAILED("DOCUMENT_006", "EXIF 추출에 실패했습니다.");

    private final String code;
    private final String message;

    /**
     * 에러를 Exception으로 변환
     * @return DocumentException
     */
    public DocumentException toException() {
        return new DocumentException(this);
    }
}
