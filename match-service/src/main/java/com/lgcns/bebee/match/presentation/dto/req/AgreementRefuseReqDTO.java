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

    private Long helperId;
    private Long agreementId;

    public RefuseAgreementUseCase.Param toParam(Long agreementId) {
        return new RefuseAgreementUseCase.Param(
                helperId,
                agreementId
        );
    }
}
