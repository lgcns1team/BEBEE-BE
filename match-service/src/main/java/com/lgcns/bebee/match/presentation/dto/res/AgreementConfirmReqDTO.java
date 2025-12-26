package com.lgcns.bebee.match.presentation.dto.res;

import com.lgcns.bebee.match.application.usecase.ConfirmAgreementUseCase;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AgreementConfirmReqDTO {

    private String helperId;
    private String disabledId;
    private String postId;
    private String title;
    private String chatRoomId;
    private String agreementId;

    public ConfirmAgreementUseCase.Param toParam(String agreementId) {
        return new ConfirmAgreementUseCase.Param(
                Long.parseLong(helperId),
                Long.parseLong(disabledId),
                Long.parseLong(postId),
                title,
                Long.parseLong(chatRoomId),
                Long.parseLong(agreementId)
        );
    }
}
