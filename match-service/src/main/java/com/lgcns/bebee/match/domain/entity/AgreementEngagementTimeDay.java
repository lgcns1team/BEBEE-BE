package com.lgcns.bebee.match.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AgreementEngagementTimeDay extends BaseTimeEntity {
    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "agreement_id", nullable = false)
    private Agreement agreement;

    @Column(nullable = false)
    private LocalDate engagementDate;

    @Column(nullable = false, columnDefinition = "json")
    private String engagementTime;
}
