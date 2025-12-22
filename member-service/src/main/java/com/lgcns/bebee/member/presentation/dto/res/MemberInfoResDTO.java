package com.lgcns.bebee.member.presentation.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 회원 정보 응답 DTO
 */
@Getter
@AllArgsConstructor(staticName = "of")
public class MemberInfoResDTO {
    private Long memberId;
    private String email;
    private String name;
    private String nickname;
    private String role;
}

