package com.lgcns.bebee.member.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.member.common.exception.AuthErrors;
import com.lgcns.bebee.member.domain.service.AuthService;
import com.lgcns.bebee.member.infrastructure.security.JwtTokenProvider.TokenPair;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 토큰 재발급 유스케이스
 */
@Service
@RequiredArgsConstructor
public class ReissueUseCase implements UseCase<ReissueUseCase.Param, TokenPair> {

    private final AuthService authService;

    @Override
    @Transactional(readOnly = true)
    public TokenPair execute(Param param) {
        param.validate();
        return authService.reissue(param.getRefreshToken());
    }

    /**
     * 토큰 재발급 파라미터
     */
    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final String refreshToken;

        @Override
        public boolean validate() {
            if (refreshToken == null || refreshToken.isBlank()) {
                throw AuthErrors.INVALID_CREDENTIALS.toException();
            }
            return true;
        }
    }
}
