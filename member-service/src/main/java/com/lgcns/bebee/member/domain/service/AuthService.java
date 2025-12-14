    package com.lgcns.bebee.member.domain.service;

import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.member.common.exception.AuthErrors;
import com.lgcns.bebee.member.common.exception.AuthInvalidParamErrors;
import com.lgcns.bebee.member.common.exception.AuthException;
import com.lgcns.bebee.member.domain.entity.Member;
import com.lgcns.bebee.member.domain.entity.vo.Gender;
import com.lgcns.bebee.member.domain.entity.vo.MemberStatus;
import com.lgcns.bebee.member.domain.entity.vo.Role;
import com.lgcns.bebee.member.domain.repository.MemberRepository;
import com.lgcns.bebee.member.infrastructure.security.JwtTokenProvider;
import com.lgcns.bebee.member.infrastructure.security.JwtTokenProvider.TokenPair;
import com.lgcns.bebee.member.infrastructure.security.RefreshTokenBlacklistService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final MemberCommandService memberCommandService;
    private final PasswordPolicyValidator passwordPolicyValidator;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenBlacklistService refreshTokenBlacklistService;

    @Transactional
    public TokenPair signup(SignupCommand command) {
        Role role = parseRole(command.role());
        Gender gender = parseGender(command.gender());

        passwordPolicyValidator.validate(command.password());
        String encodedPassword = passwordEncoder.encode(command.password());

        Member member = memberCommandService.registerMember(
                command.email(),
                encodedPassword,
                command.name(),
                command.nickname(),
                command.birthDate(),
                gender,
                role,
                command.phoneNumber(),
                command.addressRoad(),
                command.latitude(),
                command.longitude(),
                command.districtCode()
        );

        return jwtTokenProvider.generateTokens(member);
    }

    @Transactional(readOnly = true)
    public TokenPair login(LoginCommand command) {
        Member member = memberRepository.findByEmail(command.email())
                .orElseThrow(AuthErrors.INVALID_CREDENTIALS::toException);

        if (!passwordEncoder.matches(command.password(), member.getPassword())) {
            throw AuthErrors.INVALID_CREDENTIALS.toException();
        }

        if (member.getStatus() == MemberStatus.REJECTED) {
            throw new AuthException(AuthErrors.MEMBER_STATUS_REJECTED);
        }
        if (member.getStatus() == MemberStatus.WITHDRAWN || member.getStatus() == MemberStatus.WITHDRAW_APPROVAL) {
            throw new AuthException(AuthErrors.MEMBER_STATUS_WITHDRAWN);
        }

        return jwtTokenProvider.generateTokens(member);
    }

    @Transactional(readOnly = true)
    public TokenPair reissue(String refreshToken) {
        // 블랙리스트 확인
        if (refreshTokenBlacklistService.isBlacklisted(refreshToken)) {
            throw AuthErrors.INVALID_CREDENTIALS.toException();
        }

        var claims = jwtTokenProvider.parseClaims(refreshToken);
        String email = claims.getSubject();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(AuthErrors.INVALID_CREDENTIALS::toException);
        return jwtTokenProvider.reissueTokens(member, refreshToken);
    }

    /**
     * 로그아웃 처리
     * 리프레시 토큰을 블랙리스트에 추가하여 재사용을 방지합니다.
     *
     * @param refreshToken 블랙리스트에 추가할 리프레시 토큰
     */
    public void logout(String refreshToken) {
        refreshTokenBlacklistService.addToBlacklist(refreshToken);
    }

    private Role parseRole(String role) {
        try {
            Role parsed = Role.valueOf(role.toUpperCase());
            if (parsed == Role.ADMIN) {
                throw new InvalidParamException(AuthInvalidParamErrors.INVALID_ROLE);
            }
            return parsed;
        } catch (IllegalArgumentException e) {
            throw new InvalidParamException(AuthInvalidParamErrors.INVALID_ROLE);
        }
    }

    private Gender parseGender(String gender) {
        try {
            return Gender.valueOf(gender.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidParamException(AuthInvalidParamErrors.INVALID_GENDER);
        }
    }

    public record SignupCommand(
            String email,
            String password,
            String name,
            String nickname,
            LocalDate birthDate,
            String gender,
            String phoneNumber,
            String role,
            String addressRoad,
            Double latitude,
            Double longitude,
            String districtCode
    ) { }

    public record LoginCommand(
            String email,
            String password
    ) { }
}

