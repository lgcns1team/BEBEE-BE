package com.lgcns.bebee.match.domain.entity;

import com.lgcns.bebee.common.data.domain.BaseTimeEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostSchedule extends BaseTimeEntity {
    @Id
    @Tsid @Column(name = "post_schedule_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    public static PostSchedule create(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        PostSchedule schedule = new PostSchedule();
        schedule.dayOfWeek = dayOfWeek;
        schedule.startTime = startTime;
        schedule.endTime = endTime;
        return schedule;
    }

    protected void assignToPost(Post post) {
        this.post = post;
    }
}
