package com.lgcns.bebee.member.presentation.auth.dto;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class TokenResponse {
    private String accessToken;
    private Instant accessTokenExpiresAt;
}

