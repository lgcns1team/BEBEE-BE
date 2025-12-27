package com.lgcns.bebee.match.domain.entity;

import com.lgcns.bebee.common.data.domain.BaseTimeEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "post_period")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostPeriod extends BaseTimeEntity {
    @Id
    @Tsid @Column(name = "post_period_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, unique = true)
    private Post post;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    public static PostPeriod create(LocalDate startDate, LocalDate endDate) {
        PostPeriod period = new PostPeriod();
        period.startDate = startDate;
        period.endDate = endDate;
        return period;
    }

    protected void assignToPost(Post post) {
        this.post = post;
    }
}
