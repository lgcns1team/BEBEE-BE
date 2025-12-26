package com.lgcns.bebee.match.domain.repository;

import com.lgcns.bebee.match.domain.entity.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgreementRepository extends JpaRepository<Agreement, Long> {
    Optional<Agreement> findByPostId(Long postId);
}
