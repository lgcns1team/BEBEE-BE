package com.lgcns.bebee.match.domain.entity;

import com.lgcns.bebee.match.domain.entity.vo.AgreementHelpCategoryId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
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
        category.helpCategoryId = helpCategoryId;
        category.id = new AgreementHelpCategoryId(null, helpCategoryId);
        return category;
    }

    public void setAgreement(Agreement agreement) {
        this.agreement = agreement;
        if (this.id != null) {
            this.id = new AgreementHelpCategoryId(agreement.getId(), this.helpCategoryId);
        }
    }
}
