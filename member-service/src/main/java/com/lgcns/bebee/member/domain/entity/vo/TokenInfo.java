package com.lgcns.bebee.member.domain.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenInfo {
    private String accessToken;
    private String refreshToken;
    private Long refreshTokenExpiresIn;
}
