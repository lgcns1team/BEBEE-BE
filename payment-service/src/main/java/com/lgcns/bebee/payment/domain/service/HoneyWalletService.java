package com.lgcns.bebee.payment.domain.service;

import com.lgcns.bebee.payment.common.exception.PaymentErrors;
import com.lgcns.bebee.payment.domain.entity.HoneyWallet;
import com.lgcns.bebee.payment.domain.repository.HoneyWalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HoneyWalletService {

    private final HoneyWalletRepository honeyWalletRepository;

    @Transactional
    public HoneyWallet findByMemberId(Long memberId) {
        return honeyWalletRepository.findByMemberIdWithLock(memberId)
                .orElseThrow(() -> PaymentErrors.HONEY_WALLET_NOT_FOUND.toException());
    }

    /**
     * 회원의 현재 꿀 개수 조회
     * 잔액(balance)을 100으로 나눈 값을 반환
     *
     * @param memberId 회원 ID
     * @return 현재 꿀 개수 (잔액 / 100)
     */
    @Transactional(readOnly = true)
    public Long getCurrentHoney(Long memberId) {
        Optional<HoneyWallet> wallet = honeyWalletRepository.findByMemberId(memberId);
        Long balance = wallet.map(HoneyWallet::getBalance).orElse(0L);
        return balance / 100;
    }

    /**
     * 회원의 현재 잔액 조회
     *
     * @param memberId 회원 ID
     * @return 현재 잔액 (원 단위)
     */
    @Transactional(readOnly = true)
    public Long getCurrentBalance(Long memberId) {
        return honeyWalletRepository.findByMemberId(memberId)
                .map(HoneyWallet::getBalance)
                .orElse(0L);
    }

    /**
     * 꿀 개수를 잔액(원)으로 변환
     *
     * @param honeyCount 꿀 개수
     * @return 잔액 (원 단위)
     */
    public Long convertHoneyToBalance(Long honeyCount) {
        return honeyCount * 100;
    }

    /**
     * 잔액(원)을 꿀 개수로 변환
     *
     * @param balance 잔액 (원 단위)
     * @return 꿀 개수
     */
    public Long convertBalanceToHoney(Long balance) {
        return balance / 100;
    }
}
