package com.lgcns.bebee.payment.domain.entity;

import com.lgcns.bebee.common.data.domain.BaseTimeEntity;
import com.lgcns.bebee.payment.domain.entity.sync.PaymentMatchSync;
import com.lgcns.bebee.payment.domain.entity.vo.EscrowStatus;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HoneyEscrow extends BaseTimeEntity {

    @Id
    @Tsid
    private Long escrowId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false, unique = true)
    private PaymentMatchSync match;

    @Column(nullable = false)
    private Long disabledId; // 장애인: 꿀 차감, 환불 대상

    @Column(nullable = false)
    private Long helperId; // 도우미: 꿀 지급, 인출 대상

    @Column(nullable = false)
    private Long amount; // 임시 보관 금액(원 단위)

    @Column(nullable = false)
    private EscrowStatus status;

    @Column
    private LocalDateTime completedAt; // 도우미에게 전달 완료된 시각

    @Column
    private LocalDateTime cancelledAt; // 취소/환불된 시각 (매칭 중단 등의 사유로)

    public static HoneyEscrow create(
            PaymentMatchSync match,
            Long disabledId,
            Long helperId,
            Long amount,
            EscrowStatus status,
            LocalDateTime completedAt,
            LocalDateTime cancelledAt
    ) {
        HoneyEscrow honeyEscrow = new HoneyEscrow();
        honeyEscrow.match = match;
        honeyEscrow.disabledId = disabledId;
        honeyEscrow.helperId = helperId;
        honeyEscrow.amount = amount;
        honeyEscrow.status = status;
        honeyEscrow.completedAt = completedAt;
        honeyEscrow.cancelledAt = cancelledAt;

        return honeyEscrow;
    }

    /**
     * 계좌로 인출 (도우미)
     */
    public void transfer(Long amount) {
        this.completedAt = LocalDateTime.now();
        this.amount -= amount;
    }
}
