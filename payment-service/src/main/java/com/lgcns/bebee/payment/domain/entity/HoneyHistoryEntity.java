package com.lgcns.bebee.payment.domain.entity;

import com.lgcns.bebee.payment.domain.entity.vo.HoneyHistoryType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

public class HoneyHistoryEntity {
    @Id
    private Long honey_history_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private HoneyWalletEntity honeyWallet;

    @Column(nullable = false)
    private Long target_member_id;

    @Column(nullable = false)
    private Integer amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HoneyHistoryType type;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
