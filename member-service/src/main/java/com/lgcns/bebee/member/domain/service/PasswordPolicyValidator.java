package com.lgcns.bebee.member.domain.service;

import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.member.core.exception.MemberInvalidParamErrors;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class PasswordPolicyValidator {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%&])[A-Za-z\\d!@#$%&]{8,19}$");

    public void validate(String rawPassword) {
        if (rawPassword == null || !PASSWORD_PATTERN.matcher(rawPassword).matches()) {
            throw new InvalidParamException(MemberInvalidParamErrors.PASSWORD_NOT_NULL);
        }
    }
}

