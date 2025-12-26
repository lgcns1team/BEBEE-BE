package com.lgcns.bebee.match.domain.service;

import com.lgcns.bebee.match.common.exception.MatchErrors;
import com.lgcns.bebee.match.domain.entity.*;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import com.lgcns.bebee.match.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostManager {
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public Post findSinglePost(Long postId) {
        return postRepository.findById(postId).orElseThrow(MatchErrors.POST_NOT_FOUND::toException);
    }

    @Transactional
    public Post createPost(
            Long memberId,
            String postType,
            List<String> postImages,
            String title,
            List<Long> helpCategoryIds,
            String content,
            LocalDate startDate,
            LocalDate endDate,
            LocalDate date,
            List<DayOfWeek> daysOfWeek,
            List<LocalTime> startTimes,
            List<LocalTime> endTimes,
            Integer unitHoney,
            Integer totalHoney,
            String region,
            String legalDongCode,
            BigDecimal latitude,
            BigDecimal longitude
    ){
        EngagementType type = EngagementType.from(postType);

        // PostImage 리스트 생성
        List<PostImage> images = new ArrayList<>();
        for (int i = 0; i < postImages.size(); i++) {
            PostImage image = PostImage.create(postImages.get(i), i);
            images.add(image);
        }

        List<PostHelpCategory> helpCategories = helpCategoryIds.stream()
                .map(PostHelpCategory::create)
                .toList();

        // PostPeriod 생성
        PostPeriod period;
        if(type == EngagementType.DAY) period = PostPeriod.create(date, date);
        else period = PostPeriod.create(startDate, endDate);

        // PostSchedule 리스트 생성
        List<PostSchedule> schedules = new java.util.ArrayList<>();
        for (int i = 0; i < daysOfWeek.size(); i++) {
            PostSchedule schedule = PostSchedule.create(
                    daysOfWeek.get(i),
                    startTimes.get(i),
                    endTimes.get(i)
            );
            schedules.add(schedule);
        }

        Post post = Post.create(
                memberId,
                type,
                images,
                title,
                helpCategories,
                content,
                period,
                schedules,
                unitHoney,
                totalHoney,
                region,
                legalDongCode,
                latitude,
                longitude
        );

        return postRepository.save(post);
    }
}
