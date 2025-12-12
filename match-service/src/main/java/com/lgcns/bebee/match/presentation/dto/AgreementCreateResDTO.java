package com.lgcns.bebee.match.presentation.dto;

import com.lgcns.bebee.match.application.usecase.CreateAgreementUseCase;
import com.lgcns.bebee.match.domain.entity.Agreement;
import com.lgcns.bebee.match.domain.entity.AgreementHelpCategory;
import com.lgcns.bebee.match.domain.entity.vo.AgreementStatus;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AgreementCreateResDTO {

    private Long agreementId;
    private Long matchId;
    private AgreementStatus status;
    private LocalDate confirmationDate;
    private EngagementType type;
    private Boolean isVolunteer;
    private List<AgreementHelpCategory> helpCategories;
    private Integer unitHoney;
    private Integer totalHoney;
    private String region;
    private Boolean isDayComplete;
    private Boolean isTermComplete;

    /**
     * UseCase Result -> Response
     */
    public static AgreementCreateResDTO from(CreateAgreementUseCase.Result result) {
        CreateAgreementUseCase.Result.AgreementInfo info = result.getAgreementInfo();

        return new AgreementCreateResDTO(
                info.getAgreementId(),
                info.getMatchId(),
                info.getStatus(),
                info.getConfirmationDate(),
                info.getType(),
                info.getIsVolunteer(),
                info.getHelpCategories(),
                info.getUnitHoney(),
                info.getTotalHoney(),
                info.getRegion(),
                info.getIsDayComplete(),
                info.getIsTermComplete()
        );
    }
}
