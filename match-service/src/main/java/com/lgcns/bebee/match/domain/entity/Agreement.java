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

    @Column
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
    private List<AgreementHelpCategory> helpCategories= new ArrayList<>();

    @OneToOne(mappedBy = "agreement", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AgreementEngagementTimeDay day;

    @OneToOne(mappedBy = "agreement", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AgreementEngagementTimeTerm term;

    @Column
    private Boolean isVolunteer;

    public static Agreement create(
            Long matchId,
            EngagementType type,
            Boolean isVolunteer,
            Integer unitHoney,
            String region,
            List<Long> helpCategoryIds
    ) {
        Agreement agreement = new Agreement();
        agreement.matchId = matchId;
        agreement.type = type;
        agreement.isVolunteer = isVolunteer;
        agreement.unitHoney = unitHoney;
        agreement.region = region;
        agreement.confirmationDate = LocalDate.now();
        agreement.status = AgreementStatus.BEFORE;
        agreement.isDayComplete = Boolean.FALSE;
        agreement.isTermComplete = Boolean.FALSE;

        helpCategoryIds.forEach(helpCategoryId -> {
            AgreementHelpCategory agreementHelpCategory = AgreementHelpCategory.create(helpCategoryId);
            agreement.helpCategories.add(agreementHelpCategory);
            agreementHelpCategory.setAgreement(agreement);
        });

        return agreement;
    }

    public void updateTotalHoney(Integer totalHoney) {
        this.totalHoney = totalHoney;
    }

    /**
     * Agreement의 type에 따라 totalHoney를 계산합니다.
     * - DAY: unitHoney를 그대로 반환
     * - TERM: 시작일부터 종료일까지 해당 요일의 발생 횟수 × unitHoney
     */
    public Integer calculateTotalHoney() {
        if (type == EngagementType.DAY) {
            return unitHoney;
        }
        
        if (type == EngagementType.TERM) {
            if (term == null || term.getSchedules().isEmpty()) {
                throw new IllegalStateException("TERM 타입의 Agreement는 반드시 term과 schedules가 필요합니다.");
            }

            // schedules에서 설정된 요일들을 Set으로 추출
            java.util.Set<java.time.DayOfWeek> targetDaysOfWeek = term.getSchedules().stream()
                    .map(schedule -> schedule.getId().getDayOfWeek())
                    .collect(java.util.stream.Collectors.toSet());

            // 시작일부터 종료일까지 해당 요일이 몇 번 발생하는지 계산
            int occurrenceCount = calculateDayOccurrences(
                    term.getStartDate(),
                    term.getEndDate(),
                    targetDaysOfWeek
            );

            return unitHoney * occurrenceCount;
        }

        throw new IllegalStateException("알 수 없는 EngagementType: " + type);
    }

    /**
     * 시작일부터 종료일까지 특정 요일들이 몇 번 발생하는지 계산합니다.
     */
    private int calculateDayOccurrences(
            LocalDate startDate,
            LocalDate endDate,
            java.util.Set<java.time.DayOfWeek> targetDaysOfWeek
    ) {
        int count = 0;
        LocalDate current = startDate;

        while (!current.isAfter(endDate)) {
            if (targetDaysOfWeek.contains(current.getDayOfWeek())) {
                count++;
            }
            current = current.plusDays(1);
        }

        return count;
    }
}
