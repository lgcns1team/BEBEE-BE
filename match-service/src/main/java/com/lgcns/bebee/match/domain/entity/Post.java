package com.lgcns.bebee.match.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import com.lgcns.bebee.match.domain.entity.vo.PostStatus;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {
    @Id
    @Tsid @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false, length = 50)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EngagementType type;

    @Column(nullable = false)
    private int unitHoney;

    @Column(nullable = false)
    private int totalHoney;

    @Column(nullable = false, length = 30)
    private String region;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status;

    @Column(nullable = false, length = 10)
    private String legalDongCode;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(nullable = true)
    private int applicantCount;

    @Column(nullable = true, length = 1000)
    private String content;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostHelpCategory> helpCategories = new ArrayList<>();

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "post",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> images = new ArrayList<>();

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private PostPeriod period;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostSchedule> schedules = new ArrayList<>();

    public static Post create(
            Long memberId,
            EngagementType type,
            List<PostImage> postImages,
            String title,
            List<PostHelpCategory> helpCategories,
            String content,
            PostPeriod period,
            List<PostSchedule> schedules,
            Integer unitHoney,
            Integer totalHoney,
            String region,
            String legalDongCode,
            BigDecimal latitude,
            BigDecimal longitude
    ) {
        Post post = new Post();
        post.memberId = memberId;
        post.type = type;
        post.images = postImages;
        post.title = title;
        post.helpCategories = helpCategories;
        post.content = content;
        post.unitHoney = unitHoney;
        post.totalHoney = totalHoney;
        post.region = region;
        post.legalDongCode = legalDongCode;
        post.latitude = latitude;
        post.longitude = longitude;

        post.period =  period;
        post.schedules = schedules;

        post.status = PostStatus.NON_MATCHED;
        post.applicantCount = 0;

        postImages.forEach(image -> image.assignToPost(post));
        helpCategories.forEach(helpCategory -> helpCategory.assignToPost(post));
        period.assignToPost(post);
        schedules.forEach(schedule -> schedule.assignToPost(post));

        return post;
    }
}
