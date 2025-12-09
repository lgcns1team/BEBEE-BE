package com.lgcns.bebee.match.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;

@Entity // 이거를 왜 사용하는지 고민하기
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActiveTimeDay extends BaseTimeEntity {

    @Id
    @Tsid
    @Column(name = "post_id", nullable = false)
    private Long postId;


    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "post_id", nullable = false)
    private HelpRequestPost post;

    @Column(nullable = false)
    private Date engagmentDate;

    @Column(nullable = false)
    private Time engagmentTime;
}
