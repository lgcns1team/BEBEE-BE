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

import static com.lgcns.bebee.match.exception.MatchErrors.ALREADY_CONFIRMED_AGREEMENT;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Agreement extends BaseTimeEntity {
    @Id
    @Tsid
    @Column(name = "agreement_id")
    private Long id;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Long disabledId;

    @Column(nullable = false)
    private Long helperId;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AgreementStatus status = AgreementStatus.BEFORE;

    @OneToMany(mappedBy = "agreement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AgreementHelpCategory> helpCategories= new ArrayList<>();

    @OneToOne(mappedBy = "agreement", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AgreementEngagementTimeDay day;

    @OneToOne(mappedBy = "agreement", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AgreementEngagementTimeTerm term;

    @Column
    private Boolean isVolunteer;

    public static Agreement create(
            Long postId,
            Long disabledId,
            Long helperId,
            EngagementType type,
            Boolean isVolunteer,
            Integer unitHoney,
            Integer totalHoney,
            String region,
            List<Long> helpCategoryIds
    ) {
        if (isVolunteer) {
            unitHoney = 0;
            totalHoney = 0;
        }

        Agreement agreement = new Agreement();
        agreement.postId = postId;
        agreement.disabledId = disabledId;
        agreement.helperId = helperId;
        agreement.type = type;
        agreement.isVolunteer = isVolunteer;
        agreement.unitHoney = unitHoney;
        agreement.totalHoney = totalHoney;
        agreement.region = region;
        agreement.confirmationDate = LocalDate.now();
        agreement.status = AgreementStatus.BEFORE;
        agreement.isDayComplete = Boolean.FALSE;
        agreement.isTermComplete = Boolean.FALSE;

        helpCategoryIds.forEach(helpCategoryId -> {
            String categoryName = com.lgcns.bebee.match.domain.entity.vo.HelpCategoryType.getNameById(helpCategoryId);
            AgreementHelpCategory agreementHelpCategory = AgreementHelpCategory.create(agreement, helpCategoryId, categoryName);
            agreement.helpCategories.add(agreementHelpCategory);
        });

        return agreement;
    }

    public void refuse() {
        if (this.status == AgreementStatus.CONFIRMED) {
            throw ALREADY_CONFIRMED_AGREEMENT.toException();
        }
        this.status = AgreementStatus.REFUSED;
    }

    public void confirm() {
        if (this.status == AgreementStatus.CONFIRMED) {
            throw ALREADY_CONFIRMED_AGREEMENT.toException();
        }
        this.status = AgreementStatus.CONFIRMED;
    }
}
