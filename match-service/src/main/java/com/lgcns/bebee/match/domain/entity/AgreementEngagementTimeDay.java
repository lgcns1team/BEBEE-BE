package com.lgcns.bebee.match.domain.entity;

import com.lgcns.bebee.common.data.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AgreementEngagementTimeDay extends BaseTimeEntity {
    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "agreement_id", nullable = false)
    private Agreement agreement;

    @Column(nullable = false)
    private LocalDate engagementDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;
}
