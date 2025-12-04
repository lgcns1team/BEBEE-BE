package com.lgcns.bebee.payment.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class HoneyWalletEntity {
    @Id
    private Long honey_wallet_id;

    @Column(nullable = false, unique = true)
    private Long member_id;

    @Column(nullable = false)
    private Long balance = 0L;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
