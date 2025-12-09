package com.lgcns.bebee.match.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import com.lgcns.bebee.match.domain.entity.vo.AgreementStatus;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Agreement extends BaseTimeEntity {
    @Id
    @Tsid
    private Long agreement_id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, unique = true)
    private Long match_id;

    @Column(nullable = false)
    private Integer unit_honey;

    @Column(nullable = false)
    private Integer total_honey;

    @Column(nullable = false, length = 50)
    private String region;

    @Enumerated(EnumType.STRING)
    private EngagementType type;

    @Column(nullable = false)
    private LocalDateTime confirmation_date;

    @Column
    private Boolean is_day_complete = Boolean.FALSE;

    @Column
    private Boolean is_term_complete = Boolean.FALSE;

    @Column(nullable = false)
    private AgreementStatus status = AgreementStatus.BEFORE;
}
