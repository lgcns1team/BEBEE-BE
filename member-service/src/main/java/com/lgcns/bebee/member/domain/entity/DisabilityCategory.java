package com.lgcns.bebee.member.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "disability_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DisabilityCategory extends BaseTimeEntity {

    @Id
    @Tsid
    private Long disabilityCategoryId;

    @Column(nullable = false, length = 10, unique = true)
    private String type;
}

