package com.lgcns.bebee.member.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.member.common.exception.AuthInvalidParamErrors;
import com.lgcns.bebee.member.domain.service.AuthService;
import com.lgcns.bebee.member.infrastructure.security.JwtTokenProvider.TokenPair;
import java.time.LocalDate;
import java.util.Locale;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 회원가입 유스케이스
 */
@Service
@RequiredArgsConstructor
public class SignUpUseCase implements UseCase<SignUpUseCase.Param, TokenPair> {

    private final AuthService authService;

    @Override
    @Transactional
    public TokenPair execute(Param param) {
        param.validate();
        return authService.signup(toCommand(param));
    }

    private AuthService.SignupCommand toCommand(Param param) {
        return new AuthService.SignupCommand(
                param.getEmail(),
                param.getPassword(),
                param.getName(),
                param.getNickname(),
                param.getBirthDate(),
                param.getGender(),
                param.getPhoneNumber(),
                param.getRole(),
                param.getAddressRoad(),
                param.getLatitude(),
                param.getLongitude(),
                param.getDistrictCode()
        );
    }

    /**
     * 회원가입 파라미터
     */
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
        private final Double latitude;
        private final Double longitude;
        private final String districtCode;

        @Override
        public boolean validate() {
            validateEmail();
            validateNickname();
            validatePassword();
            validateGender();
            return true;
        }

        private void validateEmail() {
            if (email == null || email.isBlank()) {
                throw new InvalidParamException(AuthInvalidParamErrors.INVALID_EMAIL_FORMAT);
            }
            int length = email.length();
            if (length < 12 || length > 30) {
                throw new InvalidParamException(AuthInvalidParamErrors.INVALID_EMAIL_FORMAT);
            }
            Pattern emailPattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
            if (!emailPattern.matcher(email).matches()) {
                throw new InvalidParamException(AuthInvalidParamErrors.INVALID_EMAIL_FORMAT);
            }
        }

        private void validateNickname() {
            if (nickname == null || nickname.isBlank()) {
                throw new InvalidParamException(AuthInvalidParamErrors.INVALID_NICKNAME_FORMAT);
            }
            Pattern nicknamePattern = Pattern.compile("^[가-힣A-Za-z0-9]{2,10}$");
            if (!nicknamePattern.matcher(nickname).matches()) {
                throw new InvalidParamException(AuthInvalidParamErrors.INVALID_NICKNAME_FORMAT);
            }
        }

        private void validatePassword() {
            if (password == null || password.isBlank()) {
                throw new InvalidParamException(AuthInvalidParamErrors.INVALID_PASSWORD_FORMAT);
            }
            int length = password.length();
            if (length < 8 || length > 20) {
                throw new InvalidParamException(AuthInvalidParamErrors.INVALID_PASSWORD_FORMAT);
            }

            boolean hasLetter = Pattern.compile("[A-Za-z]").matcher(password).find();
            boolean hasDigit = Pattern.compile("[0-9]").matcher(password).find();
            boolean hasSpecial = Pattern.compile("[^A-Za-z0-9]").matcher(password).find();

            int categoryCount = 0;
            if (hasLetter) {
                categoryCount++;
            }
            if (hasDigit) {
                categoryCount++;
            }
            if (hasSpecial) {
                categoryCount++;
            }

            if (categoryCount < 2) {
                throw new InvalidParamException(AuthInvalidParamErrors.INVALID_PASSWORD_FORMAT);
            }

            if (email != null && !email.isBlank() && password.contains(email)) {
                throw new InvalidParamException(AuthInvalidParamErrors.INVALID_PASSWORD_FORMAT);
            }
        }

        private void validateGender() {
            if (gender == null || gender.isBlank()) {
                throw new InvalidParamException(AuthInvalidParamErrors.INVALID_GENDER);
            }
            String normalized = gender.toUpperCase(Locale.ROOT);
            if (!normalized.equals("MALE") && !normalized.equals("FEMALE") && !normalized.equals("NONE")) {
                throw new InvalidParamException(AuthInvalidParamErrors.INVALID_GENDER);
            }
            if (normalized.equals("NONE")) {
                throw new InvalidParamException(AuthInvalidParamErrors.INVALID_GENDER);
            }
        }
    }
}
