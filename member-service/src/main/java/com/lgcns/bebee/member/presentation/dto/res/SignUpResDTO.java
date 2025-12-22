package com.lgcns.bebee.member.presentation.dto.res;

import com.lgcns.bebee.member.application.usecase.SignUpUseCase;

public record SignUpResDTO(
        Long memberId
) {

    public static SignUpResDTO create(SignUpUseCase.Result result) {
        return new SignUpResDTO(result.getMemberId());
    }
}
