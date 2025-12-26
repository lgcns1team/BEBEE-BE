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

    private String postId;
    private String helperId;
    private String disabledId;
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
                Long.parseLong(postId),
                Long.parseLong(helperId),
                Long.parseLong(disabledId),
                type,
                isVolunteer,
                unitHoney,
                totalHoney,
                region,
                helpCategoryIds
        );
    }
}
