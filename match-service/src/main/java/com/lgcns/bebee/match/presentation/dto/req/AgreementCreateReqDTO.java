package com.lgcns.bebee.match.presentation.dto.req;

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

    private String memberId;
    private String matchId;
    private EngagementType type;
    private Boolean isVolunteer;
    private List<Long> helpCategoryIds;
    private Integer unitHoney;
    private Integer totalHoney;
    private String region;

    /**
     * Request -> UseCase Param
     */
    public CreateAgreementUseCase.Param toParam() {
        return new CreateAgreementUseCase.Param(
                Long.parseLong(memberId),
                type,
                isVolunteer,
                unitHoney,
                totalHoney,
                region,
                helpCategoryIds
        );
    }
}
