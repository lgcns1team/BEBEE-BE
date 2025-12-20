package com.lgcns.bebee.member.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 공개 메시지 응답 DTO
 */
@Getter
@AllArgsConstructor(staticName = "of")
public class PublicResDTO {
    private String message;
}

