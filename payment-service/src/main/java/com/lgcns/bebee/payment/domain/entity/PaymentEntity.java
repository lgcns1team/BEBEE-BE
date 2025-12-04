package com.lgcns.bebee.payment.domain.entity;

import com.lgcns.bebee.payment.domain.entity.vo.PaymentStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

public class PaymentEntity {
    @Id
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

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
