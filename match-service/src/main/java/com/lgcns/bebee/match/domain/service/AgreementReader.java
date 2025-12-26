package com.lgcns.bebee.match.domain.service;

import com.lgcns.bebee.match.common.exception.MatchErrors;
import com.lgcns.bebee.match.domain.entity.Agreement;
import com.lgcns.bebee.match.domain.repository.AgreementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AgreementReader {
    private final AgreementRepository agreementRepository;
    
    @Transactional(readOnly = true)
    public Agreement getById(Long agreementId) {
        return agreementRepository.findById(agreementId)
                .orElseThrow(() -> MatchErrors.AGREEMENT_NOT_FOUND.toException());
    }
}