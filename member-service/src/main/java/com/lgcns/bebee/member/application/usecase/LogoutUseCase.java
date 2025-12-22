package com.lgcns.bebee.member.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.member.application.client.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 로그아웃 유스케이스
 */
@Service
@RequiredArgsConstructor
public class LogoutUseCase implements UseCase<LogoutUseCase.Param, Void> {
    private final TokenRepository tokenRepository;

    @Override
    public Void execute(Param params){
        if(params.accessToken != null){
            tokenRepository.addToBlacklist(params.accessToken);
        }
        tokenRepository.deleteRefreshToken(params.memberId);

        return null;
    }

    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long memberId;
        private final String accessToken;
        private final String refreshToken;
    }
}
