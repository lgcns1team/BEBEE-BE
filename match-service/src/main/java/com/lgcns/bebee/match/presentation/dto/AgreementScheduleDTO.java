package com.lgcns.bebee.match.presentation.dto;

import com.lgcns.bebee.match.domain.entity.AgreementSchedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AgreementScheduleDTO {
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public static AgreementScheduleDTO from(AgreementSchedule schedule) {
        return new AgreementScheduleDTO(
                schedule.getDayOfWeek(),
                schedule.getStartTime(),
                schedule.getEndTime()
        );
    }
}