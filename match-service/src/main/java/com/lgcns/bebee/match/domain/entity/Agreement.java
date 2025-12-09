package com.lgcns.bebee.match.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import com.lgcns.bebee.match.domain.entity.vo.AgreementStatus;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Agreement extends BaseTimeEntity {
    @Id
    @Tsid
    private Long agreementId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false, unique = true)
    private Match match;

    @Column(nullable = false)
    private Integer unitHoney;

    @Column(nullable = false)
    private Integer totalHoney;

    @Column(nullable = false, length = 50)
    private String region;

    @Enumerated(EnumType.STRING)
    private EngagementType type;

    @Column(nullable = false)
    private LocalDateTime confirmationDate;

    @Column
    private Boolean isDayComplete = Boolean.FALSE;

    @Column
    private Boolean isTermComplete = Boolean.FALSE;

    @Column(nullable = false)
    private AgreementStatus status = AgreementStatus.BEFORE;

    @OneToMany(mappedBy = "agreement")
    private List<AgreementHelpCategory> helpCategories = new ArrayList<>();
}
