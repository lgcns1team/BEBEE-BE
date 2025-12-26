package com.lgcns.bebee.match.presentation.dto;

import com.lgcns.bebee.match.domain.entity.AgreementPeriod;
import com.lgcns.bebee.match.domain.entity.AgreementSchedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DayEngagementTimeDTO {
    private LocalDate engagementDate;
    private LocalTime startTime;
    private LocalTime endTime;

    public static DayEngagementTimeDTO from(AgreementPeriod period, AgreementSchedule schedule) {
        return new DayEngagementTimeDTO(
                period.getStartDate(),  // DAY 타입이므로 startDate == endDate
                schedule.getStartTime(),
                schedule.getEndTime()
        );
    }
}
