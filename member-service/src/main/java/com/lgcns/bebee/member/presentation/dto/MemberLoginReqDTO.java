package com.lgcns.bebee.member.presentation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 로그인 요청 DTO
 */
@Getter
@NoArgsConstructor
public class MemberLoginReqDTO {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}

