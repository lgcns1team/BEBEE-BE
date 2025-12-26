package com.lgcns.bebee.match.presentation.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
    private AgreementStatus status;
    private LocalDate confirmationDate;
    private EngagementType type;
    private Boolean isVolunteer;
    private List<AgreementHelpCategoryDTO> helpCategories;
    private Integer unitHoney;
    private Integer totalHoney;
    private String region;

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
            property = "type"
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(value = DayEngagementTimeDTO.class, name = "DAY"),
            @JsonSubTypes.Type(value = TermEngagementTimeDTO.class, name = "TERM")
    })
    private Object engagementTime;

    private Boolean isDayComplete;
    private Boolean isTermComplete;

    /**
     * UseCase Result -> Response
     */
    public static AgreementCreateResDTO from(CreateAgreementUseCase.Result result) {

        return new AgreementCreateResDTO(
                result.getAgreementId(),
                result.getStatus(),
                result.getConfirmationDate(),
                result.getType(),
                result.getIsVolunteer(),
                result.getHelpCategories(),
                result.getUnitHoney(),
                result.getTotalHoney(),
                result.getRegion(),
                result.getEngagementTime(),
                result.getIsDayComplete(),
                result.getIsTermComplete()
        );
    }
}
