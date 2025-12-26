package com.lgcns.bebee.match.domain.repository.dto;

import com.lgcns.bebee.match.domain.entity.sync.Gender;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import com.lgcns.bebee.match.domain.entity.vo.PostStatus;

import java.time.DayOfWeek;
import java.util.List;

public record PostSearchCond(
    EngagementType engagementType,
    List<String> legalDongCode,
    List<Long> helpCategoryIds,
    Gender gender,
    Integer minHoney,
    Integer maxHoney,
    Long disabilityCategoryId,
    List<DayOfWeek> dayOfWeeks,
    PostStatus postStatus,
    Long lastPostId,
    Integer count
) {
}
