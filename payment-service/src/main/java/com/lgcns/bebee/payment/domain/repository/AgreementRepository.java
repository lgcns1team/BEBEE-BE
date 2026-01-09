package com.lgcns.bebee.payment.domain.repository;

import com.lgcns.bebee.payment.domain.entity.sync.PaymentAgreementSync;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgreementRepository extends JpaRepository<PaymentAgreementSync, Long> {

    Optional<PaymentAgreementSync> findByAgreementId(Long agreementId);
}
