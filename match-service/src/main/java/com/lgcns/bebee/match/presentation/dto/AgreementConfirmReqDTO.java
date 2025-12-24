package com.lgcns.bebee.match.presentation.dto;

import com.lgcns.bebee.match.application.usecase.ConfirmAgreementUseCase;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AgreementConfirmReqDTO {

    private Long helperId;
    private Long disabledId;
    private Long postId;
    private String title;
    private Long chatRoomId;
    private Long agreementId;

    public ConfirmAgreementUseCase.Param toParam(Long agreementId) {
        return new ConfirmAgreementUseCase.Param(
                helperId,
                disabledId,
                postId,
                title,
                chatRoomId,
                agreementId
        );
    }
}
