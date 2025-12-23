package com.lgcns.bebee.chat.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HelpCategorySync extends BaseTimeEntity {
    @Id
    @Column(name = "help_category_id")
    private Long id;

    private String name;
}
