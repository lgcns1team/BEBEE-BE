package com.lgcns.bebee.match.presentation.dto.res;

import com.lgcns.bebee.match.application.usecase.ConfirmAgreementUseCase;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AgreementConfirmResDTO {
    private String matchId;

    public static AgreementConfirmResDTO from(ConfirmAgreementUseCase.Result result) {
        return new AgreementConfirmResDTO(String.valueOf(result.getMatchId()));
    }
}
