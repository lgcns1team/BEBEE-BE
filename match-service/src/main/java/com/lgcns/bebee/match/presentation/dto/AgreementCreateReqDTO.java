package com.lgcns.bebee.match.presentation.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.lgcns.bebee.match.application.usecase.CreateAgreementUseCase;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AgreementCreateReqDTO {

    private Long postId;
    private Long helperId;
    private Long disabledId;
    private EngagementType type;
    private Boolean isVolunteer;
    private List<Long> helpCategoryIds;
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

    /**
     * Request -> UseCase Param
     */
    public CreateAgreementUseCase.Param toParam() {
        DayEngagementTimeDTO dayTime = null;
        TermEngagementTimeDTO termTime = null;

        if (type == EngagementType.DAY) {
            dayTime = (DayEngagementTimeDTO) engagementTime;
        } else if (type == EngagementType.TERM) {
            termTime = (TermEngagementTimeDTO) engagementTime;
        }

        return new CreateAgreementUseCase.Param(
                postId,
                helperId,
                disabledId,
                type,
                isVolunteer,
                unitHoney,
                totalHoney,
                region,
                dayTime,
                termTime,
                helpCategoryIds
        );
    }
}
