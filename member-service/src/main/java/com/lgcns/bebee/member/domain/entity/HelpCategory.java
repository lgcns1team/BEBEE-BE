package com.lgcns.bebee.member.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "help_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HelpCategory extends BaseTimeEntity {

    @Id
    @Tsid
    private Long helpCategoryId;

    @Column(nullable = false, length = 30, unique = true)
    private String name;
}

