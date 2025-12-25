package com.lgcns.bebee.match.presentation.dto.res;

import com.lgcns.bebee.match.domain.entity.AgreementHelpCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AgreementHelpCategoryDTO {
    private Long helpCategoryId;
    private String categoryName;

    public static AgreementHelpCategoryDTO from(AgreementHelpCategory category) {
        return new AgreementHelpCategoryDTO(
                category.getId().getHelpCategoryId(),
                category.getCategoryName()
        );
    }
}