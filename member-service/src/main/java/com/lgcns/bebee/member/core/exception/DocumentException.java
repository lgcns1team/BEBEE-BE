package com.lgcns.bebee.member.core.exception;

import lombok.Getter;

/**
 * 문서 관련 예외
 */
@Getter
public class DocumentException extends RuntimeException {

    private final String code;

    public DocumentException(DocumentErrors error) {
        super(error.getMessage());
        this.code = error.getCode();
    }
}

