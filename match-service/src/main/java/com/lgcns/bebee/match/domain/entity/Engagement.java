package com.lgcns.bebee.match.domain.entity;

import com.lgcns.bebee.common.data.domain.BaseTimeEntity;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Engagement extends BaseTimeEntity {
    @Id
    @Tsid
    private Long engagementId;

    @Column(nullable = false)
    private Long agreementId;

    @Enumerated(EnumType.STRING)
    private EngagementType type;

    @Column
    private Boolean isDisabledCheck = false;

    @Column
    private Boolean isHelperCheck = false;

    @Column
    private Long count = 0L;
}
