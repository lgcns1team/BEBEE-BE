package com.lgcns.bebee.match.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EngagementTimeTerm extends BaseTimeEntity {
    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(nullable = false)
    private Agreement agreement;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false, columnDefinition = "json")
    private String engagementTime;
}
