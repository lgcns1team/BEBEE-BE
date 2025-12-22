package com.lgcns.bebee.member.application.client;

import java.util.Optional;

public interface TokenRepository {
    void saveRefreshToken(Long memberId, String refreshToken, Long refreshTokenExpiresIn);
    Optional<String> findRefreshTokenByMemberId(Long memberId);
    void deleteRefreshToken(Long memberId);

    void addToBlacklist(String accessToken);
}
