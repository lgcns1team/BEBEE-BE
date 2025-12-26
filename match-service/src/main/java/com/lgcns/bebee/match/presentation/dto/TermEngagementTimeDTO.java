package com.lgcns.bebee.match.presentation.dto;

import com.lgcns.bebee.match.domain.entity.AgreementPeriod;
import com.lgcns.bebee.match.domain.entity.AgreementSchedule;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TermEngagementTimeDTO {
    private LocalDate startDate;
    private LocalDate endDate;
    private List<AgreementScheduleDTO> schedules;

    public static TermEngagementTimeDTO from(AgreementPeriod period, List<AgreementSchedule> schedules) {
        List<AgreementScheduleDTO> scheduleDTOs = schedules.stream()
                .map(AgreementScheduleDTO::from)
                .collect(Collectors.toList());

        return new TermEngagementTimeDTO(
                period.getStartDate(),
                period.getEndDate(),
                scheduleDTOs
        );
    }
}
