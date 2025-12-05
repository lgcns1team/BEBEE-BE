package com.lgcns.bebee.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String ERROR_CODE = "UNKNOWN_ERROR";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception e){
        log.error("Unhandled Exception Occured: {}", e.getMessage(), e);

        ErrorResponse response = new ErrorResponse(
                ERROR_CODE,
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    public record ErrorResponse(String code, String message, LocalDateTime timestamp) {}
}
