package com.lgcns.bebee.payment.domain.repository;

import com.lgcns.bebee.payment.domain.entity.HoneyEscrow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HoneyEscrowRepository extends JpaRepository<HoneyEscrow, Long> {

    boolean existsByMatch_MatchId(Long matchId);

    Optional<HoneyEscrow> findByMatch_MatchId(Long matchId);
}
