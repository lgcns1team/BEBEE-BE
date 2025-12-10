package com.lgcns.bebee.match.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class MatchExceptionHandler {
    private static final Map<MatchErrors, HttpStatus> STATUS_MAP = Map.of(
            MatchErrors.NOT_FOUND, HttpStatus.NOT_FOUND
    );

    /**
     * 던져진 예외(MatchException)를 잡아서 클라이언트에게 보낼 HTTP 응답으로 변환
     */
    @ExceptionHandler(MatchException.class)
    public ResponseEntity<ErrorResponse> handle(MatchException e){
        if (e.getCause() != null) {
            log.info("{} : {}", e.getCause().getClass(), e.getCause().getMessage());
        } else {
            log.info(e.getMessage());
        }

        MatchErrors error = e.getError();
        HttpStatus status = STATUS_MAP.getOrDefault(error, HttpStatus.BAD_REQUEST);

        ErrorResponse response = new ErrorResponse(
                error.name(),
                error.getDesc(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(status).body(response);
    }

    public record ErrorResponse(String code, String message, LocalDateTime timestamp) {}
}
