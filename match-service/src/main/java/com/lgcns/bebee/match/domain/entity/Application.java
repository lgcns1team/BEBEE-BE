package com.lgcns.bebee.match.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Application extends BaseTimeEntity {
    @Id
    @Tsid
    private Long applicationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
//    private HelpRequestPost post;
    private Long post;

    @Column(nullable = false)
    private Long applicantId;

    @Column(nullable = false)
    private Boolean isVolunteer;
}
