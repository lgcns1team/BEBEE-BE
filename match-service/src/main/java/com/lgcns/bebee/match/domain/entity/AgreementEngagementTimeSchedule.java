package com.lgcns.bebee.match.domain.entity;

import com.lgcns.bebee.match.domain.entity.vo.AgreementEngagementTimeScheduleId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AgreementEngagementTimeSchedule {
    @EmbeddedId
    private AgreementEngagementTimeScheduleId id;

    @MapsId("termId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_id", nullable = false)
    private AgreementEngagementTimeTerm term;

    @Column
    private LocalTime startTime;

    @Column
    private LocalTime endTime;
}
