package com.lgcns.bebee.match.domain.entity.vo;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class AgreementHelpCategoryId {
    private Long agreementId;
    private Long helpCategoryId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AgreementHelpCategoryId that = (AgreementHelpCategoryId) o;
        return Objects.equals(agreementId, that.agreementId) &&
                Objects.equals(helpCategoryId, that.helpCategoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agreementId, helpCategoryId);
    }

    public AgreementHelpCategoryId() {}
}
