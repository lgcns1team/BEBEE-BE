package com.lgcns.bebee.member.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import com.lgcns.bebee.member.domain.entity.vo.CareerType;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "helper_career")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HelperCareer extends BaseTimeEntity {

    @Id
    @Tsid
    private Long helpCareerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CareerType type;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(length = 30)
    private String organization;

    private Integer period;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
}

