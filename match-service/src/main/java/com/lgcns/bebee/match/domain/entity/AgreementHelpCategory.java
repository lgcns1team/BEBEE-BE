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

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("agreementId")
    @JoinColumn(name = "agreement_id")
    private Agreement agreement;

    @Column(name = "help_category_id", insertable = false, updatable = false)
    private Long helpCategoryId;

    public static AgreementHelpCategory create(Long helpCategoryId) {
        AgreementHelpCategory category = new AgreementHelpCategory();
        category.id = new AgreementHelpCategoryId(null, helpCategoryId);
        category.helpCategoryId = helpCategoryId;
        return category;
    }

    public void setAgreement(Agreement agreement) {
        this.agreement = agreement;
        if (this.id != null && agreement != null) {
            this.id.setAgreementId(agreement.getId());
        }
    }
}
