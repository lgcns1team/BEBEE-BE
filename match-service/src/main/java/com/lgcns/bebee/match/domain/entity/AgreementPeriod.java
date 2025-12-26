package com.lgcns.bebee.match.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AgreementPeriod extends BaseTimeEntity {
    @Id
    @Tsid
    @Column(name = "agreement_period_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agreement_id", nullable = false, unique = true)
    private Agreement agreement;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    public static AgreementPeriod create(
            Agreement agreement,
            LocalDate startDate,
            LocalDate endDate
    ) {
        AgreementPeriod period = new AgreementPeriod();
        period.agreement = agreement;
        period.startDate = startDate;
        period.endDate = endDate;
        return period;
    }
}