package com.lgcns.bebee.match.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import com.lgcns.bebee.match.domain.entity.vo.PostStatus;
import com.lgcns.bebee.match.domain.entity.vo.PostType;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HelpRequestPost extends BaseTimeEntity {

    @Id
    @Tsid
    private Long postId;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false, length = 50)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostType type;

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
    private String legaldongCode;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(nullable = true)
    private int applicantCount;

    @Column(nullable = true, length = 1000)
    private String content;


}
