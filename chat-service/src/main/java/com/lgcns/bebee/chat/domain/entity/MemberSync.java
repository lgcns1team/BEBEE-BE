package com.lgcns.bebee.chat.domain.entity;

import com.lgcns.bebee.common.data.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSync extends BaseTimeEntity {

    @Id
    @Column(name = "member_id")
    private Long id;

    private String nickname;

    private String profileImageUrl;

    private BigDecimal sweetness;
}
