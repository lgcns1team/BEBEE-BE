package com.lgcns.bebee.match.presentation.dto.req;

import com.lgcns.bebee.match.application.usecase.RefuseAgreementUseCase;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AgreementRefuseReqDTO {

    private String helperId;
    private String agreementId;

    public RefuseAgreementUseCase.Param toParam(String agreementId) {
        return new RefuseAgreementUseCase.Param(
                Long.parseLong(helperId),
                Long.parseLong(agreementId)
        );
    }
}
