package com.lgcns.bebee.payment.domain.entity;

import com.lgcns.bebee.common.data.domain.BaseTimeEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HoneyWallet extends BaseTimeEntity {
    @Id
    @Tsid
    private Long honeyWalletId;

    @Column(nullable = false, unique = true)
    private Long memberId;

    @Column(nullable = false)
    private Long balance = 0L;
}
