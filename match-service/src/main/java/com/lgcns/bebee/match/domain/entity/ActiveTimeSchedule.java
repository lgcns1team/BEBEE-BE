package com.lgcns.bebee.match.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import com.lgcns.bebee.match.domain.entity.vo.PostStatus;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.DayOfWeek;
import java.util.Date;
import java.util.EnumMap;

@Entity // 이거를 왜 사용하는지 고민하기
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActiveTimeSchedule extends BaseTimeEntity {

    @Id
    @Tsid
    @Column(name = "post_id", nullable = false)
    private Long postId;


    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "post_id", nullable = false)
    private ActiveTimeTerm post;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private DayOfWeek day;

    @Column(nullable = true)
    private Time startTime;

    @Column(nullable = true)
    private Time endTime;
}
