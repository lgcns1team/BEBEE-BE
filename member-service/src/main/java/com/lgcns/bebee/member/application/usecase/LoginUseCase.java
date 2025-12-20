package com.lgcns.bebee.member.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.member.domain.service.AuthService;
import com.lgcns.bebee.member.infrastructure.security.JwtTokenProvider.TokenPair;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 로그인 유스케이스
 */
@Service
@RequiredArgsConstructor
public class LoginUseCase implements UseCase<LoginUseCase.Param, TokenPair> {

    private final AuthService authService;

    @Override
    @Transactional(readOnly = true)
    public TokenPair execute(Param param) {
        param.validate();
        return authService.login(new AuthService.LoginCommand(param.getEmail(), param.getPassword()));
    }

    /**
     * 로그인 파라미터
     */
    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final String email;
        private final String password;

        @Override
        public boolean validate() {
            // 기본 검증은 @Valid로 처리되므로 여기서는 추가 검증만 수행
            return true;
        }
    }
}
