package com.lgcns.bebee.member.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 문서 관련 예외 핸들러
 * DocumentException 발생 시 일관된 에러 응답 반환
 */
@Slf4j
@RestControllerAdvice
public class DocumentExceptionHandler {

    /**
     * DocumentException 처리
     * @param e DocumentException
     * @return 에러 응답
     */
    @ExceptionHandler(DocumentException.class)
    public ResponseEntity<Map<String, Object>> handleDocumentException(DocumentException e) {
        log.warn("DocumentException 발생: code={}, message={}", e.getCode(), e.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("code", e.getCode());
        response.put("message", e.getMessage());
        response.put("timestamp", LocalDateTime.now());

        HttpStatus status = resolveHttpStatus(e.getCode());
        return ResponseEntity.status(status).body(response);
    }

    /**
     * 에러 코드에 따른 HTTP 상태 결정
     * @param code 에러 코드
     * @return HTTP 상태
     */
    private HttpStatus resolveHttpStatus(String code) {
        if (code == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return switch (code) {
            case "DOCUMENT_001", "DOCUMENT_002" -> HttpStatus.NOT_FOUND;           // NOT_FOUND, DOCUMENT_NOT_FOUND
            case "DOCUMENT_003" -> HttpStatus.CONFLICT;                             // ALREADY_PROCESSED
            case "DOCUMENT_004" -> HttpStatus.BAD_REQUEST;                          // INVALID_FILE_TYPE
            case "DOCUMENT_005", "DOCUMENT_006", "DOCUMENT_007" -> HttpStatus.INTERNAL_SERVER_ERROR;  // 서버 처리 오류
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}

