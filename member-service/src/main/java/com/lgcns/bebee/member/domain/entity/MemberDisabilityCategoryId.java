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
public class MemberDisabilityCategoryId implements Serializable {

    private Long memberId;
    private Long disabilityCategoryId;

    public MemberDisabilityCategoryId(Long memberId, Long disabilityCategoryId) {
        this.memberId = memberId;
        this.disabilityCategoryId = disabilityCategoryId;
    }
}
