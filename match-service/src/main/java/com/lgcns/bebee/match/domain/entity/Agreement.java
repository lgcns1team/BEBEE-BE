package com.lgcns.bebee.match.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import com.lgcns.bebee.match.domain.entity.vo.AgreementStatus;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Agreement extends BaseTimeEntity {
    @Id
    @Tsid
    @Column(name = "agreement_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private Long matchId;

    @Column(nullable = false)
    private Integer unitHoney;

    @Column(nullable = false)
    private Integer totalHoney;

    @Column(nullable = false, length = 50)
    private String region;

    @Enumerated(EnumType.STRING)
    private EngagementType type;

    @Column(nullable = false)
    private LocalDate confirmationDate;

    @Column
    private Boolean isDayComplete = Boolean.FALSE;

    @Column
    private Boolean isTermComplete = Boolean.FALSE;

    @Column(nullable = false)
    private AgreementStatus status = AgreementStatus.BEFORE;

    @OneToMany(mappedBy = "agreement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AgreementHelpCategory> helpCategories = new ArrayList<>();

    @Column
    private Boolean isVolunteer;

    public void addHelpCategory(AgreementHelpCategory category) {
        this.helpCategories.add(category);
        category.setAgreement(this);
    }

    public static Agreement create(
            Long matchId,
            EngagementType type,
            Boolean isVolunteer,
            Integer unitHoney,
            Integer totalHoney,
            String region
    ) {
        Agreement agreement = new Agreement();
        agreement.matchId = matchId;
        agreement.type = type;
        agreement.isVolunteer = isVolunteer;
        agreement.unitHoney = unitHoney;
        agreement.totalHoney = totalHoney;
        agreement.region = region;
        agreement.confirmationDate = LocalDate.now();
        agreement.status = AgreementStatus.BEFORE;
        agreement.isDayComplete = Boolean.FALSE;
        agreement.isTermComplete = Boolean.FALSE;
        return agreement;
    }
}
