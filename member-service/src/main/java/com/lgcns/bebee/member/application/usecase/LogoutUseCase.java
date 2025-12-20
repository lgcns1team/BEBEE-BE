package com.lgcns.bebee.member.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.member.domain.service.AuthService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 로그아웃 유스케이스
 */
@Service
@RequiredArgsConstructor
public class LogoutUseCase implements UseCase<LogoutUseCase.Param, Void> {

    private final AuthService authService;

    @Override
    public Void execute(Param param) {
        param.validate();
        authService.logout(param.getRefreshToken());
        return null;
    }

    /**
     * 로그아웃 파라미터
     */
    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final String refreshToken;

        @Override
        public boolean validate() {
            // refreshToken이 null이어도 로그아웃은 처리 (이미 만료된 경우 등)
            return true;
        }
    }
}
