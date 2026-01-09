package com.lgcns.bebee.payment.domain.service;

import com.lgcns.bebee.payment.common.exception.PaymentErrors;
import com.lgcns.bebee.payment.domain.entity.HoneyEscrow;
import com.lgcns.bebee.payment.domain.repository.HoneyEscrowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HoneyEscrowService {
    private final HoneyEscrowRepository honeyEscrowRepository;

    @Transactional(readOnly = true)
    public Boolean existsByMatchId(Long matchId) {
        if (honeyEscrowRepository.existsByMatch_MatchId(matchId)) {
            throw PaymentErrors.ESCROW_ALREADY_EXISTS.toException();
        }

        return true;
    }

    @Transactional(readOnly = true)
    public HoneyEscrow findByMatch_MatchId(Long matchId) {
        return honeyEscrowRepository.findByMatch_MatchId(matchId)
                .orElseThrow(() -> PaymentErrors.ESCROW_NOT_FOUND.toException());
    }
}
