package com.lgcns.bebee.match.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lgcns.bebee.match.domain.entity.vo.AgreementHelpCategoryId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AgreementHelpCategory {
    @EmbeddedId
    private AgreementHelpCategoryId id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("agreementId")
    @JoinColumn(name = "agreement_id")
    private Agreement agreement;

    public static AgreementHelpCategory create(Agreement agreement, Long helpCategoryId) {
        AgreementHelpCategory category = new AgreementHelpCategory();
        category.agreement = agreement;
        category.id = new AgreementHelpCategoryId(null, helpCategoryId);
        return category;
    }
}
