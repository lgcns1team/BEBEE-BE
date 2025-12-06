package com.lgcns.bebee.payment.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HoneyWalletEntity extends BaseTimeEntity {
    @Id
    @Tsid
    private Long honey_wallet_id;

    @Column(nullable = false, unique = true)
    private Long member_id;

    @Column(nullable = false)
    private Long balance = 0L;
}
