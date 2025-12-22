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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginUseCase implements UseCase<LoginUseCase.Param, LoginUseCase.Result> {
    private final MemberManagement memberManagement;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;

    @Override
    @Transactional(readOnly = true)
    public Result execute(Param param) {
        Member member = memberManagement.findMemberByEmail(param.email);

        String encodedPassword = passwordEncoder.encode(param.password);
        member.validatePassword(encodedPassword);
        member.validateLoginAvailable();

        TokenInfo tokenInfo = tokenProvider.generateTokens(member);

        tokenRepository.saveRefreshToken(member.getId(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpiresIn());

        return new Result(tokenInfo.getAccessToken(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpiresIn());
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final String email;
        private final String password;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Result{
        private final String accessToken;
        private final String refreshToken;
        private final Long refreshTokenExpiresIn;
    }
}
