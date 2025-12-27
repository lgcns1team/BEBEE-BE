package com.lgcns.bebee.payment.domain.entity;

import com.lgcns.bebee.common.data.domain.BaseTimeEntity;
import com.lgcns.bebee.payment.domain.entity.vo.HoneyHistoryType;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HoneyHistory extends BaseTimeEntity {
    @Id
    @Tsid
    private Long honeyHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private HoneyWallet honeyWallet;

    @Column(nullable = false)
    private Long targetMemberId;

    @Column(nullable = false)
    private Integer amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HoneyHistoryType type;
}
