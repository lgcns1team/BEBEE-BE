package com.lgcns.bebee.member.presentation.exception;

import com.lgcns.bebee.common.exception.ErrorResponse;
import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.member.common.exception.AuthErrors;
import com.lgcns.bebee.member.common.exception.AuthException;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AuthExceptionHandler {

    private static final Map<AuthErrors, HttpStatus> AUTH_STATUS = Map.of(
            AuthErrors.INVALID_CREDENTIALS, HttpStatus.UNAUTHORIZED,
            AuthErrors.MEMBER_STATUS_PENDING, HttpStatus.FORBIDDEN,
            AuthErrors.MEMBER_STATUS_REJECTED, HttpStatus.FORBIDDEN,
            AuthErrors.MEMBER_STATUS_WITHDRAWN, HttpStatus.FORBIDDEN
    );

    @ExceptionHandler(InvalidParamException.class)
    public ResponseEntity<ErrorResponse> handleInvalidParam(InvalidParamException e) {
        log.info("InvalidParamException: {}", e.getMessage());
        ErrorResponse body = new ErrorResponse(e.error.getCode(), e.error.getDesc(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuth(AuthException e) {
        log.info("AuthException: {}", e.getMessage());
        HttpStatus status = AUTH_STATUS.getOrDefault(e.getError(), HttpStatus.UNAUTHORIZED);
        ErrorResponse body = new ErrorResponse(e.getError().getCode(), e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(status).body(body);
    }
}

