package com.lgcns.bebee.payment.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import com.lgcns.bebee.payment.domain.entity.vo.PaymentStatus;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentEntity extends BaseTimeEntity {
    @Id
    @Tsid
    private Long payment_id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false, length = 200, unique = true)
    private String paymentKey;

    @Column(nullable = false, length = 64, unique = true)
    private String orderId;

    @Column(nullable = false)
    private Integer amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;
}
