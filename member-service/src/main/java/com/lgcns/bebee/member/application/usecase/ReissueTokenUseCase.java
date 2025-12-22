package com.lgcns.bebee.member.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.member.application.client.TokenProvider;
import com.lgcns.bebee.member.application.client.TokenRepository;
import com.lgcns.bebee.member.domain.entity.Member;
import com.lgcns.bebee.member.domain.entity.vo.TokenInfo;
import com.lgcns.bebee.member.domain.service.MemberManagement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.lgcns.bebee.common.exception.AuthenticationErrors.*;


/**
 * 토큰 재발급 유스케이스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReissueTokenUseCase implements UseCase<ReissueTokenUseCase.Param, ReissueTokenUseCase.Result> {
    private final MemberManagement memberManagement;
    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;

    @Override
    @Transactional(readOnly = true)
    public Result execute(Param params) {
        Member member = memberManagement.getExistingMember(params.memberId);

        String refreshToken = tokenRepository.findRefreshTokenByMemberId(member.getId()).orElseThrow(REFRESH_TOKEN_EXPIRED::toException);

        if (!params.refreshToken.equals(refreshToken)) {
            tokenRepository.deleteRefreshToken(member.getId());

            throw REFRESH_TOKEN_THEFT_DETECTED.toException();
        }

        TokenInfo tokenInfo = tokenProvider.generateTokens(member);
        tokenRepository.saveRefreshToken(member.getId(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpiresIn());

        return new Result(tokenInfo.getAccessToken(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpiresIn());
    }

    /**
     * 토큰 재발급 파라미터
     */
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long memberId;
        private final String refreshToken;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Result{
        private final String accessToken;
        private final String refreshToken;
        private final Long refreshTokenExpiresIn;
    }
}
