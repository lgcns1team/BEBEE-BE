package com.lgcns.bebee.payment.domain.service;

import com.lgcns.bebee.payment.common.exception.PaymentErrors;
import com.lgcns.bebee.payment.domain.entity.sync.PaymentAgreementSync;
import com.lgcns.bebee.payment.domain.repository.AgreementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AgreementReader {
    private final AgreementRepository agreementRepository;

    @Transactional(readOnly = true)
    public PaymentAgreementSync findById(Long agreementId) {
        return agreementRepository.findByAgreementId(agreementId)
                .orElseThrow(() -> PaymentErrors.AGREEMENT_NOT_FOUND.toException());
    }
}
