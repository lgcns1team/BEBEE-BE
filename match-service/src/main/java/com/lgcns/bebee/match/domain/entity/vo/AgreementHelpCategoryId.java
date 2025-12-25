package com.lgcns.bebee.match.domain.entity.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
public class AgreementHelpCategoryId implements Serializable {
    private Long agreementId;
    private Long helpCategoryId;

    public void setAgreementId(Long agreementId) {
        this.agreementId = agreementId;
    }

    public void setHelpCategoryId(Long helpCategoryId) {
        this.helpCategoryId = helpCategoryId;
    }

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

    public AgreementHelpCategoryId() {

    }

    public AgreementHelpCategoryId(Long agreementId, Long helpCategoryId) {
        this.agreementId = agreementId;
        this.helpCategoryId = helpCategoryId;
    }
}
