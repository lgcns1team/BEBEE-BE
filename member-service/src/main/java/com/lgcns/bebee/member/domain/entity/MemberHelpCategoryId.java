package com.lgcns.bebee.member.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class MemberHelpCategoryId implements Serializable {

    private Long helpCategoryId;
    private Long memberId;

    public MemberHelpCategoryId(Long helpCategoryId, Long memberId) {
        this.helpCategoryId = helpCategoryId;
        this.memberId = memberId;
    }
}
