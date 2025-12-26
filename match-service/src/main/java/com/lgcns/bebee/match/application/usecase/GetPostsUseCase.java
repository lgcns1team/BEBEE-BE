package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.match.domain.entity.Post;
import com.lgcns.bebee.match.domain.entity.PostSchedule;
import com.lgcns.bebee.match.domain.entity.sync.Gender;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import com.lgcns.bebee.match.domain.entity.vo.HelpCategoryType;
import com.lgcns.bebee.match.domain.entity.vo.PostStatus;
import com.lgcns.bebee.match.domain.repository.PostRepository;
import com.lgcns.bebee.match.domain.repository.dto.PostSearchCond;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetPostsUseCase implements UseCase<GetPostsUseCase.Param, GetPostsUseCase.Result> {
    private final PostRepository postRepository;

    @Override
    @Transactional(readOnly = true)
    public Result execute(Param params) {
        EngagementType engagementType = null;
        if (params.postType != null) engagementType = EngagementType.from(params.postType);

        Gender gender = null;
        if(params.gender != null) gender = Gender.from(params.gender);

        List<DayOfWeek> daysOfWeek = null;
        if(params.days != null && !params.days.isEmpty()) daysOfWeek = params.days.stream()
                .map(DayOfWeek::valueOf)
                .toList();

        // isMatched를 PostStatus로 변환
        PostStatus postStatus = null;
        if (params.isMatched != null) {
            postStatus = PostStatus.NON_MATCHED;
        }

        // lastPostId가 null이면 Long.MAX_VALUE로 설정 (최초 요청)
        Long cursorId = params.lastPostId != null ? params.lastPostId : Long.MAX_VALUE;

        PostSearchCond cond = new PostSearchCond(
                engagementType,
                params.legalDongCodes,
                params.helpCategoryIds,
                gender,
                params.minHoney,
                params.maxHoney,
                params.disabilityCategoryId,
                daysOfWeek,
                postStatus,
                cursorId,
                params.count + 1  // 다음 페이지 존재 여부 확인을 위해 +1
        );

        List<Post> posts = postRepository.findPosts(cond);

        // 다음 페이지 존재하는 지 여부 확인
        boolean hasNext = posts.size() > params.count;

        Long nextPostId = null;
        if(hasNext){
            nextPostId = posts.get(params.count).getId();
            posts = posts.subList(0, params.count);
        }

        return Result.from(hasNext, nextPostId, posts);
    }

    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long currentMemberId;
        private final String postType;
        private final Boolean isMatched;
        private final List<String> legalDongCodes;
        private final List<Long> helpCategoryIds;
        private final String gender;
        private final Integer minHoney;
        private final Integer maxHoney;
        private final Long disabilityCategoryId;
        private final List<String> days;
        private final Long lastPostId;
        private final Integer count;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Result {
        private final Boolean hasNext;
        private final Long nextPostId;
        private final List<PostDTO> posts;

        public static Result from(boolean hasNext, Long nextPostId, List<Post> posts) {
            List<PostDTO> postDTOs = posts.stream()
                    .map(PostDTO::from)
                    .collect(Collectors.toList());

            return new Result(hasNext, nextPostId, postDTOs);
        }

        @Getter
        @RequiredArgsConstructor
        public static class PostDTO {
            private final Long postId;
            private final String title;
            private final Integer unitHoney;
            private final Integer totalHoney;
            private final String legalDongName;
            private final List<String> helpCategories;
            private final String helpType;
            private final String imageUrl;
            private final LocalDate date;  // DAY 타입일 때만 값 있음
            private final List<String> dayOfWeeks;

            public static PostDTO from(Post post) {
                List<String> helpCategoryNames = post.getHelpCategories().stream()
                        .map(postHelpCategory -> postHelpCategory.getId().getHelpCategoryId())
                        .map(HelpCategoryType::getNameById)
                        .collect(Collectors.toList());

                String imageUrl = post.getImages().isEmpty()
                        ? null
                        : post.getImages().get(0).getImageUrl();

                LocalDate date = null;

                if (post.getType() == EngagementType.DAY && post.getPeriod() != null) {
                    date = post.getPeriod().getStartDate();
                }
                List<String> dayOfWeeks = post.getSchedules().stream()
                        .map(PostSchedule::getDayOfWeek)
                        .distinct()
                        .sorted()
                        .map(Enum::name)
                        .collect(Collectors.toList());

                return new PostDTO(
                        post.getId(),
                        post.getTitle(),
                        post.getUnitHoney(),
                        post.getTotalHoney(),
                        post.getRegion(),
                        helpCategoryNames,
                        post.getType().name(),
                        imageUrl,
                        date,
                        dayOfWeeks
                );
            }
        }
    }
}
