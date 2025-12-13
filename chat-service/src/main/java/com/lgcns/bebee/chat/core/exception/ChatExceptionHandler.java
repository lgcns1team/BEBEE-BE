package com.lgcns.bebee.chat.core.exception;

import com.lgcns.bebee.common.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

import static com.lgcns.bebee.chat.core.exception.ChatErrors.*;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ChatExceptionHandler {
    private final Map<ChatErrors, HttpStatus> statusMap = Map.of(
            CHATROOM_NOT_FOUND, NOT_FOUND
    );

    @ExceptionHandler(ChatException.class)
    public ResponseEntity<ErrorResponse> handle(ChatException e){
        ChatErrors errors = e.getError();

        return ResponseEntity.status(
                statusMap.getOrDefault(errors, INTERNAL_SERVER_ERROR)
        ).body(new ErrorResponse(errors.name(), errors.getMessage(), LocalDateTime.now()));
    }
}
