package com.lgcns.bebee.member.presentation.dto;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class TokenResDTO {
    private String accessToken;
    private Instant accessTokenExpiresAt;
}

