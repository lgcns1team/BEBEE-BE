package com.lgcns.bebee.match.domain.entity.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AgreementEngagementTimeScheduleId implements Serializable {
    private Long termId;
    private DayOfWeek dayOfWeek;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgreementEngagementTimeScheduleId that = (AgreementEngagementTimeScheduleId) o;
        return Objects.equals(termId, that.termId) && dayOfWeek == that.dayOfWeek;
    }

    @Override
    public int hashCode() {
        return Objects.hash(termId, dayOfWeek);
    }
}
