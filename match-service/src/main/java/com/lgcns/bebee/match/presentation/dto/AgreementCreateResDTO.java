package com.lgcns.bebee.match.presentation.dto;

import com.lgcns.bebee.match.application.usecase.CreateAgreementUseCase;
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
    private List<AgreementHelpCategoryDTO> helpCategories;
    private Integer unitHoney;
    private Integer totalHoney;
    private String region;
    private Boolean isDayComplete;
    private Boolean isTermComplete;

    /**
     * UseCase Result -> Response
     */
    public static AgreementCreateResDTO from(CreateAgreementUseCase.Result result) {

        return new AgreementCreateResDTO(
                result.getAgreementId(),
                result.getMatchId(),
                result.getStatus(),
                result.getConfirmationDate(),
                result.getType(),
                result.getIsVolunteer(),
                result.getHelpCategories(),
                result.getUnitHoney(),
                result.getTotalHoney(),
                result.getRegion(),
                result.getIsDayComplete(),
                result.getIsTermComplete()
        );
    }
}
