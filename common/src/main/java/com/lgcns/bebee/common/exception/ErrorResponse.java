package com.lgcns.bebee.common.exception;

import java.time.LocalDateTime;

/**
 * 모든 서비스의 에러 응답에 사용되는 공통 DTO
 * 클라이언트에게 JSON 형태로 반환됩니다
 */
public record ErrorResponse(
        String code,
        String message,
        LocalDateTime timestamp
) {}
