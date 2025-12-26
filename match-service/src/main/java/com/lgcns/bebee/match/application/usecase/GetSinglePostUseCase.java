package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.match.domain.entity.Post;
import com.lgcns.bebee.match.domain.entity.PostImage;
import com.lgcns.bebee.match.domain.entity.PostSchedule;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.entity.vo.HelpCategoryType;
import com.lgcns.bebee.match.domain.service.MemberManager;
import com.lgcns.bebee.match.domain.service.PostManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetSinglePostUseCase implements UseCase<GetSinglePostUseCase.Param, GetSinglePostUseCase.Result> {
    private final PostManager postManager;
    private final MemberManager memberManager;

    @Transactional(readOnly = true)
    @Override
    public Result execute(Param params) {
        Post post = postManager.findSinglePost(params.postId);
        MemberSync member = memberManager.findExistingMember(post.getMemberId());
        return Result.from(post, member);
    }

    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long memberId;
        private final Long postId;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Result{
        private final String memberNickname;
        private final String memberLegalDongCode;
        private final String memberProfileImageUrl;

        private final List<String> helpCategories;

        private final String engagementType;
        private final String title;
        private final Integer unitHoney;
        private final Integer totalHoney;

        private final LocalDate date;
        private final LocalDate startDate;
        private final LocalDate endDate;

        private final List<ScheduleDTO> schedules;
        private final String postLegalDongCode;

        private final String content;
        private final Integer applicantCount;
        private final List<String> postImageUrls;

        public static Result from(Post post, MemberSync member) {
            List<String> helpCategoryNames = post.getHelpCategories().stream()
                    .map(postHelpCategory -> postHelpCategory.getId().getHelpCategoryId())
                    .map(HelpCategoryType::getNameById)
                    .toList();

            List<ScheduleDTO> scheduleDTOs = post.getSchedules().stream()
                    .sorted(Comparator.comparing(PostSchedule::getDayOfWeek))
                    .map(schedule -> new ScheduleDTO(
                            schedule.getDayOfWeek().name(),
                            schedule.getStartTime(),
                            schedule.getEndTime()
                    ))
                    .toList();

            List<String> imageUrls = post.getImages().stream()
                    .sorted((i1, i2) -> Integer.compare(i1.getSequence(), i2.getSequence()))
                    .map(PostImage::getImageUrl)
                    .toList();

            LocalDate date = null;
            LocalDate startDate = null;
            LocalDate endDate = null;

            if (post.getPeriod() != null) {
                switch (post.getType()) {
                    case DAY:
                        date = post.getPeriod().getStartDate();
                        break;
                    case TERM:
                        startDate = post.getPeriod().getStartDate();
                        endDate = post.getPeriod().getEndDate();
                        break;
                }
            }

            return new Result(
                    member.getNickname(),
                    member.getLegalDongCode(),
                    member.getProfileImageUrl(),
                    helpCategoryNames,
                    post.getType().name(),
                    post.getTitle(),
                    post.getUnitHoney(),
                    post.getTotalHoney(),
                    date,
                    startDate,
                    endDate,
                    scheduleDTOs,
                    post.getLegalDongCode(),
                    post.getContent(),
                    post.getApplicantCount(),
                    imageUrls
            );
        }

        @Getter
        @RequiredArgsConstructor
        public static class ScheduleDTO{
            private final String dayOfWeek;
            private final LocalTime startTime;
            private final LocalTime endTime;
        }
    }
}
