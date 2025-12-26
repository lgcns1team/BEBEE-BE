package com.lgcns.bebee.match.domain.repository;

import com.lgcns.bebee.match.domain.entity.Post;
import com.lgcns.bebee.match.domain.entity.sync.Gender;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import com.lgcns.bebee.match.domain.entity.vo.PostStatus;
import com.lgcns.bebee.match.domain.repository.dto.PostSearchCond;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.List;

import static com.lgcns.bebee.match.domain.entity.QPost.post;
import static com.lgcns.bebee.match.domain.entity.QPostHelpCategory.postHelpCategory;
import static com.lgcns.bebee.match.domain.entity.QPostPeriod.postPeriod;
import static com.lgcns.bebee.match.domain.entity.QPostSchedule.postSchedule;
import static com.lgcns.bebee.match.domain.entity.sync.QMemberSync.memberSync;

@Component
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Post> findPosts(PostSearchCond cond) {
        return queryFactory
                .selectFrom(post)
                .distinct()
                .leftJoin(post.period, postPeriod).fetchJoin()
                .leftJoin(post.helpCategories, postHelpCategory)
                .leftJoin(memberSync).on(memberSync.id.eq(post.memberId))
                .leftJoin(postSchedule).on(postSchedule.post.id.eq(post.id))
                .where(
                        eqEngagementType(cond.engagementType()),
                        inLegalDongCodes(cond.legalDongCode()),
                        inHelpCategoryIds(cond.helpCategoryIds()),
                        eqGender(cond.gender()),
                        betweenHoney(cond.minHoney(), cond.maxHoney()),
                        inDayOfWeeks(cond.dayOfWeeks()),
                        eqStatus(cond.postStatus()),
                        ltPostId(cond.lastPostId())
                )
                .orderBy(post.id.desc())
                .limit(cond.count())
                .fetch();
    }

    private BooleanExpression eqEngagementType(EngagementType type) {
        return type != null ? post.type.eq(type) : null;
    }

    private BooleanExpression inLegalDongCodes(List<String> legalDongCodes) {
        return legalDongCodes != null && !legalDongCodes.isEmpty()
                ? post.legalDongCode.in(legalDongCodes)
                : null;
    }

    private BooleanExpression inHelpCategoryIds(List<Long> helpCategoryIds) {
        return helpCategoryIds != null && !helpCategoryIds.isEmpty()
                ? postHelpCategory.id.helpCategoryId.in(helpCategoryIds)
                : null;
    }

    private BooleanExpression eqGender(Gender gender) {
        return gender != null ? memberSync.gender.eq(gender) : null;
    }

    private BooleanExpression betweenHoney(Integer minHoney, Integer maxHoney) {
        if (minHoney != null && maxHoney != null) {
            return post.totalHoney.between(minHoney, maxHoney);
        } else if (minHoney != null) {
            return post.totalHoney.goe(minHoney);
        } else if (maxHoney != null) {
            return post.totalHoney.loe(maxHoney);
        }
        return null;
    }

    private BooleanExpression inDayOfWeeks(List<DayOfWeek> dayOfWeeks) {
        return dayOfWeeks != null && !dayOfWeeks.isEmpty()
                ? postSchedule.dayOfWeek.in(dayOfWeeks)
                : null;
    }

    private BooleanExpression eqStatus(PostStatus status) {
        return status != null ? post.status.eq(status) : null;
    }

    private BooleanExpression ltPostId(Long lastPostId) {
        return lastPostId != null ? post.id.lt(lastPostId) : null;
    }
}