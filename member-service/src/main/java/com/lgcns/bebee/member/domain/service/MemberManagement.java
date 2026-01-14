package com.lgcns.bebee.member.domain.service;

import com.lgcns.bebee.member.core.exception.MemberErrors;
import com.lgcns.bebee.member.core.exception.MemberInvalidParamErrors;
import com.lgcns.bebee.member.domain.entity.Member;
import com.lgcns.bebee.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MemberManagement {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordPolicyValidator passwordPolicyValidator;

    public Member getExistingMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(MemberErrors.MEMBER_NOT_FOUND::toException);
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(MemberErrors.MEMBER_NOT_FOUND_BY_EMAIL::toException);
    }

    /**
     * 비밀번호를 검증합니다.
     * 컨벤션에 따라 PasswordEncoder.matches()를 사용합니다.
     */
    public void checkPassword(Member member, String rawPassword) {
        boolean isMatch = passwordEncoder.matches(rawPassword, member.getPassword());

        if (!isMatch) {
            throw MemberErrors.INVALID_PASSWORD.toException();
        }
    }

    public void checkEmailDuplicated(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw MemberInvalidParamErrors.EMAIL_DUPLICATED.toException();
        }
    }

    public void checkNicknameDuplicated(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw MemberInvalidParamErrors.NICKNAME_DUPLICATED.toException();
        }
    }

    public Member createMember(
            String email,
            String rawPassword,
            String name,
            String nickname,
            LocalDate birthDate,
            String gender,
            String phoneNumber,
            String role,
            String addressRoad,
            BigDecimal latitude,
            BigDecimal longitude,
            String districtCode,
            String introduction) {
        passwordPolicyValidator.validate(rawPassword);
        String encodedPassword = passwordEncoder.encode(rawPassword);

        return Member.create(
                email,
                encodedPassword,
                name,
                nickname,
                birthDate,
                gender,
                phoneNumber,
                role,
                addressRoad,
                latitude,
                longitude,
                districtCode,
                introduction);
    }
}
