package com.lgcns.bebee.member.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.member.core.exception.MemberInvalidParamErrors;
import com.lgcns.bebee.member.domain.entity.Member;
import com.lgcns.bebee.member.domain.repository.MemberRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.regex.Pattern;

import com.lgcns.bebee.member.domain.service.PasswordPolicyValidator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignUpUseCase implements UseCase<SignUpUseCase.Param, SignUpUseCase.Result> {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordPolicyValidator passwordPolicyValidator;

    @Override
    @Transactional
    public SignUpUseCase.Result execute(Param params) {
        params.validate();

        passwordPolicyValidator.validate(params.password);
        String encodedPassword = passwordEncoder.encode(params.password);

        Member newMember = Member.create(
                params.email,
                encodedPassword,
                params.name,
                params.nickname,
                params.birthDate,
                params.gender,
                params.phoneNumber,
                params.role,
                params.addressRoad,
                params.latitude,
                params.longitude,
                params.districtCode
        );

        Member savedMember = memberRepository.save(newMember);

        return new Result(savedMember.getId());
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final String email;
        private final String password;
        private final String name;
        private final String nickname;
        private final LocalDate birthDate;
        private final String gender;
        private final String phoneNumber;
        private final String role;
        private final String addressRoad;
        private final BigDecimal latitude;
        private final BigDecimal longitude;
        private final String districtCode;

        @Override
        public boolean validate() {
            validateEmail();
            validateNickname();
            validatePassword();
            return true;
        }

        private void validateEmail() {
            if (email == null || email.isBlank()) {
                throw MemberInvalidParamErrors.EMAIL_NOT_NULL.toException();
            }
            Pattern emailPattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
            if (!emailPattern.matcher(email).matches()) {
                throw MemberInvalidParamErrors.INVALID_EMAIL_FORMAT.toException();
            }
        }

        private void validateNickname() {
            if (nickname == null || nickname.isBlank()) {
                throw MemberInvalidParamErrors.NICKNAME_NOT_NULL.toException();
            }

        }

        private void validatePassword() {
            if (password == null || password.isBlank()) {
                throw MemberInvalidParamErrors.PASSWORD_NOT_NULL.toException();
            }
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class Result{
        private final Long memberId;
    }
}
